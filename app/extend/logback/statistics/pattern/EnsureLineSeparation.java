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

import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.PostCompileProcessor;
import extend.logback.statistics.spi.IStatisticsEvent;

public class EnsureLineSeparation implements PostCompileProcessor<IStatisticsEvent> {

  /**
   * Add a line separator converter so that access event appears on a separate
   * line.
   */
    @Override
  public void process(Converter<IStatisticsEvent> head) {
    if(head == null)
      throw new IllegalArgumentException("Empty converter chain");

    // if head != null, then tail != null as well
    Converter<IStatisticsEvent> tail = ConverterUtil.findTail(head);
    Converter<IStatisticsEvent> newLineConverter = new LineSeparatorConverter();
    if (!(tail instanceof LineSeparatorConverter)) {
        tail.setNext(newLineConverter);
    }
  }
}
