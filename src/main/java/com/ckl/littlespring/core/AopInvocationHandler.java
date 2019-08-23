package com.ckl.littlespring.core;

import com.ckl.littlespring.MyPointParser;
import com.ckl.littlespring.parser.aop.AdvisorConfig;
import com.ckl.littlespring.parser.aop.AspectConfig;
import com.ckl.littlespring.parser.aop.AspectMethodType;
import com.ckl.littlespring.parser.aop.PointCutConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/8/22 0022
 * creat_time: 14:30
 **/
public class AopInvocationHandler implements InvocationHandler {
    /**
     * bean工厂
     */
    private BeanDefinitionRegistry beanDefinitionRegistry;

    /**
     * 被代理的bean
     */
    private Object targetInstance;

    /**
     * 所有的切面配置
     */
    private List<AdvisorConfig> advisorConfigs;

    MyPointParser parser = new MyPointParser();

    public AopInvocationHandler(BeanDefinitionRegistry beanDefinitionRegistry, Object instance, List<AdvisorConfig> advisorConfigs) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.targetInstance = instance;
        this.advisorConfigs = advisorConfigs;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ArrayList<AdvisorConfig> matchedAdvisorConfigs = new ArrayList<>();
        for (AdvisorConfig advisorConfig : this.advisorConfigs) {
            PointCutConfig pointCutConfig = advisorConfig.getPointCutConfig();
            PointcutExpression pointcutExpression = parser.parsePointcutExpression(pointCutConfig.getExpression());
            ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
            if (shadowMatch.maybeMatches()){
                matchedAdvisorConfigs.add(advisorConfig);
            }
        }

        // 如果没有匹配的方法，直接返回
        if (matchedAdvisorConfigs.size() == 0) {
            return method.invoke(targetInstance,args);
        }

        /**
         * 执行before切面
         */
        for (AdvisorConfig matchedAdvisorConfig : matchedAdvisorConfigs) {
            AspectConfig aspectConfig = matchedAdvisorConfig.getAspectConfig();
            Object aspectRef = aspectConfig.getAspectRef();
            Object aspect = beanDefinitionRegistry.getBeanByName((String) aspectRef);

            List<AspectConfig.AspectRuleItemConfig> list = aspectConfig.getAspectRuleItemConfigs();

            for (AspectConfig.AspectRuleItemConfig aspectRuleItemConfig : list) {
                if (aspectRuleItemConfig.getAspectMethodType().getType().equals(AspectMethodType.ASPECT_METHOD_BEFORE.getType())){
                    Method aspectMethod = aspect.getClass().getMethod(aspectRuleItemConfig.getMethod());
                    aspectMethod.invoke(aspect);
                }
            }
        }

        return method.invoke(targetInstance,args);
    }
}
