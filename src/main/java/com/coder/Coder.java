package com.coder;

import lombok.Data;

/**
 * desc:
 * @author: caokunliang
 * creat_date: 2019/6/29 0029
 * creat_time: 11:12
 **/
@Data
public class Coder {
    private String name;

    private String sex;

    private String love;
    /**
     * 女朋友
     */
    private Girl girl;
}
