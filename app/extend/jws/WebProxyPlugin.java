package extend.jws;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jws.JwsPlugin;
import jws.Logger;
import jws.mvc.Http.Header;
import jws.mvc.Http.Request;
import jws.mvc.Http.Response;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import utils.RequestUtils;

import com.alibaba.druid.util.StringUtils;

/**
 * 
 * web代理，用于HTTP（支持GET、POST等）请求转发<br>
 * 示例：http://127.0.0.1:9020/web/proxy?http%3A%2F%2Fwww.csdn.net
 * 
 *
 * @author "cairf@ucweb.com"
 * @date 2016年4月8日
 */
public class WebProxyPlugin extends JwsPlugin {

    @Override
    public boolean rawInvocation(Request request, Response response) throws Exception {
        // return super.rawInvocation(request, response);
        if (!StringUtils.equalsIgnoreCase(request.path, "/web/proxy")) {
            return false;
        }
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        String encoding = StringUtils.isEmpty(request.encoding) ? CharEncoding.UTF_8
                : request.encoding;
        String url = URLDecoder.decode(request.querystring, encoding);
        String method = request.method;
        byte[] body = RequestUtils.getBytes(request.body);
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(1000 * 5);// 5秒
            conn.setReadTimeout(1000 * 5);// 5秒
            conn.setDefaultUseCaches(false);
            conn.setFollowRedirects(true);// 设置所有的http连接是否自动处理重定向
            // conn.setInstanceFollowRedirects(true);// 设置本次连接是否自动处理重定向
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // conn.connect();

            Set<Map.Entry<String, Header>> headerAttribute = request.headers.entrySet();
            if (CollectionUtils.isNotEmpty(headerAttribute)) {
                // 设置HttpRequest请求头的属性
                for (Map.Entry<String, Header> entry : headerAttribute) {
                    if (StringUtils.isEmpty(entry.getKey())) {
                        continue;
                    }
                    // 不支持压缩
                    if (StringUtils.equalsIgnoreCase(HttpHeaders.Names.ACCEPT_ENCODING,
                            entry.getKey())) {
                        continue;
                    }
                    for (String value : entry.getValue().values) {
                        conn.addRequestProperty(entry.getKey(), value);
                    }
                }
            }
            // 发
            if (ArrayUtils.isNotEmpty(body)) {
                os = conn.getOutputStream();
                IOUtils.write(body, os);
            }

            // 收
            Integer responseCode = conn.getResponseCode();
            response.status = responseCode;
            response.setContentTypeIfNotSet(conn.getContentType());
            is = conn.getInputStream();
            response.out.write(RequestUtils.getBytes(is));
            Map<String, List<String>> headFields = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headFields.entrySet()) {
                if (StringUtils.isEmpty(entry.getKey())) {
                    continue;
                }
                for (String value : entry.getValue()) {
                    if (response.headers.containsKey(entry.getKey())) {
                        Header header = response.headers.get(entry.getKey());
                        header.values.add(value);
                    } else {
                        response.setHeader(entry.getKey(), value);
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e, "web proxy direct to url = %s", url);
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
            if (conn != null)
                conn.disconnect();
        }
        return true;
    }

}
