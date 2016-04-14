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

import ch.qos.logback.core.sift.AbstractDiscriminator;
import extend.logback.statistics.spi.IStatisticsEvent;

/**
 * 
 * StatisticsEventDiscriminator's job is to return the value of a designated field in an {@link IStatisticsEvent} instance.
 * 
 * <p>
 * The field is specified via the {@link FieldName} property.
 * 
 * @author Ceki G&uuml;lc&uuml;
 * 
 */
public class StatisticsEventDiscriminator extends AbstractDiscriminator<IStatisticsEvent> {

    /**
     * At present time the followed fields can be designated: COOKIE, REQUEST_ATTRIBUTE, SESSION_ATTRIBUTE, REMOTE_ADDRESS,
     * LOCAL_PORT,REQUEST_URI
     * 
     * <p>
     * The first three fields require an additional key. For the SESSION_ATTRIBUTE field, the additional key named "id" has special meaning
     * as it is mapped to the session id of the current http request.
     */
    public enum FieldName {
        COOKIE, REQUEST_ATTRIBUTE, SESSION_ATTRIBUTE, REMOTE_ADDRESS, LOCAL_PORT, REQUEST_URI
    }

    String defaultValue;
    String key;
    FieldName fieldName;
    String additionalKey;

    @Override
    public String getDiscriminatingValue(IStatisticsEvent acccessEvent) {
        // TODO 唯一识别符
        return acccessEvent.getAction();
    }

    @Override
    public void start() {

        int errorCount = 0;

        if (defaultValue == null) {
            addError("\"DefaultValue\" property must be set.");
        }
        if (fieldName == null) {
            addError("\"FieldName\" property must be set.");
            errorCount++;
        }

        switch (fieldName) {
        case SESSION_ATTRIBUTE:
        case REQUEST_ATTRIBUTE:
        case COOKIE:
            if (additionalKey == null) {
                addError("\"OptionalKey\" property is mandatory for field name "
                        + fieldName.toString());
                errorCount++;
            }
        }

        if (errorCount == 0) {
            started = true;
        }
    }

    public void setFieldName(FieldName fieldName) {
        this.fieldName = fieldName;
    }

    public FieldName getFieldName() {
        return fieldName;
    }

    public String getAdditionalKey() {
        return additionalKey;
    }

    public void setAdditionalKey(String additionalKey) {
        this.additionalKey = additionalKey;
    }

    /**
     * @see #setDefaultValue(String)
     * @return
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * The default value returned by this discriminator in case it cannot compute the discriminating value from the access event.
     * 
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
