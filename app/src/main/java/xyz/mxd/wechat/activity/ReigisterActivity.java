package xyz.mxd.wechat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.Welcome;
import xyz.mxd.wechat.net.Request;
import xyz.mxd.wechat.tools.WorksSizeCheckUtil;
import xyz.mxd.wechat.util.ToastUtils;


public class ReigisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText phone;
    private EditText password;
    private Button button;
    private MyHandler myhandler = new MyHandler();

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
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
        if ((username.getText() + "").equals("") || (phone.getText() + "").equals("") || (password.getText() + "").equals("")) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
        inputFocus();
        buttonChangeColor();
        button.setOnClickListener(v -> {
            Pattern pattern = Pattern
                    .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
            Matcher matcher = pattern.matcher(phone.getText());
            if (matcher.matches()) {
                new Thread(() -> httpUrlConnPost(ReigisterActivity.this.username.getText() + "",
                        phone.getText() + "", password.getText() + "")).start();
            } else {
                Toast.makeText(getApplicationContext(), "手机格式错误", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    public void initViews() {
        username = this.findViewById(R.id.reg_name);
        phone = this.findViewById(R.id.reg_phone);
        password = this.findViewById(R.id.reg_passwd);
        button = this.findViewById(R.id.reg_button);
    }

    public void inputFocus() {
        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ImageView imageView = findViewById(R.id.reg_diver1);
                imageView.setBackgroundResource(R.color.input_dvier_focus);
            } else {
                ImageView imageView = findViewById(R.id.reg_diver1);
                imageView.setBackgroundResource(R.color.input_dvier);
            }
        });
        phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ImageView imageView = findViewById(R.id.reg_diver2);
                imageView.setBackgroundResource(R.color.input_dvier_focus);
            } else {
                ImageView imageView = findViewById(R.id.reg_diver2);
                imageView.setBackgroundResource(R.color.input_dvier);
            }
        });
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ImageView imageView = findViewById(R.id.reg_diver3);
                imageView.setBackgroundResource(R.color.input_dvier_focus);
            } else {
                ImageView imageView = findViewById(R.id.reg_diver3);
                imageView.setBackgroundResource(R.color.input_dvier);
            }
        });
    }

    public void buttonChangeColor() {
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(button);
        textChangeListener.addAllEditText(username, phone, password);//把所有要监听的EditText都添加进去
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

    public void httpUrlConnPost(String name, String phone, String password) {
        try {
            JSONObject jsonObject = Request.post(new HashMap<String, String>() {
                {
                    put("number", URLEncoder.encode(phone, "UTF-8"));
                    put("name", URLEncoder.encode(name, "UTF-8"));
                    put("phone", URLEncoder.encode(phone, "UTF-8"));
                    put("password", URLEncoder.encode(password, "UTF-8"));
                }
            }, "/Register");
            boolean result = jsonObject.getBoolean("json");
            if (result) {
                myhandler.sendEmptyMessage(1);
            } else {
                myhandler.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showMsg(getApplicationContext(), "注册成功");
                    Intent intent = new Intent();
                    try {
                        intent.putExtra("weixin_number",  URLEncoder.encode(phone.getText() + "", "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    intent.setClass(xyz.mxd.wechat.activity.ReigisterActivity.this, xyz.mxd.wechat.activity.LoginUserActivity.class);
                    startActivity(intent);
                    xyz.mxd.wechat.activity.ReigisterActivity.this.finish();
                    break;
                case 2:
                    ToastUtils.showMsg(getApplicationContext(), "手机号已被注册");
            }
        }
    }

    public void rigister_activity_back(View v) {
        Intent intent = new Intent();
        intent.setClass(xyz.mxd.wechat.activity.ReigisterActivity.this, Welcome.class);
        startActivity(intent);
        xyz.mxd.wechat.activity.ReigisterActivity.this.finish();
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

