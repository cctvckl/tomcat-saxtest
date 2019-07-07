package com.coder;

import com.ckl.littlespring.annotation.Autowired;
import com.ckl.littlespring.annotation.Component;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * desc:
 * @author: caokunliang
 * creat_date: 2019/6/29 0029
 * creat_time: 11:12
 **/
@Getter
@Setter
public class Coder {
    private String name;

    private String sex;

    private String love;

    private Girl girl;
}
