package extend.logback.logging.logback;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import extend.logback.logging.AbstractLoggingConfiguration;
import extend.logback.logging.LoggerContext;
import extend.logback.logging.config.ConsoleLoggingConfiguration;
import extend.logback.logging.config.FileLoggingConfiguration;
import extend.logback.logging.config.FilteredLoggingConfiguration;
import extend.logback.logging.config.LoggingConfigurationFactory;
import extend.logback.logging.spi.IStatisticsEvent;

/**
 * Borrowed heavily from com.yammer.dropwizard.logging.LogbackFactory.
 */
public class LogbackFactory {

    private LogbackFactory() { /* singleton */
    }

    // TODO: duplication with below method
    public static ConsoleAppender<IStatisticsEvent> buildConsoleAppender(
            ConsoleLoggingConfiguration config, LoggerContext context) {
        final StatisticsLogFormatter formatter = new StatisticsLogFormatter(context,
                config.getTimeZone());
        formatter.setPattern(config.getLogFormat().get());
        formatter.start();

        final ConsoleAppender<IStatisticsEvent> appender = new ConsoleAppender<IStatisticsEvent>();
        appender.setContext(context);
        appender.setLayout(formatter);
        appender.start();

        return appender;

    }

    public static FileAppender<IStatisticsEvent> buildFileAppender(FileLoggingConfiguration file,
            LoggerContext context) {
        final StatisticsLogFormatter formatter = new StatisticsLogFormatter(context,
                file.getTimeZone());
        formatter.setPattern(file.getLogFormat().get());
        formatter.start();
        
        StatisticsLogEncoder encoder = new StatisticsLogEncoder();
        encoder.setCharset(Charset.defaultCharset());

        final FileAppender<IStatisticsEvent> appender = file.isArchive() ? new RollingFileAppender<IStatisticsEvent>()
                : new FileAppender<IStatisticsEvent>();

        appender.setAppend(true);
        appender.setContext(context);
        appender.setLayout(formatter);
        appender.setEncoder(encoder);
        appender.setFile(file.getCurrentLogFilename());
        appender.setPrudent(false);

        if (file.isArchive()) {

            final DefaultTimeBasedFileNamingAndTriggeringPolicy<IStatisticsEvent> triggeringPolicy = new DefaultTimeBasedFileNamingAndTriggeringPolicy<IStatisticsEvent>();
            triggeringPolicy.setContext(context);

            final TimeBasedRollingPolicy<IStatisticsEvent> rollingPolicy = new TimeBasedRollingPolicy<IStatisticsEvent>();
            rollingPolicy.setContext(context);
            rollingPolicy.setFileNamePattern(file.getArchivedLogFilenamePattern());
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);
            triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
            rollingPolicy.setMaxHistory(file.getArchivedFileCount());

            ((RollingFileAppender<IStatisticsEvent>) appender).setRollingPolicy(rollingPolicy);
            ((RollingFileAppender<IStatisticsEvent>) appender)
                    .setTriggeringPolicy(triggeringPolicy);

            rollingPolicy.setParent(appender);
            rollingPolicy.start();
        }

        appender.stop();
        appender.start();

        return appender;
    }

    public static OutputStreamAppender<IStatisticsEvent> buildFilteredAppender(
            final FilteredLoggingConfiguration filtered, LoggerContext context) {
        AbstractLoggingConfiguration appenderConfig = filtered.getAppender();
        OutputStreamAppender<IStatisticsEvent> appender = buildAppender(appenderConfig, context);
        return appender;
    }

    public static Set<OutputStreamAppender<IStatisticsEvent>> buildAppenders(
            LoggingConfigurationFactory configuration, LoggerContext context) {
        final Set<OutputStreamAppender<IStatisticsEvent>> appenders = new HashSet<OutputStreamAppender<IStatisticsEvent>>();
        List<AbstractLoggingConfiguration> appenderConfigs = configuration
                .getAppenderConfigurations();
        for (AbstractLoggingConfiguration appenderConfig : appenderConfigs) {
            appenders.add(buildAppender(appenderConfig, context));
        }
        return appenders;
    }

    private static OutputStreamAppender<IStatisticsEvent> buildAppender(
            AbstractLoggingConfiguration appenderConfig, LoggerContext context) {
        if (appenderConfig instanceof ConsoleLoggingConfiguration) {
            return buildConsoleAppender((ConsoleLoggingConfiguration) appenderConfig, context);
        } else if (appenderConfig instanceof FileLoggingConfiguration) {
            return buildFileAppender((FileLoggingConfiguration) appenderConfig, context);
        } else if (appenderConfig instanceof FilteredLoggingConfiguration) {
            return buildFilteredAppender((FilteredLoggingConfiguration) appenderConfig, context);
        } else {
            throw new IllegalArgumentException("Unrecognised appender config type: "
                    + appenderConfig.getClass());
        }
    }

}
