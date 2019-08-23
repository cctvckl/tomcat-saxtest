package com.ckl.littlespring.util;

import com.ckl.littlespring.annotation.Autowired;
import com.ckl.littlespring.parser.MyBeanDefiniton;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 14:26
 **/
public class BeanDefinitionUtil {

    public static MyBeanDefiniton convert2BeanDefinition(Class<?> clazz,String beanName){
        MyBeanDefiniton definiton = new MyBeanDefiniton();
        String name = clazz.getSimpleName();
        if (StringUtils.isBlank(beanName)){
            definiton.setBeanName(name.substring(0,1).toLowerCase() + name.substring(1));
        }else {
            definiton.setBeanName(beanName);
        }
        definiton.setBeanType(clazz.getCanonicalName());
        definiton.setBeanClazz(clazz);

        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0){
            return definiton;
        }

        ArrayList<Field> list = new ArrayList<>();
        list.addAll(Arrays.asList(fields));
        List<Field> dependencysField = list.stream().filter(field -> field.getAnnotation(Autowired.class) != null).collect(Collectors.toList());
        definiton.setDependencysByField(dependencysField);

        return definiton;
    }
}
