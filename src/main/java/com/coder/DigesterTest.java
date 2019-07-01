package com.coder;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.ObjectCreateRule;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2019/6/28.
 */
public class DigesterTest {

    public static void main(String[] args) {

        final Digester digester = new Digester();
        digester.setValidating(false);
//        digester.setRulesValidation(true);
        HashMap<Class<?>, List<String>> fakeAttributes =
                new HashMap<Class<?>, List<String>>();
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("className");
        fakeAttributes.put(Object.class, attrs);
//        digester.setFakeAttributes(fakeAttributes);
        digester.setUseContextClassLoader(true);

        // Configure the actions we will be using
        digester.addObjectCreate("Coder",
                "com.coder.Coder",
                "className");
        digester.addRule("Coder", new ObjectCreateRule(Coder.class) {
            @Override
            public void end(String namespace, String name) throws Exception {
                Object top = digester.pop();
                System.out.println("wo shi coder:" + top);
            }

            @Override
            public void begin(String namespace, String name, Attributes attributes) throws Exception {
                super.begin(namespace, name, attributes);

                throw new RuntimeException("hahha...");
            }
        });
        digester.addSetProperties("Coder");
//        digester.addSetNext("Coder",
//                "setServer",
//                "org.apache.catalina.Server");

        digester.addObjectCreate("Coder/Girl",
                "com.coder.Girl",
                "className");
        digester.addSetProperties("Coder/Girl");
        digester.addSetNext("Coder/Girl",
                "setGirl",
                "com.coder.Girl");


//
//        digester.addRule("Coder/Girl", new Rule() {
//            @Override
//            public void finish() throws Exception {
//                super.finish();
//                System.out.println("doc finish");
//            }
//        });


        parse(digester);

//        Object o = digester.pop();
//        System.out.println(o);
    }

    public static void parse(Digester digester) {
        InputSource inputSource = null;
        InputStream inputStream = null;
        File file = null;
        try {
            inputStream = DigesterTest.class.getClassLoader()
                    .getResourceAsStream("girlfriend.xml");
            inputSource = new InputSource(inputStream);
            inputSource.setByteStream(inputStream);
            digester.push(digester);
            Object o = digester.parse(inputSource);
            System.out.println(o);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
