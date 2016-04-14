/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package extend.logback.access.spi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import jws.mvc.Http;
import jws.mvc.Http.Cookie;
import jws.mvc.Http.Header;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.common.collect.Maps;

import extend.logback.access.AccessConstants;

// Contributors:  Joern Huxhorn (see also bug #110)

/**
 * The Access module's internal representation of logging events. When the logging component instance is called in the container to log then
 * a <code>AccessEvent</code> instance is created. This instance is passed around to the different logback components.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 */
public class AccessEvent implements Serializable, IAccessEvent {

    private static final long serialVersionUID = 866718993618836343L;

    private transient final Http.Request httpRequest;
    private transient final Http.Response httpResponse;

    String requestURI;
    String requestURL;
    String remoteHost;
    String remoteUser;
    String remoteAddr;
    String protocol;
    String method;
    String serverName;
    String requestContent;
    String responseContent;
    long elapsedTime;

    Map<String, String> requestHeaderMap;
    Map<String, String[]> requestParameterMap;
    Map<String, String> responseHeaderMap;
    Map<String, Object> attributeMap;

    long contentLength = SENTINEL;
    int statusCode = SENTINEL;
    int localPort = SENTINEL;

    transient ServerAdapter serverAdapter;

    /**
     * The number of milliseconds elapsed from 1/1/1970 until logging event was created.
     */
    private long timeStamp = 0;

    public AccessEvent(Http.Request httpRequest, Http.Response httpResponse, ServerAdapter adapter) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.timeStamp = System.currentTimeMillis();
        this.serverAdapter = adapter;
        this.elapsedTime = calculateElapsedTime();
    }

    /**
     * Returns the underlying Http.Request. After serialization the returned value will be null.
     *
     * @return
     */
    @Override
    public Http.Request getRequest() {
        return httpRequest;
    }

    /**
     * Returns the underlying Http.Response. After serialization the returned value will be null.
     *
     * @return
     */
    @Override
    public Http.Response getResponse() {
        return httpResponse;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        if (this.timeStamp != 0) {
            throw new IllegalStateException("timeStamp has been already set for this event.");
        } else {
            this.timeStamp = timeStamp;
        }
    }

    @Override
    public String getProtocol() {
        if (protocol == null) {
            if (httpRequest != null) {
                protocol = httpRequest.secure ? "https://" : "http://";
            } else {
                protocol = NA;
            }
        }
        return protocol;
    }

    @Override
    public String getMethod() {
        if (method == null) {
            if (httpRequest != null) {
                method = httpRequest.method;
            } else {
                method = NA;
            }
        }
        return method;
    }

    @Override
    public String getServerName() {
        if (serverName == null) {
            if (httpRequest != null) {
                serverName = httpRequest.domain;
            } else {
                serverName = NA;
            }
        }
        return serverName;
    }

    @Override
    public String getRemoteAddr() {
        if (remoteAddr == null) {
            if (httpRequest != null) {
                remoteAddr = httpRequest.remoteAddress;
            } else {
                remoteAddr = NA;
            }
        }
        return remoteAddr;
    }

    @Override
    public String getRequestHeader(String key) {
        String result = null;
        key = key.toLowerCase();
        if (requestHeaderMap == null) {
            if (httpRequest != null) {
                buildRequestHeaderMap();
                result = requestHeaderMap.get(key);
            }
        } else {
            result = requestHeaderMap.get(key);
        }

        if (result != null) {
            return result;
        } else {
            return NA;
        }
    }

    @Override
    public Enumeration getRequestHeaderNames() {
        // post-serialization
        if (httpRequest == null || httpRequest.headers == null) {
            Vector<String> list = new Vector<String>(getRequestHeaderMap().keySet());
            return list.elements();
        }
        Vector<String> list = new Vector<String>(httpRequest.headers.keySet());
        return list.elements();
    }

    @Override
    public Map<String, String> getRequestHeaderMap() {
        if (requestHeaderMap == null) {
            buildRequestHeaderMap();
        }
        return requestHeaderMap;
    }

    public void buildRequestHeaderMap() {
        if (httpRequest == null || httpRequest.headers == null) {
            requestHeaderMap = MapUtils.EMPTY_MAP;
        } else {
            requestHeaderMap = Maps.newTreeMap();
            Set<Entry<String, Header>> entry = httpRequest.headers.entrySet();
            for (Entry<String, Header> tmp : entry) {
                requestHeaderMap.put(tmp.getKey(), tmp.getValue() != null ? tmp.getValue().value()
                        : StringUtils.EMPTY);
            }
        }
    }

    @Override
    public Map<String, String[]> getRequestParameterMap() {
        if (requestParameterMap == null) {
            requestParameterMap = httpRequest.params.all();
        }
        return requestParameterMap;
    }

    @Override
    public String getAttribute(String key) {
        Object value = null;
        if (attributeMap != null) {
            // Event was prepared for deferred processing so we have a copy of attribute map and must use that copy
            value = attributeMap.get(key);
        } else if (httpRequest != null) {
            // We have original request so take attribute from it
            value = httpRequest.args.get(key);
        }

        return value != null ? value.toString() : NA;
    }

    private void copyAttributeMap() {

        if (httpRequest == null) {
            return;
        }

        attributeMap = new HashMap<String, Object>();

        Enumeration<String> names = new Vector<String>(httpRequest.args.keySet()).elements();
        while (names.hasMoreElements()) {
            String name = names.nextElement();

            Object value = httpRequest.args.get(name);
            if (shouldCopyAttribute(name, value)) {
                attributeMap.put(name, value);
            }
        }
    }

    private boolean shouldCopyAttribute(String name, Object value) {
        if (value == null) {
            // No reasons to copy nulls - Map.get() will return null for missing keys and the list of attribute
            // names is not available through IAccessEvent
            return false;
        } else {
            // Only copy what is serializable
            return value instanceof Serializable;
        }
    }

    @Override
    public String[] getRequestParameter(String key) {
        if (httpRequest != null) {
            String[] value = httpRequest.params.all().get(key);
            if (value == null) {
                return new String[] { NA };
            } else {
                return value;
            }
        } else {
            return new String[] { NA };
        }
    }

    @Override
    public String getCookie(String key) {

        if (httpRequest != null && httpRequest.cookies != null) {
            Cookie[] cookieArray = httpRequest.cookies.values().toArray(new Cookie[0]);
            if (cookieArray == null) {
                return NA;
            }

            for (Cookie cookie : cookieArray) {
                if (key.equals(cookie.name)) {
                    return cookie.value;
                }
            }
        }
        return NA;
    }

    @Override
    public long getContentLength() {
        if (contentLength == SENTINEL) {
            if (httpResponse != null) {
                contentLength = serverAdapter.getContentLength();
                return contentLength;
            }
        }
        return contentLength;
    }

    public int getStatusCode() {
        if (statusCode == SENTINEL) {
            if (httpResponse != null) {
                statusCode = serverAdapter.getStatusCode();
            }
        }
        return statusCode;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    private long calculateElapsedTime() {
        if (serverAdapter.getRequestTimestamp() < 0) {
            return -1;
        }
        return getTimeStamp() - serverAdapter.getRequestTimestamp();
    }

    public String getRequestContent() {
        if (requestContent != null) {
            return requestContent;
        }

        if (Util.isFormUrlEncoded(httpRequest)) {
            try {
                requestContent = IOUtils.toString(httpRequest.body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            requestContent = httpRequest.querystring;
        }

        return requestContent;
    }

    public String getResponseContent() {
        if (responseContent != null) {
            return responseContent;
        }

        if (Util.isImageResponse(httpResponse)) {
            responseContent = "[IMAGE CONTENTS SUPPRESSED]";
        } else {
            try {
                responseContent = IOUtils.toString(new ByteArrayInputStream(httpResponse.out
                        .toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseContent;
    }

    public int getLocalPort() {
        if (localPort == SENTINEL) {
            if (httpRequest != null) {
                localPort = httpRequest.port;
            }

        }
        return localPort;
    }

    public ServerAdapter getServerAdapter() {
        return serverAdapter;
    }

    public String getResponseHeader(String key) {
        buildResponseHeaderMap();
        return responseHeaderMap.get(key);
    }

    void buildResponseHeaderMap() {
        if (responseHeaderMap == null) {
            responseHeaderMap = serverAdapter.buildResponseHeaderMap();
        }
    }

    public Map<String, String> getResponseHeaderMap() {
        buildResponseHeaderMap();
        return responseHeaderMap;
    }

    public List<String> getResponseHeaderNameList() {
        buildResponseHeaderMap();
        return new ArrayList<String>(responseHeaderMap.keySet());
    }

    public void prepareForDeferredProcessing() {
        getRequestHeaderMap();
        getRequestParameterMap();
        getResponseHeaderMap();
        getLocalPort();
        getMethod();
        getProtocol();
        getRemoteAddr();
        getServerName();
        getTimeStamp();
        getElapsedTime();

        getStatusCode();
        getContentLength();
        getRequestContent();
        getResponseContent();

        copyAttributeMap();
    }

    @Override
    public String getServerHost() {
        if (httpRequest != null) {
            return httpRequest.host;
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public String getRequestQueryString() {
        if (httpRequest != null) {
            return httpRequest.querystring;
        } else {
            return StringUtils.EMPTY;
        }
    }
}
