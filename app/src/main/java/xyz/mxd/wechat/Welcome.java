package xyz.mxd.wechat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.tim.client.Options;
import org.tim.client.TIMClient;
import org.tim.client.intf.Callback;
import org.tio.core.Node;
import xyz.mxd.wechat.activity.LoginUserActivity;
import xyz.mxd.wechat.activity.MainActivity;
import xyz.mxd.wechat.activity.ReigisterActivity;
import xyz.mxd.wechat.adapter.MyHandler;
import xyz.mxd.wechat.tim.TIMMessageProcessor;
import xyz.mxd.wechat.util.ToastUtils;


public class Welcome extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉任务栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome); //设置布局
//        StatusBarCompat.setStatusBarColor(this,0,false);


    }

    //登录按钮点击事件处理方法
    public void welcome_login(View v) {
        Intent intent = new Intent();
        /* 页面跳转到登录界面*/
        intent.setClass(Welcome.this, LoginUserActivity.class);
        startActivity(intent);
        this.finish(); //结束当前activity
    }

    //注册按钮点击事件处理方法
    public void welcome_register(View v) {
        Intent intent = new Intent();
        /*页面跳转到注册界面*/
        intent.setClass(Welcome.this, ReigisterActivity.class);
        startActivity(intent);
        this.finish(); //结束当前activity
    }

}

