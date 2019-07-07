package com.ckl.littlespring;

import com.ckl.littlespring.core.BeanDefinitionRegistry;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 13:40
 **/
public class TestLittleSpring {


    public static void main(String[] args) {
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry("springcontext-config.xml");
        registry.refresh();

        Object o = registry.getBeanByType(com.ckl.littlespring.Coder.class);
        System.out.println(o);

        Object o1 = registry.getBeanByType(com.ckl.littlespring.Girl.class);
        System.out.println(o1);


    }
}
