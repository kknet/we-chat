package xyz.mxd.wechat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;
import org.tim.client.Options;
import org.tim.client.TIMClient;
import org.tim.client.intf.Callback;
import org.tim.common.packets.ChatBody;
import org.tim.common.packets.User;
import org.tio.core.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import xyz.mxd.wechat.adapter.MyHandler;
import xyz.mxd.wechat.dao.TIMSQLiteOpenHelper;
import xyz.mxd.wechat.net.Request;
import xyz.mxd.wechat.threadpool.TIMThreadPool;
import xyz.mxd.wechat.tim.TIMMessageProcessor;
import xyz.mxd.wechat.util.ToastUtils;

public class AppStart extends AppCompatActivity {

    private MyHandler myhandler = new MyHandler();

    private String weixinNumber;
    private String password;
    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //要求窗口没有title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        context = getApplicationContext();
        //去掉任务栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTheme(R.style.AppTheme);//恢复原有的样式
        setContentView(R.layout.app_start); //设置布局
        // 处理安卓4.4以下https握手失败异常
        try{
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

//        StatusBarCompat.setStatusBarColor(this,0,false);
        new Handler().postDelayed(this::requestPermisson, 2000);




    }





    public static void doThing() {
        TIMClient.getInstance().authReq();
        TIMClient.getInstance().joinGroup("200");
        ChatBody.Builder builder = ChatBody.newBuilder();
        ChatBody body = builder.from("999")
                .to("119119")
                .content("集群私聊消息")
                .msgType(0)
                .chatType(2)
                .setCreateTime(new Date().getTime())
                .groupId(null)
                .build();

        TIMClient.getInstance().sendChatBody(body, 100);
        try {
            Thread.sleep(2000);
            TIMClient.getInstance().messageReq("119119");
            TIMClient.getInstance().onlineUserId();
            Thread.sleep(2000);
//            TIMClient.logout();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void requestPermisson(){
        RxPermissions rxPermission = new RxPermissions(this);
        Disposable subscribe = rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET
                )
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        init();
                    } else {
                        ToastUtils.showToast("提示", "请赋予权限", context, new Callback() {
                            @Override
                            public void success() {
                                Disposable subscribe1 =  rxPermission.request(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.RECORD_AUDIO,
                                                Manifest.permission.INTERNET
                                        )
                                        .subscribe(aBoolean -> {
                                            if (aBoolean) {
                                                init();
                                        }else {
                                                finish();
                                            }
                            });
                            }

                            @Override
                            public void fail() {
                                finish();
                            }
                        });
                    }
                });
    }

    private void init() throws InterruptedException {
        // 初始化
        Thread thread = new Thread(() -> {
            Options options = new Options(new Node("mixiaodong.xyz", 8888));
            TIMClient.start(options, new TIMMessageProcessor());
        });
        thread.start();
        List<String> list = TIMSQLiteOpenHelper.queryUser();
        if (list.size() == 2) {
            weixinNumber = list.get(0);
            password = list.get(1);
            thread.join(5000L);
            TIMThreadPool.execute(() -> httpUrlConnPost(weixinNumber + "",
                    password + ""));
            return;
        }
        Intent intent = new Intent(AppStart.this, Welcome.class);
        startActivity(intent);
        AppStart.this.finish();
    }


    public void httpUrlConnPost(String number, String password) {
        try {
            JSONObject jsonObject = Request.post(new HashMap<String, String>() {
                {
                    put("number", number);
                    put("password", password);
                }
            }, "/Login");
            System.out.println(number + "-" + password);
            boolean result = jsonObject.getBoolean("json");
            if (result) {
                connectTIMServer(number, password);
                Log.i("用户", "登录成功");
            } else {
                myhandler.sendEmptyMessage(2);
                Log.i("用户", "登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            myhandler.sendEmptyMessage(2);
        }
    }

    private void connectTIMServer(String number, String pass) {
        TIMClient.getInstance().login(number, pass, new Callback() {
            @Override
            public void success() {
                TIMClient.getInstance().setUser(User.newBuilder()
                        .nick("A信")
                        .avatar("https://gitee.com/mxd_2022/smartboot/raw/master/images/user.jpg")
                        .build());
                TIMClient.getInstance().set("first", true);
                TIMClient.getInstance().getUser().setUserId(number);
                TIMClient.getInstance().setExtraMap(new HashMap<>());
                TIMClient.getInstance().setExtraList(new ArrayList<>());
                myhandler.sendEmptyMessage(1);

            }

            @Override
            public void fail() {
                ToastUtils.showToast("错误提示", "IM服务器登录失败，请重启APP", getApplicationContext(), new Callback() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void fail() {

                    }
                });
            }
        });
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showMsg(getApplicationContext(), "登录成功");
                    Intent intent = new Intent();
                    intent.putExtra("weixin_number", weixinNumber);
                    intent.setClass(xyz.mxd.wechat.AppStart.this,
                            xyz.mxd.wechat.activity.MainActivity.class);
                    startActivity(intent);
                    AppStart.this.finish();
                    break;
                case 2:
                    ToastUtils.showMsg(getApplicationContext(),"用户名或密码错误，请重新填写");
                    break;
            }
        }
    }
}

