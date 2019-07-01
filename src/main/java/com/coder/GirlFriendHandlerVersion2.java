package com.coder;

import com.coder.rule.CreateObjectParseRule;
import com.coder.rule.ParentChildRule;
import com.coder.rule.ParseRule;
import com.coder.rule.SetPropertiesParseRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * desc:
 * @author: caokunliang
 * creat_date: 2019/6/29 0029
 * creat_time: 11:06
 **/
public class GirlFriendHandlerVersion2 extends DefaultHandler {
    private LinkedList<Object> stack = new LinkedList<>();

    /**
     * 规则定义。每个元素可以有多条规则，所以value是一个list。解析时，会按顺序调用各个规则
     */
    private ConcurrentHashMap<String, List<ParseRule>> ruleMap = new ConcurrentHashMap<>();

    {
        ArrayList<ParseRule> rules = new ArrayList<>();
        rules.add(new CreateObjectParseRule("class",this));
        rules.add(new SetPropertiesParseRule(this));

        ruleMap.put("Coder",rules);

        rules = new ArrayList<>();
        rules.add(new CreateObjectParseRule("class",this));
        rules.add(new SetPropertiesParseRule(this));
        rules.add(new ParentChildRule("setGirl", this));

        ruleMap.put("Girl",rules);
    }

    private AtomicInteger eventOrderCounter = new AtomicInteger(0);

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("startElement: " + qName + " It's the " + eventOrderCounter.getAndIncrement() + " one");

        List<ParseRule> rules = ruleMap.get(qName);
        for (ParseRule rule : rules) {
            rule.startElement(attributes);
        }

    }



    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("endElement: " + qName + " It's the " + eventOrderCounter.getAndIncrement() + " one");

        List<ParseRule> rules = ruleMap.get(qName);
        if (rules != null) {
            for (ParseRule rule : rules) {
                rule.endElement();
            }
        }

    }

    public static void main(String[] args) {
        GirlFriendHandlerVersion2 handler = new GirlFriendHandlerVersion2();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {
            SAXParser parser = spf.newSAXParser();
            InputStream inputStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("girlfriend.xml");

            parser.parse(inputStream, handler);
            Object o = handler.stack.pop();
            System.out.println(o);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 栈内弹出栈顶对象
     * @return
     */
    public Object pop(){
        return stack.pop();
    }

    /**
     * 栈顶push元素
     * @param object
     */
    public void push(Object object){
        stack.push(object);
    }

    /**
     * 返回栈顶元素，但不弹出
     */
    public Object peek(){
        return stack.peek();
    }
}
