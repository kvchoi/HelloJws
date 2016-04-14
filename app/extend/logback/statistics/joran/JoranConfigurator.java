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
package extend.logback.statistics.joran;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.core.joran.JoranConfiguratorBase;
import ch.qos.logback.core.joran.action.AppenderRefAction;
import ch.qos.logback.core.joran.action.IncludeAction;
import ch.qos.logback.core.joran.action.NOPAction;
import ch.qos.logback.core.joran.conditional.ElseAction;
import ch.qos.logback.core.joran.conditional.IfAction;
import ch.qos.logback.core.joran.conditional.ThenAction;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.net.ssl.SSLNestedComponentRegistryRules;
import extend.logback.statistics.PatternLayout;
import extend.logback.statistics.PatternLayoutEncoder;
import extend.logback.statistics.boolex.JaninoEventEvaluator;
import extend.logback.statistics.joran.action.ConfigurationAction;
import extend.logback.statistics.joran.action.EvaluatorAction;
import extend.logback.statistics.joran.action.LoggerAction;
import extend.logback.statistics.sift.SiftAction;

/**
 * This JoranConfiguratorclass adds rules specific to logback-access.
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class JoranConfigurator extends JoranConfiguratorBase {

    @Override
    public void addInstanceRules(RuleStore rs) {
        super.addInstanceRules(rs);

        rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());

        rs.addRule(new ElementSelector("configuration/appender/sift"), new SiftAction());
        rs.addRule(new ElementSelector("configuration/appender/sift/*"), new NOPAction());

        rs.addRule(new ElementSelector("configuration/evaluator"), new EvaluatorAction());
        
        rs.addRule(new ElementSelector("configuration/logger"), new LoggerAction());

        rs.addRule(new ElementSelector("configuration/logger/appender-ref"),
                new AppenderRefAction());
        
        // add if-then-else support
        rs.addRule(new ElementSelector("*/if"), new IfAction());
        rs.addRule(new ElementSelector("*/if/then"), new ThenAction());
        rs.addRule(new ElementSelector("*/if/then/*"), new NOPAction());
        rs.addRule(new ElementSelector("*/if/else"), new ElseAction());
        rs.addRule(new ElementSelector("*/if/else/*"), new NOPAction());

        rs.addRule(new ElementSelector("configuration/include"), new IncludeAction());
    }

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        registry.add(AppenderBase.class, "layout", PatternLayout.class);
        registry.add(EvaluatorFilter.class, "evaluator", JaninoEventEvaluator.class);

        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(UnsynchronizedAppenderBase.class, "encoder", PatternLayoutEncoder.class);
        SSLNestedComponentRegistryRules.addDefaultNestedComponentRegistryRules(registry);
    }

}