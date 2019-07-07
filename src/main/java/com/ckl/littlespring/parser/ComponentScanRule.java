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
public class ComponentScanRule extends Rule {

    private String basePackage;

    private BeanDefinitionParser beanDefinitionParser;

    public ComponentScanRule(BeanDefinitionParser beanDefinitionParser) {
        this.beanDefinitionParser = beanDefinitionParser;
    }

    @Override
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        basePackage = attributes.getValue("base-package");
        beanDefinitionParser.doScanBasePackage(basePackage);
    }

    @Override
    public void end(String namespace, String name) throws Exception {

    }
}
