package utils;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 
 * http://docs.oracle.com/javase/7/docs/whchatApi/java/net/doc-files/net-properties.html
 *
 * @author "cairf@ucweb.com"
 * @date 2015年12月22日
 */
public class ProxyUtils {

    public static boolean useSystemProxies() {
        return StringUtils.equalsIgnoreCase(
                System.getProperty("java.net.useSystemProxies", Boolean.FALSE.toString()),
                Boolean.TRUE.toString());
    }

    public static String httpsProxyHost() {
        return System.getProperty("https.proxyHost");
    }

    public static int httpsProxyPort() {
        return NumberUtils.toInt(System.getProperty("https.proxyPort", "443"), 443);
    }

    public static String httpProxyHost() {
        return System.getProperty("http.proxyHost");
    }

    public static int httpProxyPort() {
        return NumberUtils.toInt(System.getProperty("http.proxyPort", "80"), 80);
    }

    public static String httpNonProxyHosts() {
        return System.getProperty("http.nonProxyHosts");
    }

    public static HttpHost createSystemHttpProxy() {
        String proxyHost = httpProxyHost();
        if (StringUtils.isEmpty(proxyHost)) {
            return null;
        }
        int proxyPort = httpProxyPort();
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        return proxy;
    }

    public static HttpHost createSystemHttpsProxy() {
        String proxyHost = httpsProxyHost();
        if (StringUtils.isEmpty(proxyHost)) {
            return null;
        }
        int proxyPort = httpsProxyPort();
        // 依次是代理地址，代理端口号，协议类型
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "https");
        return proxy;
    }

    public static HttpHost createCustomHttpProxy(String proxyHost, int proxyPort, boolean isHttps) {
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, isHttps ? "https" : "http");
        return proxy;
    }

    /**
     * -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8888
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        RequestConfig config = null;
        HttpHost proxy = createSystemHttpProxy();
        // HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
        if (proxy != null) {
            config = RequestConfig.custom().setProxy(proxy).build();
        } else {
            config = RequestConfig.custom().build();
        }
        // 发出请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        httpGet.setConfig(config);
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            EntityUtils.consume(response.getEntity());
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
