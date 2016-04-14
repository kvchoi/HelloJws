package extend.logback.statistics.spi;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

public class DummyStatisticsEvent extends StatisticsEvent {

    public static final DummyStatisticsEvent INSTANCE = new DummyStatisticsEvent("default");

    public DummyStatisticsEvent() {
        this(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    public DummyStatisticsEvent(String dummyStr) {
        super("dummy", "dummy event : {}", new Object[] { dummyStr });
    }

}
