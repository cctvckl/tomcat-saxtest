package com.ckl.littlespring.parser.aop;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/24 0024
 * creat_time: 20:21
 **/
@Data
public class AspectConfig {


    /**
     * 切面所在的类的对象
     */
    private Object aspectRef;

    /**
     * 切面细节，比如方法前类型切面，执行哪个方法；方法后切面，在方法执行后，执行哪个方法
     */
    private List<AspectRuleItemConfig> aspectRuleItemConfigs = new ArrayList<>();

    @Data
    public static class AspectRuleItemConfig{
        private String pointcutRef;

        private AspectMethodType aspectMethodType;

        private String method;

        public void setAspectMethodType(AspectMethodType aspectMethodType) {
            this.aspectMethodType = aspectMethodType;
        }
    }

    public void addAspectRuleItemConfig(AspectRuleItemConfig aspectRuleItemConfig) {
        aspectRuleItemConfigs.add(aspectRuleItemConfig);
    }
}
