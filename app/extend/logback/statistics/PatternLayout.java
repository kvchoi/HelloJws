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
package extend.logback.statistics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.parser.Parser;
import extend.logback.statistics.pattern.ActionConverter;
import extend.logback.statistics.pattern.DateConverter;
import extend.logback.statistics.pattern.EnsureLineSeparation;
import extend.logback.statistics.pattern.LineSeparatorConverter;
import extend.logback.statistics.pattern.MessageConverter;
import extend.logback.statistics.spi.DummyStatisticsEvent;
import extend.logback.statistics.spi.IStatisticsEvent;

/**
 * <p>
 * This class is a module-specific implementation of {@link ch.qos.logback.StatisticsLogPatternLayout} to allow http-specific patterns to be used. The
 * <code>ch.qos.logback.PatternLayout</code> provides a way to format the logging output that is just as easy and flexible as the usual
 * <code>StatisticsLogPatternLayout</code>.
 * </p>
 * <p/>
 * For more information about this layout, please refer to the online manual at
 * http://logback.qos.ch/manual/layouts.html#AccessPatternLayout
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 */
public class PatternLayout extends PatternLayoutBase<IStatisticsEvent> {

    public static final Map<String, String> defaultConverterMap = new HashMap<String, String>();
    public static final String HEADER_PREFIX = "#logback.access pattern: ";

    public static final String CLF_PATTERN = "%h %l %u [%t] \"%r\" %s %b";
    public static final String CLF_PATTERN_NAME = "common";
    public static final String CLF_PATTERN_NAME_2 = "clf";
    public static final String COMBINED_PATTERN = "%h %l [%t] %s %b \"%i{Referer}\" \"%i{User-Agent}\"";
    public static final String COMBINED_PATTERN_NAME = "combined";

    static {
        defaultConverterMap.putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);

        defaultConverterMap.put("a", ActionConverter.class.getName());
        defaultConverterMap.put("action", ActionConverter.class.getName());

        defaultConverterMap.put("d", DateConverter.class.getName());
        defaultConverterMap.put("date", DateConverter.class.getName());

        defaultConverterMap.put("m", MessageConverter.class.getName());
        defaultConverterMap.put("msg", MessageConverter.class.getName());
        defaultConverterMap.put("message", MessageConverter.class.getName());

        defaultConverterMap.put("n", LineSeparatorConverter.class.getName());

    }

    public PatternLayout() {
        // set a default value for pattern
        setPattern(CLF_PATTERN);
        // by default postCompileProcessor the is an EnsureLineSeparation instance
        this.postCompileProcessor = new EnsureLineSeparation();
    }

    /**
     * Returns the default converter map for this instance.
     */
    @Override
    public Map<String, String> getDefaultConverterMap() {
        return defaultConverterMap;
    }

    @Override
    public String doLayout(IStatisticsEvent event) {
        if (!isStarted()) {
            return null;
        }
        // TODO dummy event return "" to write
        if (event instanceof DummyStatisticsEvent) {
            return StringUtils.EMPTY;
        }
        return writeLoopOnConverters(event);
    }

    @Override
    public void start() {
        if (getPattern().equalsIgnoreCase(CLF_PATTERN_NAME)
                || getPattern().equalsIgnoreCase(CLF_PATTERN_NAME_2)) {
            setPattern(CLF_PATTERN);
        } else if (getPattern().equalsIgnoreCase(COMBINED_PATTERN_NAME)) {
            setPattern(COMBINED_PATTERN);
        }
        super.start();
    }

    @Override
    protected String getPresentationHeaderPrefix() {
        return HEADER_PREFIX;
    }
}
