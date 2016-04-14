package extend.logback.logging;

import java.util.Map;
import java.util.TimeZone;

import com.google.common.base.Optional;

public abstract class AbstractLoggingConfiguration {

    private TimeZone timeZone = TimeZone.getDefault();
    private String logFormat;

    public AbstractLoggingConfiguration(Map<String, ?> config) {
        if (config.get("timeZone") != null) {
            setTimeZone(TimeZone.getTimeZone(config.get("timeZone").toString()));
        } else {
            setTimeZone(timeZone.getDefault());
        }
        if (config.get("logFormat") != null) {
            setLogFormat(config.get("logFormat").toString());
        } else {
            setLogFormat("%msg");
        }

        // handle deprecated logging format
        if (config.containsKey("rootLevel")) {
            throw new IllegalArgumentException(
                    "As of lightweight-deploy versions > 0.9.0 rootLevel is now set one level up under 'logging'.");
        }
        if (config.containsKey("loggers")) {
            throw new IllegalArgumentException(
                    "As of lightweight-deploy versions > 0.9.0 loggers is now set one level up under 'logging'.");
        }

    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public Optional<String> getLogFormat() {
        return Optional.fromNullable(logFormat);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

}
