package mypackage;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/7/3 0003
 * creat_time: 10:22
 **/
public class Test {
    public static void main(String[] args) {
        Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("foo", "mypackage.Foo");
        digester.addSetProperties("foo");
        digester.addObjectCreate("foo/bar", "mypackage.Bar");
        digester.addSetProperties("foo/bar");
        digester.addSetNext("foo/bar", "addBar", "mypackage.Bar");
        InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("bar.xml");
        try {
            Foo foo = (Foo) digester.parse(inputStream);
            System.out.println(foo);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
