package extend.logback.logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import jws.mvc.Http.Request;
import jws.mvc.Http.Response;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import extend.logback.logging.spi.IStatisticsEvent;
import extend.logback.logging.spi.StatisticsEvent;

public class AsyncStatisticsLogger {
    private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();
    private static final int BATCH_SIZE = 512;
    private final BlockingQueue<String> queue;
    private final Dispatcher dispatcher;
    private final Thread dispatchThread;
    private final AppenderAttachableImpl<IStatisticsEvent> appenders;
    private List<String> cookies;

    public AsyncStatisticsLogger(AppenderAttachableImpl<IStatisticsEvent> appenders,
            final TimeZone timeZone, List<String> cookies) {
        this.cookies = cookies;
        this.queue = new LinkedBlockingQueue<String>();
        this.dispatcher = new Dispatcher();
        this.dispatchThread = new Thread(dispatcher);
        dispatchThread.setName("async-request-log-dispatcher-" + THREAD_COUNTER.incrementAndGet());
        dispatchThread.setDaemon(true);

        this.appenders = appenders;
    }

    private class Dispatcher implements Runnable {
        private volatile boolean running = true;
        private final List<String> statements = new ArrayList<String>(BATCH_SIZE);

        @Override
        public void run() {
            while (running) {
                try {
                    statements.add(queue.take());
                    queue.drainTo(statements, BATCH_SIZE);

                    for (String statement : statements) {
                        final StatisticsEvent event = new StatisticsEvent("test", statement);
                        appenders.appendLoopOnAppenders(event);
                    }

                    statements.clear();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        public void stop() {
            this.running = false;
        }
    }

    protected void doStart() throws Exception {
        final Iterator<Appender<IStatisticsEvent>> iterator = appenders.iteratorForAppenders();
        while (iterator.hasNext()) {
            iterator.next().start();
        }
        dispatchThread.start();
    }

    protected void doStop() throws Exception {
        dispatcher.stop();
        final Iterator<Appender<IStatisticsEvent>> iterator = appenders.iteratorForAppenders();
        while (iterator.hasNext()) {
            iterator.next().stop();
        }
    }

    public void log(Request request, Response response) {
        // copied almost entirely from NCSARequestLog
        final StringBuilder buf = new StringBuilder(256);
        buf.append(" [");
        buf.append("] \"");
        buf.append(request.method);
        buf.append(' ');
        buf.append(request.url);
        buf.append(' ');
        buf.append(request.secure ? "https" : "http");
        buf.append("\" ");
        buf.append("hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | hello logback | ");
        queue.add(buf.toString());
    }
}
