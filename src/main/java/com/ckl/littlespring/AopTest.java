package com.ckl.littlespring;

import com.ckl.littlespring.controller.TestController;
import com.ckl.littlespring.core.BeanDefinitionRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.*;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/19 0019
 * creat_time: 19:35
 **/
@Slf4j
public class AopTest {
    public static void main(String[] args) throws NoSuchMethodException {
        MyPointParser parser = new MyPointParser();
//        PointcutExpression pointcutExpression = parser.parsePointcutExpression("execution(* com.practice.spring.aop.model.Product.add(..))");
//        PointcutExpression pointcutExpression = parser.parsePointcutExpression("execution(* com.ckl.littlespring.*.BeanDefinitionRegistry.*(java.lang.Class))");
        PointcutExpression pointcutExpression = parser.parsePointcutExpression("execution(* com.ckl.littlespring.controller.TestController.*(..))");
        System.out.println(pointcutExpression);
        boolean b = pointcutExpression.couldMatchJoinPointsInType(TestController.class);
        log.info("b:{}",b);

        Method[] methods = TestController.class.getMethods();
        ShadowMatch match = null;
        List<Method> list = new ArrayList<>();
        for (Method methoditem : methods) {
            match = pointcutExpression.matchesMethodExecution(methoditem);
            if (match.maybeMatches()){
                list.add(methoditem);
            }
        }
        System.out.println(list);
        Set<Method> hashSetAll = new HashSet<>();
        hashSetAll.addAll(Arrays.asList(methods));

        Set<Method> methodsPart = new HashSet<Method>(list);

        System.out.println("exclude :" + hashSetAll.removeAll(list));
        System.out.println(match.alwaysMatches());

    }

    public  void add() {
        System.out.println("add");
    }

    public static void v1(String[] args) {
//        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
//        pc.setExpression(expression);
    }
}
