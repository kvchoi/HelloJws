package extend.logback.statistics;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import jws.Jws;
import jws.config.MacroDefinedParser;
import ch.qos.logback.core.joran.spi.JoranException;
import extend.logback.statistics.util.ContextInitializer;

public class StatisticsLog {
    private static final StatisticsLog SINGLETON = new StatisticsLog();
    private boolean initialized = false;
    private LoggerContext defaultLoggerContext = new LoggerContext();

    static {
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY,
                "D:\\workspace6\\HelloJws\\conf\\env\\logback-statistics.xml");
        SINGLETON.init();
    }

    private StatisticsLog() {
        this.defaultLoggerContext.setName("default");
    }

    public static StatisticsLog getInstance() {
        return SINGLETON;
    }

    public void log(String logger, String msg, Object... args) {
        if (!initialized) {
            jws.Logger.warn("not initialized");
            return;
        }
        this.defaultLoggerContext.getLogger(logger).info(msg, args);
    }

    public void logDummy() {
        if (!initialized) {
            jws.Logger.warn("not initialized");
            return;
        }
        List<Logger> loggerList = this.defaultLoggerContext.getLoggerList();
        for (Logger logger : loggerList) {
            logger.dummy();
        }
    }

    private void init() {
        try {
            try {
                // TODO 注入jws配置项
                this.defaultLoggerContext.putProperty(Jws.JWS_PATH_KEY, MacroDefinedParser
                        .getInstance().getMacroValByKey(Jws.JWS_PATH_KEY));
                this.defaultLoggerContext.putProperty(Jws.APP_PATH_KEY, MacroDefinedParser
                        .getInstance().getMacroValByKey(Jws.APP_PATH_KEY));
                new ContextInitializer(this.defaultLoggerContext).autoConfig();
            } catch (JoranException je) {
                jws.Logger.error("Failed to auto configure default logger context", je);
            }
            this.initialized = true;
        } catch (Throwable t) {
            jws.Logger.error("Failed to instantiate [" + LoggerContext.class.getName() + "]", t);
        }
    }
}
