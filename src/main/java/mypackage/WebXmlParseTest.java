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
 * creat_time: 11:15
 **/
public class WebXmlParseTest {
    public static void main(String[] args) {
        Digester digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("web-app/servlet",
                "mypackage.ServletBean");
        digester.addCallMethod("web-app/servlet/servlet-name", "setServletName", 0);
        digester.addCallMethod("web-app/servlet/servlet-class",
                "setServletClass", 0);
        digester.addCallMethod("web-app/servlet/init-param",
                "addInitParam", 2);
        digester.addCallParam("web-app/servlet/init-param/param-name", 0);
        digester.addCallParam("web-app/servlet/init-param/param-value", 1);

        InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("web.xml");
        try {
            ServletBean servletBean = (ServletBean) digester.parse(inputStream);
            System.out.println(servletBean);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
