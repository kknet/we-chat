package xyz.mxd.wechat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import com.githang.statusbar.StatusBarCompat;
import org.tim.client.intf.Callback;
import org.tim.common.packets.User;
import org.tim.common.util.json.JsonKit;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.tools.GetImageByUrl;
import xyz.mxd.wechat.util.ToastUtils;
import xyz.mxd.wechat.widget.EaseImageView;

public class ContactDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EaseImageView mAvatarUser;
    private TextView mTvName;
    private TextView mTvNote;
    private ImageView iv_skip;
    private TextView mBtnChat;
    private TextView mBtnVoice;
    private TextView mBtnVideo;
    private Group mGroupFriend;
    private ImageView image_left;

    private User user;
    private String userJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userJson = intent.getStringExtra("user");
        user = JsonKit.toBean(userJson, User.class);
        setContentView(R.layout.activity_friends_contact_detail);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white, null),true);
        initView();
        initListener();
        mTvName.setText(user.getNick());
        GetImageByUrl getImageByUrl = new GetImageByUrl();
        getImageByUrl.setImage(mAvatarUser, user.getAvatar());

    }

    public void actionStart(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", userJson);
        context.startActivity(intent);
    }


    protected void initView() {
        TextView lblTitle= findViewById(R.id.common_toolbar_title);
        image_left = findViewById(R.id.image_left);
        ImageView image_right = findViewById(R.id.plus);
        image_right.setVisibility(View.GONE);

        mAvatarUser = findViewById(R.id.avatar_user);
        mTvName = findViewById(R.id.tv_name);
        mTvNote = findViewById(R.id.tv_note);
        iv_skip = findViewById(R.id.iv_skip);
        mBtnChat = findViewById(R.id.btn_chat);
        mBtnVoice = findViewById(R.id.btn_voice);
        mBtnVideo = findViewById(R.id.btn_video);
        mGroupFriend = findViewById(R.id.group_friend);
    }

    protected void initListener() {
        image_left.setOnClickListener(this);
        mTvNote.setOnClickListener(this);
        iv_skip.setOnClickListener(this);
        mBtnChat.setOnClickListener(this);
        mBtnVoice.setOnClickListener(this);
        mBtnVideo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_left :
                finish();
                break;
            case R.id.tv_note :
            case R.id.iv_skip :
                ToastUtils.showMsg(getApplicationContext(), "跳转到备注设置");
                break;
            case R.id.btn_chat :
                actionStart(this);
                break;
            case R.id.btn_voice :
                ToastUtils.showToast("温馨提示", "语音通话-尽情期待......", this, new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getApplicationContext(), "您点击了语音通话");
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
            case R.id.btn_video :
                ToastUtils.showToast("温馨提示", "视频通话-尽情期待......", this, new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getApplicationContext(), "您点击了视频通话");
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
        }
    }

}
