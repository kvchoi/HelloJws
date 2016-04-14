package extend;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import extend.log4j2.ConfigTest;
import extend.logback.statistics.StatisticsLog;
import jws.Init;

public class INIT implements Init {

    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

    @Override
    public void init() {
        // StatisticsLogImpl.getInstance().start();
        executor.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                StatisticsLog.getInstance().logDummy();
            }
        }, 10, 60, TimeUnit.SECONDS);
        
        ConfigTest.test1();
    }

}
