package xyz.mxd.wechat.ui.self;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import org.tim.client.TIMClient;
import org.tim.client.intf.Callback;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.activity.AboutIMActivity;
import xyz.mxd.wechat.activity.MainActivity;
import xyz.mxd.wechat.tools.GetImageByUrl;
import xyz.mxd.wechat.util.ToastUtils;
import xyz.mxd.wechat.widget.ArrowItemView;
import xyz.mxd.wechat.widget.EaseImageView;

public class SelfFragment extends Fragment implements View.OnClickListener {



    private ConstraintLayout clUser;
    private TextView name;
    private ArrowItemView itemCommonSet;
    private ArrowItemView itemFeedback;
    private ArrowItemView itemAboutHx;
    private ArrowItemView itemDeveloperSet;
    private ArrowItemView itemCommonMoney;
    private Button mBtnLogout;
    @SuppressLint("StaticFieldLeak")
    private static View view;


    protected int getLayoutId() {
        return R.layout.fragment_self;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_self, container, false);
        initView(view);
        getLayoutId();
        initListener();
        return view;
    }




    private void initView(View view) {

        TextView lblTitle= view.findViewById(R.id.common_toolbar_title);
        lblTitle.setText("我的");
        ImageView image_left = view.findViewById(R.id.image_left);
        image_left.setVisibility(View.GONE);
        ImageView image_right = view.findViewById(R.id.plus);
        image_right.setVisibility(View.GONE);
        clUser = view.findViewById(R.id.cl_user);
        name = view.findViewById(R.id.name);
        itemCommonSet = view.findViewById(R.id.item_common_set);
        itemFeedback = view.findViewById(R.id.item_feedback);
        itemAboutHx = view.findViewById(R.id.item_about_hx);
        itemDeveloperSet = view.findViewById(R.id.item_developer_set);
        itemCommonMoney = view.findViewById(R.id.item_common_money);
        mBtnLogout = view.findViewById(R.id.btn_logout);
        name.setText(TIMClient.getInstance().getUser().getNick());
        EaseImageView mAvatarUser = view.findViewById(R.id.avatar);
        GetImageByUrl getImageByUrl = new GetImageByUrl();
        getImageByUrl.setImage(mAvatarUser, TIMClient.getInstance().getUser().getAvatar());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout :
                logout();
                break;
            case R.id.cl_user:
//                UserDetailActivity.actionStart(getActivity());
                break;
            case R.id.item_common_set:
//                SetIndexActivity.actionStart(getActivity());
                break;
            case R.id.item_feedback:
//                FeedbackActivity.actionStart(getActivity());
                break;
            case R.id.item_about_hx:
                Intent intent = new Intent(getActivity(), AboutIMActivity.class);
                startActivity(intent);

//                this.startActivity(new Intent(this, AboutIMActivity.class));
//                new AboutIMActivity().actionStart(getActivity());
                break;
            case R.id.item_developer_set:
//                DeveloperSetActivity.actionStart(getActivity());
                break;
            case R.id.item_common_money:
                ToastUtils.showToast("温馨提示", "钱包-尽情期待......", getActivity(), new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getContext(), "您点击了钱包");
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
        }
    }


    private void logout() {

        ToastUtils.showToast("温馨提示", "您确定要退出程序吗?", getContext(), new Callback() {
            @Override
            public void success() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("exit", true);
                startActivity(intent);
            }

            @Override
            public void fail() {
            }
        });

    }


    private void initListener() {
        mBtnLogout.setOnClickListener(this);
        clUser.setOnClickListener(this);
        itemCommonSet.setOnClickListener(this);
        itemFeedback.setOnClickListener(this);
        itemAboutHx.setOnClickListener(this);
        itemDeveloperSet.setOnClickListener(this);
        itemCommonMoney.setOnClickListener(this);
    }


}