package xyz.mxd.wechat.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import org.tim.client.TIMClient;
import org.tim.client.intf.Callback;
import org.tim.common.packets.User;

import java.util.ArrayList;
import java.util.HashMap;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.Welcome;
import xyz.mxd.wechat.net.Request;
import xyz.mxd.wechat.tools.WorksSizeCheckUtil;
import xyz.mxd.wechat.util.ToastUtils;


public class LoginPhoneActivity extends AppCompatActivity {

    private EditText phone;
    private EditText password;
    private TextView user_login;
    private Button button;
    private MyHandler myhandler = new MyHandler();

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initViews();
        if ((phone.getText() + "").equals("") || (password.getText() + "").equals("")) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
        inputFocus(); //监听EditView变色
        buttonChangeColor(); //登录按钮变色
        //设置通过微信号登录的监听器
        user_login.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPhoneActivity.this, LoginUserActivity.class);
            startActivity(intent);
        });
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(LoginPhoneActivity.this, LoadingActivity.class);
            startActivity(intent);
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    httpUrlConnPost(LoginPhoneActivity.this.phone.getText() + "",
                            password.getText() + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

    }

    @SuppressLint("NewApi")
    public void initViews() {
        phone = this.findViewById(R.id.log_phone);
        password = this.findViewById(R.id.log_passwd);
        user_login = this.findViewById(R.id.user_log);
        button = this.findViewById(R.id.log_button);
    }

    public void inputFocus() {
        phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ImageView imageView = findViewById(R.id.login_diver1);
                imageView.setBackgroundResource(R.color.input_dvier_focus);
            } else {
                ImageView imageView = findViewById(R.id.login_diver1);
                imageView.setBackgroundResource(R.color.input_dvier);
            }
        });
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ImageView imageView = findViewById(R.id.login_diver2);
                imageView.setBackgroundResource(R.color.input_dvier_focus);
            } else {
                ImageView imageView = findViewById(R.id.login_diver2);
                imageView.setBackgroundResource(R.color.input_dvier);
            }
        });
    }

    public void buttonChangeColor() {
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(button);
        textChangeListener.addAllEditText(phone, password);
        WorksSizeCheckUtil.setChangeListener(isHasContent -> {
            if (isHasContent) {
                button.setBackgroundResource(R.drawable.login_button_focus);
                button.setTextColor(getResources().getColor(R.color.loginButtonTextFouse));
            } else {
                button.setBackgroundResource(R.drawable.login_button_shape);
                button.setTextColor(getResources().getColor(R.color.loginButtonText));
            }
        });
    }

    public void httpUrlConnPost(String phone, String password) {
        try {
            JSONObject jsonObject = Request.post(new HashMap<String, String>() {
                {
                    put("number", phone);
                    put("password", password);
                }
            }, "/Login");
            boolean result = jsonObject.getBoolean("json");
            if (result) {
                connectTIMServer(phone, password);
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
                    Intent intent = new Intent (xyz.mxd.wechat.activity.LoginPhoneActivity.this, xyz.mxd.wechat.activity.MainActivity.class);
                    intent.putExtra("weixin_number", LoginPhoneActivity.this.phone.getText().toString());
                    startActivity(intent);
                    xyz.mxd.wechat.activity.LoginPhoneActivity.this.finish();
                    break;
                case 2:
                    ToastUtils.showMsg(getApplicationContext(),"用户名或密码错误，请重新填写");
                    break;
            }
        }
    }

    //返回按钮处理事件
    public void login_activity_back(View v) {
        Intent intent = new Intent();
        intent.setClass(xyz.mxd.wechat.activity.LoginPhoneActivity.this, Welcome.class);
        startActivity(intent);
        xyz.mxd.wechat.activity.LoginPhoneActivity.this.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, me)) {
                hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(me);
    }


    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    protected void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputManager == null) {
                    return;
                }
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


}

