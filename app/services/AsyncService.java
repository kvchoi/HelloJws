package services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.RandomStringUtils;

import jws.libs.F;
import jws.libs.F.Promise;
import jws.libs.WS.HttpResponse;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class AsyncService {
    protected static ExecutorService executor = Executors.newCachedThreadPool();
    protected static ListeningExecutorService ListeningExecutor = MoreExecutors
            .listeningDecorator(executor);

    public static F.Promise<String> asyncGet() {

        ListenableFuture lf = ListeningExecutor.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                // 评估并发时间
                System.out.println(Thread.currentThread().getName() + ":"
                        + System.currentTimeMillis());
                Thread.currentThread().sleep(2000);
                System.out.println(Thread.currentThread().getName() + ":"
                        + System.currentTimeMillis());
                return RandomStringUtils.randomAlphabetic(5);
            }
        });
        final Promise<String> smartFuture = new Promise<String>();
        Futures.addCallback(lf, new FutureCallback<String>() {
            public void onSuccess(String result) {
                smartFuture.invoke(result);
            }

            public void onFailure(Throwable t) {
                smartFuture.invokeWithException(t);
            }
        });
        return smartFuture;
    }

}
