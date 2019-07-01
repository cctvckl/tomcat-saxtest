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
    /**
     * 遇到xml元素的开始标记时，调用该方法。
     * @param attributes 元素中的属性
     */
    void startElement(Attributes attributes);

    void body(String body);

    void endElement();
}
