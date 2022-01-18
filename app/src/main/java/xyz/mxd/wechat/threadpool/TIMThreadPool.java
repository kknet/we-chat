package xyz.mxd.wechat.threadpool;


import android.util.Log;

import org.tim.client.TIMClient;
import org.tio.utils.thread.pool.SynThreadPoolExecutor;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 将TIMClient提供的线程池封装起来
 */
public class TIMThreadPool {

    private static final ThreadPoolExecutor executor;

    private static final SynThreadPoolExecutor synThreadPoolExecutor;

    private static final TIMSynRunnable timSynRunnable;

    static {
        executor = TIMClient.executor;
        synThreadPoolExecutor = TIMClient.synExecutor;
        timSynRunnable = new TIMSynRunnable(synThreadPoolExecutor);
    }

    public static void execute (Runnable runnable){
        if (runnable == null) {
            Log.i("execute", "null");
            return;
        }
        if (executor == null) {
            Log.i("executor", "null");
            return;
        }
        executor.execute(runnable);
    }

    public static <T> void submit (Runnable runnable, T t) {
        synThreadPoolExecutor.submit(runnable, t);
    }

    /**
     * 执行一个寂寞
     */
    public static void synExecute () {
        timSynRunnable.execute();
    }


}
