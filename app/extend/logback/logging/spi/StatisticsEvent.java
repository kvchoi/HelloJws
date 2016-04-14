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
package extend.logback.logging.spi;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.helpers.MessageFormatter;

// Contributors:  Joern Huxhorn (see also bug #110)

/**
 * The Access module's internal representation of logging events. When the logging component instance is called in the container to log then
 * a <code>StatisticsEvent</code> instance is created. This instance is passed around to the different logback components.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 */
public class StatisticsEvent implements Serializable, IStatisticsEvent {

    private static final long serialVersionUID = 866718993618836343L;

    private String action;

    private String message;

    private Object[] argumentArray;

    transient String formattedMessage;

    private long timestamp;

    public StatisticsEvent(String action, String message) {
        this(action, message, null);
    }

    public StatisticsEvent(String action, String message, Object[] argumentArray) {
        this.action = action;
        this.message = message;
        this.argumentArray = argumentArray;
    }

    @Override
    public String getAction() {
        return this.action;
    }

    @Override
    public String getMessage() {
        return ArrayUtils.isEmpty(argumentArray) ? this.message : getFormattedMessage();
    }

    @Override
    public String getFormattedMessage() {
        if (formattedMessage != null) {
            return this.formattedMessage;
        } else {
            formattedMessage = MessageFormatter.arrayFormat(message, argumentArray).getMessage();
            return this.formattedMessage;
        }
    }

    /**
     * This method should be called prior to serializing an event. It should also be called when using asynchronous or deferred logging.
     * <p/>
     * <p/>
     * Note that due to performance concerns, this method does NOT extract caller data. It is the responsibility of the caller to extract
     * caller information.
     */
    @Override
    public void prepareForDeferredProcessing() {
        this.getFormattedMessage();
    }

    @Override
    public long getTimeStamp() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(this.action).append("] ");
        sb.append(getFormattedMessage());
        return sb.toString();
    }

}
