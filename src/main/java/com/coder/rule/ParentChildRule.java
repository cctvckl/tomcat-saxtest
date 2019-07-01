package com.coder.rule;

import com.coder.GirlFriendHandlerVersion2;
import org.xml.sax.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/1 0001
 * creat_time: 14:28
 **/
public class ParentChildRule implements ParseRule{
    /**
     * 父对象的方法名，通过该方法将子对象设置进去
     */
    private String parentObjectSetter;

    private GirlFriendHandlerVersion2 girlFriendHandler;

    public ParentChildRule(String parentObjectSetter, GirlFriendHandlerVersion2 girlFriendHandler) {
        this.parentObjectSetter = parentObjectSetter;
        this.girlFriendHandler = girlFriendHandler;
    }

    @Override
    public void startElement(Attributes attributes) {

    }

    @Override
    public void body(String body) {

    }

    @Override
    public void endElement() {
        // 获取到栈顶对象child，该对象将作为child，被设置到parent中
        Object child = girlFriendHandler.pop();
        //栈顶的child被弹出后，继续调用peek，将获取到parent
        Object parent = girlFriendHandler.peek();

        try {
            Method method = parent.getClass().getMethod(parentObjectSetter, new Class[]{child.getClass()});
            method.invoke(parent,child);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
