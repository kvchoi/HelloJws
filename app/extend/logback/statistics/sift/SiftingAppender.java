/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package extend.logback.statistics.sift;

import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.sift.Discriminator;
import ch.qos.logback.core.sift.SiftingAppenderBase;
import extend.logback.statistics.spi.IStatisticsEvent;

/**
 * This appender can contains other appenders which it can build dynamically
 * depending on MDC values. The built appender is specified as part of a
 * configuration file.
 * 
 * <p>See the logback manual for further details.
 * 
 * 
 * @author Ceki Gulcu
 */
public class SiftingAppender extends SiftingAppenderBase<IStatisticsEvent> {

  @Override
  public void start() {
    super.start();
  }

  @Override
  protected long getTimestamp(IStatisticsEvent event) {
    return event.getTimeStamp();
  }

  @Override
  protected boolean eventMarksEndOfLife(IStatisticsEvent event) {
    return false;
  }

  @Override
  @DefaultClass(StatisticsEventDiscriminator.class)
  public void setDiscriminator(Discriminator<IStatisticsEvent> discriminator) {
    super.setDiscriminator(discriminator);
  }
}
