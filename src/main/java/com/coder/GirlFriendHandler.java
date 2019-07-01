package com.coder;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * desc:
 * @author: caokunliang
 * creat_date: 2019/6/29 0029
 * creat_time: 11:06
 **/
public class GirlFriendHandler  extends DefaultHandler {
    private LinkedList<Object> stack = new LinkedList<>();

    private AtomicInteger eventOrderCounter = new AtomicInteger(0);

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("startElement: " + qName + " It's the " + eventOrderCounter.getAndIncrement() + " one");

        if ("Coder".equals(qName)) {

            Coder coder = new Coder();

            setProperties(attributes,coder);

            stack.push(coder);
        } else if ("Girl".equals(qName)) {

            Girl girl = new Girl();
            setProperties(attributes, girl);

            Coder coder = (Coder) stack.peek();
            coder.setGirl(girl);
        }
    }

    private void setProperties(Attributes attributes, Object object) {
        Method[] methods = object.getClass().getMethods();
        ArrayList<Method> list = new ArrayList<>();
        list.addAll(Arrays.asList(methods));
        list.removeIf(o -> o.getParameterCount() != 1);


        for (int i = 0; i < attributes.getLength(); i++) {
            // 获取属性名
            String attributesQName = attributes.getQName(i);
            String setterMethod = "set" + attributesQName.substring(0, 1).toUpperCase() + attributesQName.substring(1);

            String value = attributes.getValue(i);
            TwoTuple<Method, Object[]> tuple = getSuitableMethod(list, setterMethod, value);
            // 没有找到合适的方法
            if (tuple == null) {
                continue;
            }

            Method method = tuple.first;
            Object[] params = tuple.second;
            try {
                method.invoke(object,params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private TwoTuple<Method, Object[]> getSuitableMethod(List<Method> list, String setterMethod, String value) {

        for (Method method : list) {

            if (!Objects.equals(method.getName(), setterMethod)) {
                continue;
            }

            Object[] params = new Object[1];

            /**
             * 1；如果参数类型就是String，那么就是要找的
             */
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> parameterType = parameterTypes[0];
            if (parameterType.equals(String.class)) {
                params[0] = value;
                return new TwoTuple<>(method,params);
            }

            Boolean ok = true;

            // 看看int是否可以转换
            String name = parameterType.getName();
            if (name.equals("java.lang.Integer")
                    || name.equals("int")){
                try {
                    params[0] = Integer.valueOf(value);
                }catch (NumberFormatException e){
                    ok = false;
                    e.printStackTrace();
                }
                // 看看 long 是否可以转换
            }else if (name.equals("java.lang.Long")
                    || name.equals("long")){
                try {
                    params[0] = Long.valueOf(value);
                }catch (NumberFormatException e){
                    ok = false;
                    e.printStackTrace();
                }
                // 如果int 和 long 不行，那就只有尝试boolean了
            }else if (name.equals("java.lang.Boolean") ||
                    name.equals("boolean")){
                params[0] = Boolean.valueOf(value);
            }

            if (ok){
                return new TwoTuple<Method,Object[]>(method,params);
            }
        }
        return null;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("endElement: " + qName + " It's the " + eventOrderCounter.getAndIncrement() + " one");

        if ("Coder".equals(qName)){
            Object o = stack.pop();
            System.out.println(o);
        }
    }

    public static void main(String[] args) {
        GirlFriendHandler handler = new GirlFriendHandler();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = spf.newSAXParser();
            InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("girlfriend.xml");

            parser.parse(inputStream, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
