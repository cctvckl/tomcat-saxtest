package com.ckl.littlespring.parser;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 14:19
 **/
@Data
public class MyBeanDefiniton {

    /**
     * bean的名字，默认使用类名，将首字母变成小写
     */
    private String beanName;

    /**
     * bean的类型
     */
    private String beanType;

    /**
     * bean的class
     */
    private Class<?> beanClazz;

    /**
     * field依赖的bean
     */
    private List<Field> dependencysByField;


}
