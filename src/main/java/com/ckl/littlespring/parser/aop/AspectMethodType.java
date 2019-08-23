package com.ckl.littlespring.parser.aop;

import lombok.Data;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/25 0025
 * creat_time: 8:40
 **/
public enum AspectMethodType {
    ASPECT_METHOD_BEFORE(1,"切面中的before类型切面")    ;


    private Integer type;

    private String desc;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    AspectMethodType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
