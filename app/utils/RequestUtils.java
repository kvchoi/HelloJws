package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import jws.Logger;
import jws.mvc.Http.Header;
import jws.mvc.Http.Request;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;

public class RequestUtils {
    /**
     * 取得header集
     * 
     * @return
     */
    public static Map<String, String> getHeaders() {
        Request request = Request.current();
        if (request == null) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, Header> headers = request.headers;
        if (MapUtils.isEmpty(headers)) {
            return MapUtils.EMPTY_MAP;
        }
        Map<String, String> headerMap = Maps.newHashMap();
        Set<String> headerKeySet = headers.keySet();
        for (String headerKey : headerKeySet) {
            Header header = headers.get(headerKey);
            headerMap.put(headerKey, header != null ? header.value() : StringUtils.EMPTY);
        }
        return headerMap;
    }

    /**
     * 从输入流中获取字节数组
     * 
     * @param is
     * @return
     */
    public static byte[] getBytes(InputStream is) {
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            Logger.error(e, "error in InputStream to byte[]");
            return new byte[0];
        }
    }
}
