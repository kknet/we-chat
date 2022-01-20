package xyz.mxd.wechat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.yzq.zxinglibrary.common.Constant;
import com.githang.statusbar.StatusBarCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import org.json.JSONObject;
import org.tim.client.TIMClient;
import org.tim.common.packets.User;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.bean.Contact;
import xyz.mxd.wechat.net.Request;
import xyz.mxd.wechat.util.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_SCAN = 111;
    public static ArrayList<User> list;
    public static List<Map<String, String>> data = new ArrayList<>();
    private MyHandler myhandler = new MyHandler();
    public static String number;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        Intent intent = getIntent();
        number = intent.getStringExtra("weixin_number");
//        TIMClient.getInstance().messageReq("15511090451");
        TIMClient.getInstance().authReq();
        Thread thread1 = new Thread(() -> httpUrlConnPost(String.valueOf(number)));
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initData();
        setContentView(R.layout.activity_main);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white, null),true);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_friend, R.id.navigation_self)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExitApp = intent.getBooleanExtra("exit", false);
            if (isExitApp) {
                TIMClient.getInstance().logout();
                this.finish();
                System.exit(0);
            }
        }
    }

    public void buttonTopRight(View v) {
        View viewById = getWindow().getDecorView().findViewById(R.id.plus);
        showPopupMenu(viewById);

    }

    @SuppressLint("RestrictedApi")
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.conversation_menu, popupMenu.getMenu());
        try {
            Field mPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mPopup.setAccessible(true);
            MenuPopupHelper mHelper = (MenuPopupHelper) mPopup.get(popupMenu);
            assert mHelper != null;
            mHelper.setForceShowIcon(true);
            Class standardMenuClass = Class.forName("androidx.appcompat.view.menu.StandardMenuPopup");
            Field mHasContentWidth = standardMenuClass.getDeclaredField("mHasContentWidth");
            mHasContentWidth.setAccessible(true);
            mHasContentWidth.setBoolean(mHelper.getPopup(), true);
            Field mContentWidth = standardMenuClass.getDeclaredField("mContentWidth");
            mContentWidth.setAccessible(true);
            mContentWidth.setInt(mHelper.getPopup(), 400);
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getTitle().toString()) {
                case "视频会议":

                    break;
                case "创建群聊":

                    break;
                case "添加朋友":

                    break;
                case "扫一扫":
                    scan_start();
                    break;
            }
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        });
        popupMenu.setOnDismissListener(menu -> {
            //
        });

        popupMenu.show();
    }

    private void initData() {
        Map<String, String> map = new HashMap<String, String>();
        List<User> friends = TIMClient.getInstance().getUser().getFriends();
        data.add(map);
        MainActivity.list = new ArrayList<>();
        Collections.sort(friends); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        friends.add(0, User.newBuilder().nick("新的朋友").avatar(String.valueOf(R.drawable.em_friends_new_chat)).build());
        friends.add(1, User.newBuilder().nick("群聊").avatar(String.valueOf(R.drawable.em_friends_chat_room)).build());
        friends.add(2, User.newBuilder().nick("标签").avatar(String.valueOf(R.drawable.em_friends_label)).build());
        friends.add(3, User.newBuilder().nick("公众号").avatar(String.valueOf(R.drawable.em_friends_group_chat)).build());
    }

    public void httpUrlConnPost(String number) {
        try {
            JSONObject jsonObject = Request.post(new HashMap<String, String>() {
                {
                    put("number", number);
                }
            }, "/Contact");
            List<Contact> contacts = JSON.parseArray(jsonObject.getJSONArray("json").toString(), Contact.class);
            List<User> userList = new ArrayList<>();
            contacts.forEach(action -> userList.add(User.newBuilder().userId(action.getFriendid()).avatar(action.getImg()).nick(action.getName()).build()));
            System.out.println(userList.size());
            TIMClient.getInstance().getUser().setFriends(userList);
            boolean result = jsonObject.getBoolean("rjson");
            if (result) {
                myhandler.sendEmptyMessage(1);
                Log.i("用户", "获取好友列表成功");
            } else {
                myhandler.sendEmptyMessage(2);
                Log.i("用户", "失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            myhandler.sendEmptyMessage(2);
        }
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
            }
        }
    }


    // ----------------------  扫一扫-------------------
    private void scan_start(){
        AndPermission.with(this)
                .runtime()
                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    /*ZxingConfig是配置类
                     *可以设置是否显示底部布局，闪光灯，相册，
                     * 是否播放提示音  震动
                     * 设置扫描框颜色等
                     * 也可以不传这个参数
                     * */
                    ZxingConfig config = new ZxingConfig();
                    config.setPlayBeep(true);//是否播放扫描声音 默认为true
                    config.setShake(true);//是否震动  默认为true
                    config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                    config.setReactColor(R.color.colorPrimary);//设置扫描框四个角的颜色 默认为白色
//                    config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
                    config.setScanLineColor(R.color.colorPrimary);//设置扫描线的颜色 默认白色
                    config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    Toast.makeText(getApplicationContext(), "没有权限无法扫描呦", Toast.LENGTH_SHORT).show();
                })
                .start();
    }


    /**
     * 处理数据返回 回调事件..  本页面用于扫一扫
     * @param requestCode 1111
     * @param resultCode 1
     * @param data 1
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Uri uri = Uri.parse(content);
                ToastUtils.showMsg(this, uri.toString());
                Intent it = new Intent();
                it.putExtra("url", uri.toString());
                it.setClass(getApplication(), WebViewActivity.class);
                startActivity(it);

            }
        }

    }
}
