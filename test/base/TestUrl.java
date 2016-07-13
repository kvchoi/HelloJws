package base;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TestUrl {

    public static void main(String[] args) throws Exception {
        String url1 = "http://m.pp.cn/home.html";
        String url2 = "http://127.0.0.1:9020/home.html";
        String url3 = "http://server.m.pp.cn:80/home.html";
        String url4 = "http://m.pp.cn/update.html";
        System.out.println(getBaseDomain(url1));
        System.out.println(getBaseDomain(url2));
        System.out.println(getBaseDomain(url3));
        System.out.println(getBaseDomain(url4));
        System.out.println(isSameSite(url1, url4));
        long a = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            isSameSite(url1, url4);
        }
        long b = System.currentTimeMillis();
        System.out.println(b-a);
    }

    public static boolean isSameSite(String url1, String url2) {
        return StringUtils.equals(getBaseDomain(url1), getBaseDomain(url2));
    }
    
    public static boolean isSameSite2(String site1, String site2) {
        return StringUtils.equalsIgnoreCase(StringUtils.removeEnd(getBaseDomain2(site1), ":80"),
                StringUtils.removeEnd(getBaseDomain2(site2), ":80"));
    }

    public static String getBaseDomain(String url) {
        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY;
        }
        StringBuilder base = new StringBuilder();
        URL u = null;
        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return StringUtils.EMPTY;
        }
        String protocal = u.getProtocol();
        if (StringUtils.isNotEmpty(protocal)) {
            base.append(StringUtils.lowerCase(protocal));
            base.append("://");
        }
        String host = u.getHost();
        if (StringUtils.isNotEmpty(StringUtils.lowerCase(host))) {
            base.append(host);
        }
        int port = u.getPort();
        if (port > 0) {
            base.append(":");
            base.append(port);
        }
        return base.toString();
    }
    
    public static String getBaseDomain2(String url) {
        String baseDomain = StringUtils.EMPTY;
        baseDomain = StringUtils.lowerCase(url);
        Matcher m = Pattern.compile("^(http|https)://[^/]+").matcher(baseDomain);
        while (m.find()) {
            baseDomain = m.group();
        }
        return baseDomain;
    }
}
