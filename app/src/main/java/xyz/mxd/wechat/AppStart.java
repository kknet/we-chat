package xyz.mxd.wechat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.tbruyelle.rxpermissions2.RxPermissions;
import org.tim.client.TIMClient;
import org.tim.common.packets.ChatBody;
import java.util.Date;
import io.reactivex.disposables.Disposable;
import xyz.mxd.wechat.widget.SetPermissionDialog;

public class AppStart extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //要求窗口没有title
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
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
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(aBoolean -> {
                    if (aBoolean) {
//                            Intent intent = new Intent(xyz.mxd.wechat.AppStart.this, xyz.mxd.wechat.activity.MainActivity.class);
                        Intent intent = new Intent(AppStart.this, Welcome.class);
//                            intent.putExtra("weixin_number", "18274366030");
                        startActivity(intent);

                        AppStart.this.finish();
                    } else {

                        SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(AppStart.this);
                        mSetPermissionDialog.show();
                        mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                            @Override
                            public void onLeftClick() {
                                finish();
                            }

                            @Override
                            public void onRightClick() {

                                finish();
                            }
                        });
                    }
                });
    }
}

