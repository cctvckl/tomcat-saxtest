package com.ckl.littlespring.parser;

import org.apache.commons.digester3.Rule;
import org.xml.sax.Attributes;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 13:47
 **/
public class BeanDefineRule extends Rule {

    private String id;

    private String clazz;

    private BeanDefinitionParser beanDefinitionParser;

    public BeanDefineRule(BeanDefinitionParser beanDefinitionParser) {
        this.beanDefinitionParser = beanDefinitionParser;
    }

    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {

        id = attributes.getValue("id");
        clazz = attributes.getValue("class");
        beanDefinitionParser.doDefineBean(id,clazz);
    }

    @Override
    public void end(String namespace, String name) throws Exception {

    }
}
