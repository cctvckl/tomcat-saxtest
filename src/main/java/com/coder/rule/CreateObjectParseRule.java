package com.coder.rule;

import com.coder.GirlFriendHandler;
import com.coder.GirlFriendHandlerVersion2;
import org.xml.sax.Attributes;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/1 0001
 * creat_time: 11:20
 **/
public class CreateObjectParseRule implements ParseRule {
    private String attributeNameForObjectType;

    private ClassLoader loader;

    private GirlFriendHandlerVersion2 girlFriendHandler;


    public CreateObjectParseRule(String attributeNameForObjectType, GirlFriendHandlerVersion2 girlFriendHandler) {
        this.attributeNameForObjectType = attributeNameForObjectType;
        this.girlFriendHandler = girlFriendHandler;
        //默认使用当前线程类加载器
        loader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void startElement(Attributes attributes) {
        String clazzStr = attributes.getValue(attributeNameForObjectType);
        if (clazzStr == null) {
            throw new RuntimeException("element must has attribute :" + attributeNameForObjectType);
        }

        Class<?> clazz;
        try {
            clazz = loader.loadClass(clazzStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("class not found:" + clazzStr);
        }

        Object o;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("new instance failed.");
        }

        girlFriendHandler.push(o);
    }

    @Override
    public void body(String body) {

    }

    @Override
    public void endElement() {

    }
}
