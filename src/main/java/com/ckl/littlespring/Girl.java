package com.ckl.littlespring;

import com.ckl.littlespring.annotation.Autowired;
import com.ckl.littlespring.annotation.Component;
import com.coder.SaxTest;
import lombok.Data;

/**
 * desc:
 * @author: caokunliang
 * creat_date: 2019/6/29 0029
 * creat_time: 11:13
 **/
@Data
@Component
public class Girl {
    private String name = "catalina";
    private String height;
    private String breast;
    private String legLength;

    private Boolean isPregnant;

    @Autowired
    private com.ckl.littlespring.Coder coder;

//    @Autowired
    private SaxTest saxTest;


}
