package com.ckl.littlespring.parser;

import com.ckl.littlespring.core.BeanDefinitionRegistry;
import com.ckl.littlespring.annotation.Component;
import com.ckl.littlespring.util.BeanDefinitionUtil;
import com.ckl.littlespring.util.ClassUtil;
import lombok.Data;
import org.apache.commons.digester3.Digester;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 13:43
 **/
@Data
public class BeanDefinitionParser {
    /**
     * xml 解析器
     */
    private Digester digester;

    private String configFileLocation;


    private List<MyBeanDefiniton> myBeanDefinitonList = new ArrayList<>();



    public BeanDefinitionParser(String configFileLocation) {
        this.configFileLocation = configFileLocation;
        digester = new Digester();
    }

    /**
     * 根据指定规则，解析xml
     */
    public void parse() {
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

        // Configure the actions we will be using
        digester.addRule("beans/component-scan",
                new ComponentScanRule(this));

        InputSource inputSource = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileLocation);
            inputSource = new InputSource(inputStream);
            inputSource.setByteStream(inputStream);
            Object o = digester.parse(inputSource);
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 当遇到component-scan元素时，该函数被回调，解析指定包下面的bean 定义，并加入bean 定义集合
     * @param basePackage
     */
    public void doScanBasePackage(String basePackage) {
        Set<Class<?>> classSet = ClassUtil.getClasses(basePackage);

        if (classSet == null) {
            return;
        }

        //过滤出带有Component注解的类,并将其转换为beanDefinition
        List<Class<?>> list = classSet.stream().filter(clazz -> clazz.getAnnotation(Component.class) != null).collect(Collectors.toList());

        for (Class<?> clazz : list) {
            MyBeanDefiniton myBeanDefiniton = BeanDefinitionUtil.convert2BeanDefinition(clazz);
            myBeanDefinitonList.add(myBeanDefiniton);
        }

    }

    public List<MyBeanDefiniton> getBeanDefinitions() {
        return myBeanDefinitonList;
    }
}
