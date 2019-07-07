package com.ckl.littlespring.core;

import com.ckl.littlespring.parser.BeanDefinitionParser;
import com.ckl.littlespring.parser.MyBeanDefiniton;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 13:35
 **/
@Data
public class BeanDefinitionRegistry {

    /**
     * map：存储 bean的class-》bean实例
     */
    private Map<Class, Object> beanMapByClass = new ConcurrentHashMap<>();

    /**
     * bean定义解析器
     */
    private BeanDefinitionParser parser;

    /**
     * bean定义是否解析完成
     */
    private Boolean hasBeanDefinitionParseOver = false;


    public BeanDefinitionRegistry(String configFileLocation) {
        parser = new BeanDefinitionParser(configFileLocation, this);
    }


    public void refresh() {
        /**
         * 判断是否已经解析完成bean定义。如果没有完成，则先进行解析
         */
        if (!hasBeanDefinitionParseOver) {
            parser.parse();
            hasBeanDefinitionParseOver = true;
        }

        /**
         * 初始化所有的bean，完成自动注入
         */
        for (MyBeanDefiniton beanDefiniton : getBeanDefinitions()) {
            getBean(beanDefiniton);
        }
    }

    /**
     * 根据bean 定义获取bean
     * 1、先查bean容器，查到则返回
     * 2、生成bean，放进容器（此时，依赖还没注入，主要是解决循环依赖问题）
     * 3、注入依赖
     *
     * @param beanDefiniton
     * @return
     */
    private Object getBean(MyBeanDefiniton beanDefiniton) {
        Class<?> beanClazz = beanDefiniton.getBeanClazz();
        Object bean = beanMapByClass.get(beanClazz);
        if (bean != null) {
            return bean;
        }

        //没查到的话，说明还没有，需要去生成bean，然后放进去
        try {
            bean = beanClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        // 先行暴露，解决循环依赖问题
        beanMapByClass.put(beanClazz, bean);

        //注入依赖
        List<Field> dependencysByField = beanDefiniton.getDependencysByField();
        if (dependencysByField == null) {
            return bean;
        }

        for (Field field : dependencysByField) {
            try {
                autowireField(beanClazz, bean, field);
            } catch (Exception e) {
                throw new RuntimeException(beanClazz.getName() + " 创建失败",e);
            }
        }

        return bean;
    }

    private void autowireField(Class<?> beanClazz, Object bean, Field field) {
        Class<?> fieldType = field.getType();
        List<MyBeanDefiniton> beanDefinitons = getBeanDefinitions();
        if (beanDefinitons == null) {
            return;
        }

        // 根据类型去所有beanDefinition看，哪个类型是该类型的子类;把满足的都找出来
        List<MyBeanDefiniton> candidates = beanDefinitons.stream().filter(myBeanDefiniton -> {
            return fieldType.isAssignableFrom(myBeanDefiniton.getBeanClazz());
        }).collect(Collectors.toList());

        if (candidates == null || candidates.size() == 0) {
            throw new RuntimeException(beanClazz.getName() + "根据类型自动注入失败。field:" + field.getName() + " 无法注入，没有候选bean");
        }
        if (candidates.size() > 1) {
            throw new RuntimeException(beanClazz.getName() + "根据类型自动注入失败。field:" + field.getName() + " 无法注入，有多个候选bean" + candidates);
        }

        MyBeanDefiniton candidate = candidates.get(0);
        Object fieldBean;
        try {
            // 递归调用
            fieldBean = getBean(candidate);
            field.setAccessible(true);
            field.set(bean, fieldBean);
        } catch (Exception e) {
            throw new RuntimeException("注入属性失败:" + beanClazz.getName() + "##" + field.getName(), e);
        }


    }

    /**
     * 根据类型获取bean对象
     *
     * @param clazz
     * @return
     */
    public Object getBeanByType(Class clazz) {
        return beanMapByClass.get(clazz);
    }

    private List<MyBeanDefiniton> getBeanDefinitions() {
        return parser.getBeanDefinitions();
    }
}
