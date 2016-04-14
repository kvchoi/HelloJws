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
package extend.logback.statistics.pattern;

import extend.logback.statistics.spi.IStatisticsEvent;

/**
 * Return the event's formatted message.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MessageConverter extends StatisticsConverter {

  public String convert(IStatisticsEvent event) {
    return event.getFormattedMessage();
  }

}
