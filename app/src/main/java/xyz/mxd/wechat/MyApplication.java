package xyz.mxd.wechat;

import android.app.Application;

public class MyApplication extends Application {
    public static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;

    }

}
