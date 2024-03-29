package com.coder.rule;

import com.coder.GirlFriendHandlerVersion2;
import com.coder.TwoTuple;
import org.xml.sax.Attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/1 0001
 * creat_time: 11:33
 **/
public class SetPropertiesParseRule implements ParseRule {
    private GirlFriendHandlerVersion2 girlFriendHandler;

    public SetPropertiesParseRule(GirlFriendHandlerVersion2 girlFriendHandler) {
        this.girlFriendHandler = girlFriendHandler;
    }

    @Override
    public void startElement(Attributes attributes) {
        Object object = girlFriendHandler.peek();

        setProperties(attributes,object);
    }

    @Override
    public void body(String body) {

    }

    @Override
    public void endElement() {

    }

    private void setProperties(Attributes attributes, Object object) {
        Method[] methods = object.getClass().getMethods();
        ArrayList<Method> list = new ArrayList<>();
        list.addAll(Arrays.asList(methods));
        list.removeIf(o -> o.getParameterCount() != 1);


        for (int i = 0; i < attributes.getLength(); i++) {
            // 获取属性名
            String attributesQName = attributes.getQName(i);
            String setterMethod = "set" + attributesQName.substring(0, 1).toUpperCase() + attributesQName.substring(1);

            String value = attributes.getValue(i);
            TwoTuple<Method, Object[]> tuple = getSuitableMethod(list, setterMethod, value);
            // 没有找到合适的方法
            if (tuple == null) {
                continue;
            }

            Method method = tuple.first;
            Object[] params = tuple.second;
            try {
                method.invoke(object,params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private TwoTuple<Method, Object[]> getSuitableMethod(List<Method> list, String setterMethod, String value) {

        for (Method method : list) {

            if (!Objects.equals(method.getName(), setterMethod)) {
                continue;
            }

            Object[] params = new Object[1];

            /**
             * 1；如果参数类型就是String，那么就是要找的
             */
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> parameterType = parameterTypes[0];
            if (parameterType.equals(String.class)) {
                params[0] = value;
                return new TwoTuple<>(method,params);
            }

            Boolean ok = true;

            // 看看int是否可以转换
            String name = parameterType.getName();
            if (name.equals("java.lang.Integer")
                    || name.equals("int")){
                try {
                    params[0] = Integer.valueOf(value);
                }catch (NumberFormatException e){
                    ok = false;
                    e.printStackTrace();
                }
                // 看看 long 是否可以转换
            }else if (name.equals("java.lang.Long")
                    || name.equals("long")){
                try {
                    params[0] = Long.valueOf(value);
                }catch (NumberFormatException e){
                    ok = false;
                    e.printStackTrace();
                }
                // 如果int 和 long 不行，那就只有尝试boolean了
            }else if (name.equals("java.lang.Boolean") ||
                    name.equals("boolean")){
                params[0] = Boolean.valueOf(value);
            }

            if (ok){
                return new TwoTuple<Method,Object[]>(method,params);
            }
        }
        return null;
    }
}
