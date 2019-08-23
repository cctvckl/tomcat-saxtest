package com.ckl.littlespring.parser.aop;

import lombok.Data;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/24 0024
 * creat_time: 20:21
 **/
@Data
public class AdvisorConfig {

    /**
     * 切点
     */
    private PointCutConfig pointCutConfig;

    /**
     * 切面配置
     */
    private AspectConfig aspectConfig;

    public void setPointCutConfig(PointCutConfig pointCutConfig) {
        this.pointCutConfig = pointCutConfig;
    }

    public void setAspectConfig(AspectConfig aspectConfig) {
        this.aspectConfig = aspectConfig;
    }
}
