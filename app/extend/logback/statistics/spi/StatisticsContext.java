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

import java.util.Iterator;
import java.util.List;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;

/**
 * A minimal context implementation used by certain logback-access components,
 * mainly SocketServer.
 * 
 * @author S&eacute;bastien Pennec
 */
public class StatisticsContext extends ContextBase implements
    AppenderAttachable<IStatisticsEvent>, FilterAttachable<IStatisticsEvent> {

  AppenderAttachableImpl<IStatisticsEvent> aai = new AppenderAttachableImpl<IStatisticsEvent>();
  FilterAttachableImpl<IStatisticsEvent> fai = new FilterAttachableImpl<IStatisticsEvent>();

  public void callAppenders(IStatisticsEvent event) {
    aai.appendLoopOnAppenders(event);
  }

  @Override
  public void addAppender(Appender<IStatisticsEvent> newAppender) {
    aai.addAppender(newAppender);
  }

  @Override
  public void detachAndStopAllAppenders() {
    aai.detachAndStopAllAppenders();
  }

  @Override
  public boolean detachAppender(Appender<IStatisticsEvent> appender) {
    return aai.detachAppender(appender);
  }

  @Override
  public boolean detachAppender(String name) {
    return aai.detachAppender(name);
  }

  @Override
  public Appender<IStatisticsEvent> getAppender(String name) {
    return aai.getAppender(name);
  }

  @Override
  public boolean isAttached(Appender<IStatisticsEvent> appender) {
    return aai.isAttached(appender);
  }

  @Override
  public Iterator<Appender<IStatisticsEvent>> iteratorForAppenders() {
    return aai.iteratorForAppenders();
  }

  @Override
  public void addFilter(Filter<IStatisticsEvent> newFilter) {
    fai.addFilter(newFilter);
  }

  @Override
  public void clearAllFilters() {
    fai.clearAllFilters();
  }

  @Override
  public List<Filter<IStatisticsEvent>> getCopyOfAttachedFiltersList() {
    return fai.getCopyOfAttachedFiltersList();
  }

  @Override
  public FilterReply getFilterChainDecision(IStatisticsEvent event) {
    return fai.getFilterChainDecision(event);
  }
}
