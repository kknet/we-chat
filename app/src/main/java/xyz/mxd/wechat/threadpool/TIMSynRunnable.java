package xyz.mxd.wechat.threadpool;

import org.tio.utils.thread.pool.AbstractSynRunnable;

import java.util.concurrent.Executor;

public class TIMSynRunnable extends AbstractSynRunnable {


    TIMSynRunnable(Executor executor) {
        super(executor);
    }

    @Override
    public boolean isNeededExecute() {
        return true;
    }

    @Override
    public void runTask() {

    }
}
