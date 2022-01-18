package xyz.mxd.wechat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.githang.statusbar.StatusBarCompat;
import xyz.mxd.wechat.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView image_left;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white, null),true);
        TextView lblTitle=(TextView)findViewById(R.id.common_toolbar_title);
        lblTitle.setText(R.string.webview_title);
        image_left = findViewById(R.id.image_left);
        image_left.setVisibility(View.VISIBLE);
        ImageView image_right = findViewById(R.id.plus);
        image_right.setVisibility(View.GONE);
        initListener();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        WebView mWebView = findViewById(R.id.mWebView);

        WebSettings setting = mWebView.getSettings();
        setting.setPluginState(WebSettings.PluginState.ON);
        setting.setJavaScriptEnabled(true);
        //设置滚动条的样式
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

        //复写WebViewClient的shouldOverrideUrlLoading()的方法
        //如果需要事件处理返回false,否则返回true.这样就可以解决问题了
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                lodurl(view, url);
                return false;
            }
        });

        mWebView.loadUrl(url);
    }

    public void lodurl(final WebView webView, final String url) {
        new Thread(() -> webView.loadUrl(url));
    }

    protected void initListener() {
        image_left.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_left) {
            finish();
        }
    }
}