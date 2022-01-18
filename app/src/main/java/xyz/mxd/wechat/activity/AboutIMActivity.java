package xyz.mxd.wechat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.githang.statusbar.StatusBarCompat;
import xyz.mxd.wechat.BuildConfig;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.widget.ArrowItemView;


public class AboutIMActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_version;
    private ArrowItemView item_product;
    private ArrowItemView item_company;
    private ImageView image_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_im);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white, null),true);
        initView();
        initData();
        initListener();
        getLayoutId();
    }

    private void initView() {
        ImageView image_right = findViewById(R.id.plus);
        image_right.setVisibility(View.GONE);
        tv_version = findViewById(R.id.tv_version);
        item_product = findViewById(R.id.item_product);
        item_company = findViewById(R.id.item_company);
        image_left = findViewById(R.id.image_left);
        TextView lblTitle= findViewById(R.id.common_toolbar_title);
        lblTitle.setText(R.string.app_name);
    }


    protected int getLayoutId() {
        return R.layout.activity_about_im;
    }



    protected void initData() {

        tv_version.setText(getString(R.string.about_im_version, BuildConfig.VERSION_NAME));
    }

    protected void initListener() {
        item_product.setOnClickListener(this);
        item_company.setOnClickListener(this);
        image_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_product :
                jumpToIMIntroduction();
                break;
            case R.id.item_company :
                jumpToCompanyIntroduction();
                break;
            case R.id.image_left :
                finish();
                break;
        }
    }

    private void jumpToIMIntroduction() {
        Uri uri = Uri.parse("https://gitee.com/mxd_2022/t-im");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    private void jumpToCompanyIntroduction() {
        Uri uri = Uri.parse("https://gitee.com/mxd_2022/t-im");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }


}
