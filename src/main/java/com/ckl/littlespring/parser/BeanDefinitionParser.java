package com.ckl.littlespring.parser;

import com.ckl.littlespring.annotation.Component;
import com.ckl.littlespring.parser.aop.AdvisorConfig;
import com.ckl.littlespring.parser.aop.AspectConfig;
import com.ckl.littlespring.parser.aop.AspectMethodType;
import com.ckl.littlespring.parser.aop.PointCutConfig;
import com.ckl.littlespring.util.BeanDefinitionUtil;
import com.ckl.littlespring.util.ClassUtil;
import lombok.Data;
import org.apache.commons.digester3.CallMethodRule;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreateRule;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/7 0007
 * creat_time: 13:43
 **/
@Data
public class BeanDefinitionParser {
    /**
     * xml 解析器
     */
    private Digester digester;

    private String configFileLocation;


    private List<MyBeanDefiniton> myBeanDefinitonList = new ArrayList<>();

    /**
     * 存储切面配置信息
     */
    private List<AdvisorConfig> advisorConfigs = new ArrayList<>();

    public void addAspectConfig(AdvisorConfig advisorConfig) {
        this.advisorConfigs.add(advisorConfig);
    }

    public BeanDefinitionParser(String configFileLocation) {
        this.configFileLocation = configFileLocation;
        digester = new Digester();
    }

    /**
     * 根据指定规则，解析xml
     */
    public void parse() {
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);
        digester.setNamespaceAware(false);

        digester.push(this);

        // Configure the actions we will be using
        digester.addRule("beans/context:component-scan",
                new ComponentScanRule(this));
        digester.addRule("beans/bean",
                new BeanDefineRule(this));

        // 生成切面配置对象
        digester.addRule("beans/aop:config",
                new ObjectCreateRule(AdvisorConfig.class));
        digester.addRule( "beans/aop:config", new CallMethodRule(1,"addAspectConfig",1,new Class[]{AdvisorConfig.class} ) );
//        digester.addCallMethod("beans/aop:config","addAspectConfig",1,new Class[]{AdvisorConfig.class});
        digester.addCallParam("beans/aop:config",0,0);

        // 生成切点
        digester.addRule("beans/aop:config/aop:pointcut",new ObjectCreateRule(PointCutConfig.class));
        digester.addSetProperties("beans/aop:config/aop:pointcut");
        digester.addSetNext("beans/aop:config/aop:pointcut",
                "setPointCutConfig",
                "com.ckl.littlespring.parser.aop.PointCutConfig");

        // 生成切面,并需要在结束时设置到上层
        digester.addRule("beans/aop:config/aop:aspect",new ObjectCreateRule(AspectConfig.class));
        digester.addSetProperties("beans/aop:config/aop:aspect","ref","aspectRef");
        digester.addSetNext("beans/aop:config/aop:aspect",
                "setAspectConfig",
                "com.ckl.littlespring.parser.aop.AspectConfig");

        digester.addRule("beans/aop:config/aop:aspect/aop:before",new ObjectCreateRule(AspectConfig.AspectRuleItemConfig.class));
        digester.addCallMethod("beans/aop:config/aop:aspect/aop:before","setAspectMethodType", 1,new Class[]{AspectMethodType.class});
        digester.addObjectParam("beans/aop:config/aop:aspect/aop:before",0, AspectMethodType.ASPECT_METHOD_BEFORE);

        digester.addSetProperties("beans/aop:config/aop:aspect/aop:before","pointcut-ref","pointcutRef");
        digester.addSetNext("beans/aop:config/aop:aspect/aop:before",
                "addAspectRuleItemConfig",
                "com.ckl.littlespring.parser.aop.AspectConfig$AspectRuleItemConfig");




        InputSource inputSource = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileLocation);
            inputSource = new InputSource(inputStream);
            inputSource.setByteStream(inputStream);
            Object o = digester.parse(inputSource);
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 当遇到component-scan元素时，该函数被回调，解析指定包下面的bean 定义，并加入bean 定义集合
     * @param basePackage
     */
    public void doScanBasePackage(String basePackage) {
        Set<Class<?>> classSet = ClassUtil.getClasses(basePackage);

        if (classSet == null) {
            return;
        }

        //过滤出带有Component注解的类,并将其转换为beanDefinition
        List<Class<?>> list = classSet.stream().filter(clazz -> clazz.getAnnotation(Component.class) != null).collect(Collectors.toList());

        for (Class<?> clazz : list) {
            String beanName = clazz.getAnnotation(Component.class).value();
            MyBeanDefiniton myBeanDefiniton = BeanDefinitionUtil.convert2BeanDefinition(clazz, beanName);
            myBeanDefinitonList.add(myBeanDefiniton);
        }

    }

    public List<MyBeanDefiniton> getBeanDefinitions() {
        return myBeanDefinitonList;
    }

    /**
     * 定义单个bean，对应于xml中的 <bean></bean>
     * @param id
     * @param clazzName
     */
    public void doDefineBean(String id, String clazzName) {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);

            MyBeanDefiniton myBeanDefiniton = BeanDefinitionUtil.convert2BeanDefinition(clazz,id);
            myBeanDefinitonList.add(myBeanDefiniton);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found by name:" + clazzName);
        }

    }
}
