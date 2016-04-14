package utils;

import org.apache.commons.lang.StringUtils;

import jws.cache.Cache;
import mpsdk4j.session.SessionCache;

public class MCSessionCache implements SessionCache {

    @Override
    public Object get(String prefix, String key) {
        return Cache.get(StringUtils.defaultIfEmpty(prefix, StringUtils.EMPTY) + key);
    }

    @Override
    public <T> T get(String prefix, String key, Class<T> clazz) {
        return Cache.get(StringUtils.defaultIfEmpty(prefix, StringUtils.EMPTY) + key, clazz);
    }

    @Override
    public void set(String prefix, String key, Object value) {
        Cache.set(StringUtils.defaultIfEmpty(prefix, StringUtils.EMPTY) + key, value);
    }

    @Override
    public void remove(String prefix, String key) {
        Cache.delete(StringUtils.defaultIfEmpty(prefix, StringUtils.EMPTY) + key);
    }

}
