package extend.logback.logging.logback;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import extend.logback.logging.spi.IStatisticsEvent;

public class StatisticsLogLayout extends LayoutBase<IStatisticsEvent> {
    @Override
    public String doLayout(IStatisticsEvent event) {
        return event.getFormattedMessage() + CoreConstants.LINE_SEPARATOR;
    }
}
