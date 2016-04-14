package base;

import org.owasp.encoder.Encode;

import utils.XssFilter;

public class TestXss {

    public static void pattern() {
        String js1 = "<script>alert(1);</script>";
        String js2 = "<script type=\"text/javascript\">alert(1);</script>";
        String js3 = "javascript:alert(1);";
        System.out.println(XssFilter.cleanXSS(js1));
        System.out.println(XssFilter.cleanXSS(js2));
        System.out.println(XssFilter.cleanXSS(js3));
    }

    public static void esapi() {
        String js1 = "<script>alert(1);</script>";
        String js2 = "<script type=\"text/javascript\">alert(1);</script>";
        String js3 = "javascript:alert(1);";
        System.out.println(Encode.forJavaScriptBlock(js1));
        System.out.println(Encode.forJavaScriptBlock(js2));
        System.out.println(Encode.forJavaScriptBlock(js3));
        System.out.println(Encode.forHtmlAttribute(js3));
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        pattern();
        esapi();
    }

}
