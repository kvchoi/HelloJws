package extend.logback.logging.logback;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import extend.logback.logging.spi.IStatisticsEvent;

/**
 * Borrowed heavily from com.yammer.dropwizard.config.AsyncAppender.
 */
public class AsyncAppender extends AppenderBase<IStatisticsEvent> implements Runnable {
    private static final int BATCH_SIZE = 1000;

    public static Appender<IStatisticsEvent> wrap(Appender<IStatisticsEvent> delegate) {
        final AsyncAppender appender = new AsyncAppender(delegate);
        appender.start();
        return appender;
    }

    private static final ThreadFactory THREAD_FACTORY =
            new ThreadFactoryBuilder().setNameFormat("async-log-appender-%d")
                                      .setDaemon(true)
                                      .build();

    private final Appender<IStatisticsEvent> delegate;
    private final BlockingQueue<IStatisticsEvent> queue;
    private final List<IStatisticsEvent> batch;
    private final Thread dispatcher;
    private volatile boolean running;

    private AsyncAppender(Appender<IStatisticsEvent> delegate) {
        this.delegate = delegate;
        this.queue = Queues.newLinkedBlockingQueue();
        this.batch = Lists.newArrayListWithCapacity(BATCH_SIZE);
        this.dispatcher = THREAD_FACTORY.newThread(this);
        setContext(delegate.getContext());
    }

    @Override
    protected void append(IStatisticsEvent eventObject) {
        eventObject.prepareForDeferredProcessing();
        queue.add(eventObject);
    }

    @Override
    public void start() {
        super.start();
        this.running = true;
        dispatcher.start();
    }

    @Override
    public void stop() {
        this.running = false;
        super.stop();
    }

    @Override
    public void run() {
        while (running) {
            try {
                batch.add(queue.take());
                queue.drainTo(batch, BATCH_SIZE - 1);

                for (IStatisticsEvent event : batch) {
                    delegate.doAppend(event);
                }

                batch.clear();
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
