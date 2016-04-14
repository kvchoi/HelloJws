package extend.logback.logging.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import extend.logback.logging.AbstractLoggingConfiguration;

import java.util.*;

import static java.util.Collections.unmodifiableList;

public class LoggingConfigurationFactory {

    private List<AbstractLoggingConfiguration> appenderConfigurations = new ArrayList<AbstractLoggingConfiguration>();

    public LoggingConfigurationFactory(Map<String, ?> config) {
        if (config.containsKey("appenders")) {
            List<Map<String, ?>> appendersConfig = (List<Map<String, ?>>) config.get("appenders");
            for (Map<String, ?> appenderConfig : appendersConfig) {
                appenderConfigurations.add(createAppenderConfiguration(appenderConfig));
            }
        }

        // handle deprecated logging format
        if (config.containsKey("file")) {
            jws.Logger.warn("'file' is deprecated - please move to 'appenders' list with type 'file'");
            Map<String, ?> fileConfig = (Map<String, ?>) config.get("file");
            appenderConfigurations.add(new FileLoggingConfiguration(fileConfig));
        }
        if (config.containsKey("console")) {
            jws.Logger.warn("'console' is deprecated - please move to 'appenders' list with type 'console'");
            Map<String, ?> consoleConfig = (Map<String, ?>) config.get("console");
            appenderConfigurations.add(new ConsoleLoggingConfiguration(consoleConfig));
        }
    }

    public List<AbstractLoggingConfiguration> getAppenderConfigurations() {
        return unmodifiableList(appenderConfigurations);
    }

    public static AbstractLoggingConfiguration createAppenderConfiguration(Map<String, ?> appenderConfig) {
        String type = appenderConfig.get("type").toString();
        if (type.equals("file")) {
            return new FileLoggingConfiguration(appenderConfig);
        } else if (type.equals("console")) {
            return new ConsoleLoggingConfiguration(appenderConfig);
        } else if (type.equals("filtered")) {
            return new FilteredLoggingConfiguration(appenderConfig);
        } else {
            throw new IllegalArgumentException("Unknown appender type '" + type + "'");
        }
    }

    public TimeZone getTimeZone() {
        if (!appenderConfigurations.isEmpty()) {
            return appenderConfigurations.get(0).getTimeZone();
        }
        return TimeZone.getDefault();
    }

}
