package com.coder.rule;

import org.xml.sax.Attributes;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/1 0001
 * creat_time: 11:13
 **/
public interface ParseRule {
    void startElement(Attributes attributes);

    void body(String body);

    void endElement();
}
