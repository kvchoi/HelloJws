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
package extend.logback.access;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.parser.Parser;
import extend.logback.access.pattern.ContentLengthConverter;
import extend.logback.access.pattern.DateConverter;
import extend.logback.access.pattern.EnsureLineSeparation;
import extend.logback.access.pattern.FullRequestConverter;
import extend.logback.access.pattern.FullResponseConverter;
import extend.logback.access.pattern.LineSeparatorConverter;
import extend.logback.access.pattern.LocalIPAddressConverter;
import extend.logback.access.pattern.LocalPortConverter;
import extend.logback.access.pattern.NAConverter;
import extend.logback.access.pattern.RemoteIPAddressConverter;
import extend.logback.access.pattern.RequestAttributeConverter;
import extend.logback.access.pattern.RequestContentConverter;
import extend.logback.access.pattern.RequestCookieConverter;
import extend.logback.access.pattern.RequestHeaderConverter;
import extend.logback.access.pattern.RequestHostConverter;
import extend.logback.access.pattern.RequestMethodConverter;
import extend.logback.access.pattern.RequestParameterConverter;
import extend.logback.access.pattern.RequestProtocolConverter;
import extend.logback.access.pattern.ResponseContentConverter;
import extend.logback.access.pattern.ResponseHeaderConverter;
import extend.logback.access.pattern.ServerNameConverter;
import extend.logback.access.pattern.StatusCodeConverter;
import extend.logback.access.spi.IAccessEvent;

/**
 * <p>
 * This class is a module-specific implementation of
 * {@link ch.qos.logback.StatisticsLogPatternLayout} to allow http-specific patterns
 * to be used. The <code>ch.qos.logback.PatternLayout</code> provides a
 * way to format the logging output that is just as easy and flexible as the
 * usual <code>StatisticsLogPatternLayout</code>.
 * </p>
 * <p/>
 * For more information about this layout, please refer to the online manual at
 * http://logback.qos.ch/manual/layouts.html#AccessPatternLayout
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author S&eacute;bastien Pennec
 */
public class PatternLayout extends PatternLayoutBase<IAccessEvent> {

  public static final Map<String, String> defaultConverterMap = new HashMap<String, String>();
  public static final String HEADER_PREFIX = "#logback.access pattern: ";

  public static final String CLF_PATTERN = "%h %l %u [%t] \"%r\" %s %b";
  public static final String CLF_PATTERN_NAME = "common";
  public static final String CLF_PATTERN_NAME_2 = "clf";
  public static final String COMBINED_PATTERN = "%h %l [%t] %s %b \"%i{Referer}\" \"%i{User-Agent}\"";
  public static final String COMBINED_PATTERN_NAME = "combined";

  static {
    defaultConverterMap.putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);

    defaultConverterMap.put("a", RemoteIPAddressConverter.class.getName());
    defaultConverterMap.put("remoteIP", RemoteIPAddressConverter.class
        .getName());

    defaultConverterMap.put("A", LocalIPAddressConverter.class.getName());
    defaultConverterMap.put("localIP", LocalIPAddressConverter.class.getName());

    defaultConverterMap.put("b", ContentLengthConverter.class.getName());
    defaultConverterMap.put("B", ContentLengthConverter.class.getName());
    defaultConverterMap
        .put("bytesSent", ContentLengthConverter.class.getName());

    defaultConverterMap.put("H", RequestProtocolConverter.class.getName());
    defaultConverterMap.put("protocol", RequestProtocolConverter.class
        .getName());
    
    defaultConverterMap.put("h", RequestHostConverter.class.getName());
    defaultConverterMap.put("requestHost", RequestHostConverter.class
            .getName());

    defaultConverterMap.put("i", RequestHeaderConverter.class.getName());
    defaultConverterMap.put("header", RequestHeaderConverter.class.getName());

    defaultConverterMap.put("l", NAConverter.class.getName());

    defaultConverterMap.put("m", RequestMethodConverter.class.getName());
    defaultConverterMap.put("requestMethod", RequestMethodConverter.class
        .getName());

    defaultConverterMap.put("s", StatusCodeConverter.class.getName());
    defaultConverterMap.put("statusCode", StatusCodeConverter.class.getName());

    defaultConverterMap.put("t", DateConverter.class.getName());
    defaultConverterMap.put("date", DateConverter.class.getName());

    defaultConverterMap.put("v", ServerNameConverter.class.getName());
    defaultConverterMap.put("server", ServerNameConverter.class.getName());

    defaultConverterMap.put("localPort", LocalPortConverter.class.getName());

    defaultConverterMap.put("requestAttribute", RequestAttributeConverter.class
        .getName());
    defaultConverterMap.put("reqAttribute", RequestAttributeConverter.class
        .getName());

    defaultConverterMap
        .put("reqCookie", RequestCookieConverter.class.getName());
    defaultConverterMap
        .put("requestCookie", RequestCookieConverter.class.getName());


    defaultConverterMap.put("responseHeader", ResponseHeaderConverter.class
        .getName());


    defaultConverterMap.put("requestParameter", RequestParameterConverter.class
        .getName());
    defaultConverterMap.put("reqParameter", RequestParameterConverter.class
        .getName());

    defaultConverterMap.put("requestContent", RequestContentConverter.class.getName());

    defaultConverterMap.put("responseContent", ResponseContentConverter.class.getName());

    defaultConverterMap.put("fullRequest", FullRequestConverter.class.getName());
    defaultConverterMap.put("fullResponse", FullResponseConverter.class.getName());

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
  public String doLayout(IAccessEvent event) {
    if (!isStarted()) {
      return null;
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
