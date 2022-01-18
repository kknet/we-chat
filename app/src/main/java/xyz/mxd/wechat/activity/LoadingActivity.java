package xyz.mxd.wechat.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import xyz.mxd.wechat.R;


public class LoadingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading); //设置布局
        //一秒后结束当前activity
        new Handler().postDelayed(LoadingActivity.this::finish, 1000);
    }
}
