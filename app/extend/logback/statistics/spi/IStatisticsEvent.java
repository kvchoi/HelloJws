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
package extend.logback.statistics.spi;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import jws.mvc.Http;
import ch.qos.logback.core.spi.DeferredProcessingAware;

// Contributors:  Joern Huxhorn (see also bug #110)

/**
 * The Access module's internal representation of logging events. When the logging component instance is called in the container to log then
 * a <code>StatisticsEvent</code> instance is created. This instance is passed around to the different logback components.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 * @author J&ouml;rn Huxhorn
 */
public interface IStatisticsEvent extends DeferredProcessingAware {

    String NA = "-";
    int SENTINEL = -1;

    String getAction();

    String getMessage();

    String getFormattedMessage();

    void prepareForDeferredProcessing();

    long getTimeStamp();
}
