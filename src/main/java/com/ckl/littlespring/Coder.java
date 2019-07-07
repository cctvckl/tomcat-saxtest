package com.ckl.littlespring;

import com.ckl.littlespring.annotation.Autowired;
import com.ckl.littlespring.annotation.Component;
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
@Component
public class Coder {
    private String name = "xiaoming";

    private String sex;

    private String love;
    /**
     * 女朋友
     */
    @Autowired
    private com.ckl.littlespring.Girl girl;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coder{");
        sb.append("name='").append(name).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", love='").append(love).append('\'');
        sb.append(", girl='").append(girl.hashCode()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
