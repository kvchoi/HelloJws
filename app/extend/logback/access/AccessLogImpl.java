package extend.logback.access;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import jws.mvc.Http;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.util.FileUtil;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.util.StatusPrinter;
import extend.logback.access.joran.JoranConfigurator;
import extend.logback.access.jws.JwsServerAdapter;
import extend.logback.access.spi.AccessEvent;
import extend.logback.access.spi.IAccessEvent;
import extend.logback.access.spi.ServerAdapter;

public class AccessLogImpl extends ContextBase implements AppenderAttachable<IAccessEvent>,
        FilterAttachable<IAccessEvent> {
    private static AccessLogImpl instance;

    public final static String DEFAULT_CONFIG_FILE = "conf" + File.separatorChar + "env"
            + File.separatorChar + "logback-access.xml";

    AppenderAttachableImpl<IAccessEvent> aai = new AppenderAttachableImpl<IAccessEvent>();
    FilterAttachableImpl<IAccessEvent> fai = new FilterAttachableImpl<IAccessEvent>();
    String fileName;
    String resource;
    boolean started = false;
    boolean quiet = false;

    private AccessLogImpl() {
        putObject(CoreConstants.EVALUATOR_MAP, new HashMap());
    }

    public static AccessLogImpl getInstance() {
        if (instance == null) {
            synchronized (AccessLogImpl.class) {
                if (instance == null)
                    instance = new AccessLogImpl();
            }
        }
        return instance;
    }

    public void log(Http.Request request, Http.Response response) {
        ServerAdapter adapter = new JwsServerAdapter(request, response);
        IAccessEvent accessEvent = new AccessEvent(request, response, adapter);
        if (getFilterChainDecision(accessEvent) == FilterReply.DENY) {
            return;
        }
        // 加到所有appender
        // aai.appendLoopOnAppenders(accessEvent);
        // 加到指定appender
        aai.getAppender("FILE").doAppend(accessEvent);
    }

    private void addInfo(String msg) {
        getStatusManager().add(new InfoStatus(msg, this));
    }

    private void addError(String msg) {
        getStatusManager().add(new ErrorStatus(msg, this));
    }

    @Override
    public void start() {
        configure();
        if (!isQuiet()) {
            StatusPrinter.print(getStatusManager());
        }
        started = true;
    }

    protected void configure() {
        URL configURL = getConfigurationFileURL();
        if (configURL != null) {
            runJoranOnFile(configURL);
        } else {
            addError("Could not find configuration file for logback-access");
        }
    }

    protected URL getConfigurationFileURL() {
        if (fileName != null) {
            addInfo("Will use configuration file [" + fileName + "]");
            File file = new File(fileName);
            if (!file.exists())
                return null;
            return FileUtil.fileToURL(file);
        }
        if (resource != null) {
            addInfo("Will use configuration resource [" + resource + "]");
            return this.getClass().getResource(resource);
        }

        String jettyHomeProperty = OptionHelper.getSystemProperty("jetty.home");
        String defaultConfigFile = DEFAULT_CONFIG_FILE;
        if (!OptionHelper.isEmpty(jettyHomeProperty)) {
            defaultConfigFile = jettyHomeProperty + File.separatorChar + DEFAULT_CONFIG_FILE;
        } else {
            addInfo("[jetty.home] system property not set.");
        }
        File file = new File(defaultConfigFile);
        addInfo("Assuming default configuration file [" + defaultConfigFile + "]");
        if (!file.exists())
            return null;
        return FileUtil.fileToURL(file);
    }

    private void runJoranOnFile(URL configURL) {
        try {
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(this);
            jc.doConfigure(configURL);
            if (getName() == null) {
                setName("LogbackRequestLog");
            }
        } catch (JoranException e) {
            // errors have been registered as status messages
        }
    }

    @Override
    public void stop() {
        aai.detachAndStopAllAppenders();
        started = false;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    @Override
    public void addAppender(Appender<IAccessEvent> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<IAccessEvent>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<IAccessEvent> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<IAccessEvent> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<IAccessEvent> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }

    @Override
    public void addFilter(Filter<IAccessEvent> newFilter) {
        fai.addFilter(newFilter);
    }

    @Override
    public void clearAllFilters() {
        fai.clearAllFilters();
    }

    @Override
    public List<Filter<IAccessEvent>> getCopyOfAttachedFiltersList() {
        return fai.getCopyOfAttachedFiltersList();
    }

    @Override
    public FilterReply getFilterChainDecision(IAccessEvent event) {
        return fai.getFilterChainDecision(event);
    }
}
