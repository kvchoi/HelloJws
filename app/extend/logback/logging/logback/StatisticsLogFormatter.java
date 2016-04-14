package extend.logback.logging.logback;

import java.util.TimeZone;

import extend.logback.logging.LoggerContext;

/**
 * Borrowed heavily from com.yammer.dropwizard.logging.LogFormatter.
 */
public class StatisticsLogFormatter extends StatisticsLogPatternLayout {

    public StatisticsLogFormatter(LoggerContext context, TimeZone timeZone) {
        super();
        setOutputPatternAsHeader(false);
        setPattern("%-5p [%d{ISO8601," + timeZone.getID() + "}] %c: %m%n");
        setContext(context);
    }
}
