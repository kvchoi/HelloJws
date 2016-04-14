package extend.logback.logging;

import java.io.File;
import java.io.IOException;

import jws.Jws;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import extend.logback.logging.logback.LogbackFactory;
import extend.logback.logging.logback.StatisticsLogLayout;
import extend.logback.logging.spi.IStatisticsEvent;

public class AsyncStatisticsLoggerFactory {

    private final static Object lock = new Object();
    private final YamlConfiguration config;
    private static AsyncStatisticsLoggerFactory factory;

    public AsyncStatisticsLoggerFactory(YamlConfiguration config) {
        this.config = config;
    }

    public static AsyncStatisticsLoggerFactory getInstance() {
        if (factory == null) {
            synchronized (lock) {
                if (factory == null) {
                    File confFile = new File(Jws.applicationPath, "conf/env/statistics.yml");
                    try {
                        YamlConfiguration config = new YamlConfiguration(confFile);
                        factory = new AsyncStatisticsLoggerFactory(config);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return factory;
    }

    public AsyncStatisticsLogger getLogger() {

        final LoggerContext context = new LoggerContext();

        final AppenderAttachableImpl<IStatisticsEvent> appenders = new AppenderAttachableImpl<IStatisticsEvent>();

        final StatisticsLogLayout layout = new StatisticsLogLayout();
        layout.start();

        for (OutputStreamAppender<IStatisticsEvent> appender : LogbackFactory.buildAppenders(
                config.getStatisticsLogConfiguration(), context)) {
            appender.stop();// ?
            appender.setLayout(layout);
            appender.start();
            appenders.addAppender(appender);
        }

        AsyncStatisticsLogger log = new AsyncStatisticsLogger(appenders, config
                .getStatisticsLogConfiguration().getTimeZone(), config
                .getStatisticsLogConfiguration().getCookies());
        try {
            log.doStart();
        } catch (Exception e) {
            return null;
        }

        return log;

    }

}
