package extend.log4j;

import java.util.Enumeration;

import org.apache.log4j.Appender;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Logger;

/**
 * 
 * log4j 异步appender适配类
 *
 * @author "cairf@ucweb.com"
 * @date 2015年12月8日
 */
public class AsyncAppenderHelper extends AsyncAppender {

    public AsyncAppenderHelper() {
        super();
    }

    public void setAppenderFromLogger(String name) {
        Logger l = Logger.getLogger(name);

        Enumeration<Appender> e = l.getAllAppenders();

        while (e.hasMoreElements()) {
            Appender a = e.nextElement();
            this.addAppender(a);
            System.out.println("The newAppender " + a.getName() + " attach status "
                    + this.isAttached(a));
        }

    }
}
