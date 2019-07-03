package mypackage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/3 0003
 * creat_time: 11:08
 **/
@Data
public class ServletBean {
    private String servletName;
    private String servletClass;

    private List<InitParam> initParams = new ArrayList<>();

    public void addInitParam(String name, String value){
        initParams.add(new InitParam(name,value));
    }

}
