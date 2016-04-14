package extend.logback.statistics.rolling;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import extend.logback.statistics.spi.IStatisticsEvent;

@NoAutoStart
public class StatisticsTimeBasedFileNamingAndTriggeringPolicy extends
        DefaultTimeBasedFileNamingAndTriggeringPolicy<IStatisticsEvent> {

}
