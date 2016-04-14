package controllers;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.Map;

import jws.cache.Cache;
import jws.templates.FastTags;
import jws.templates.JavaExtensions;
import jws.templates.GroovyTemplate.ExecutableTemplate;

/**
 * 扩展标签
 * 
 * @author cairf@ucweb.com
 * @createDate 2015年3月3日
 *
 */
public class AppFastTags extends FastTags {

    /**
     * #{select items:appList,as:"app",id:"id",name:"name",class:"class",selectedIndex:"index",selected:"match" }<br>
     * #{option app.id}${app.name}#{/option} <br>
     * #{/select}
     * 
     * @param args
     * @param body
     * @param out
     * @param template
     * @param fromLine
     */
    public static void _select(Map<?, ?> args, Closure body, PrintWriter out,
            ExecutableTemplate template, int fromLine) {
        String key = args.get("arg").toString();
        String duration = null;
        if (args.containsKey("for")) {
            duration = args.get("for").toString();
        }
        Object cached = Cache.get(key);
        if (cached != null) {
            out.print(cached);
            return;
        }
        String result = JavaExtensions.toString(body);
        Cache.set(key, result, duration);
        out.print(result);
    }
}
