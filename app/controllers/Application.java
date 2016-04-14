package controllers;

import java.util.Properties;

import jws.mvc.Controller;
import jws.mvc.With;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;

import extend.logback.logging.AsyncStatisticsLogger;
import extend.logback.logging.AsyncStatisticsLoggerFactory;

@With(Handler.class)
public class Application extends Controller {

    static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Application.class);
    static AsyncStatisticsLogger logger2 = AsyncStatisticsLoggerFactory.getInstance().getLogger();

    public static void index() {
        // StatisticsLogImpl.getInstance().log(request, response);
        String msg = "{} hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | ";
        // StatisticsLog.getInstance().log("com", msg, "kevin");
        // StatisticsLog.getInstance().log("com.test", msg, "kevin");
        Properties pro = new Properties();
        pro.put("log4j.logger.jws.asyncAccesslog", "info,asyncLog");
        pro.put("log4j.appender.asyncLog", "extend.log4j.AsyncAppenderHelper");
        pro.put("log4j.appender.asyncLog.appenderFromLogger", "jws.accesslog");
        jws.Logger.event("accesslog", msg, "kevin");
        // logger.entry();
        // logger.error(msg,"kevin");
        // jws.Logger.warn(msg, "kevin");
        // logger2.log(request, response);
        render();
    }

    public static void error() {
        throw new RuntimeException("a test exception");
    }
}