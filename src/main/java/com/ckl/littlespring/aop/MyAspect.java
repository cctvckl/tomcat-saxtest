package com.ckl.littlespring.aop;

import org.aspectj.lang.JoinPoint;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/24 0024
 * creat_time: 13:56
 **/
public class MyAspect {

    public void before(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        Object args[] =jp.getArgs();
        System.out.println(" I am befor advice to :" + methodName);

    }

    public void before() {
        System.out.println(" I am befor advice to :");

    }
}
