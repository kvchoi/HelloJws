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
package extend.logback.access.sift;

import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.sift.AbstractAppenderFactoryUsingJoran;
import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;
import extend.logback.access.spi.IAccessEvent;

public class AppenderFactoryUsingJoran extends AbstractAppenderFactoryUsingJoran<IAccessEvent> {

  AppenderFactoryUsingJoran(List<SaxEvent> eventList, String key, Map<String, String> parentPropertyMap) {
    super(eventList, key, parentPropertyMap);
  }

  @Override
  public SiftingJoranConfiguratorBase<IAccessEvent> getSiftingJoranConfigurator(
      String keyValue) {
    return new SiftingJoranConfigurator(key, keyValue, parentPropertyMap);
  }

}
