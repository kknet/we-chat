package xyz.mxd.wechat.ui.friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.tim.client.TIMClient;
import org.tim.client.intf.Callback;
import org.tim.common.packets.User;
import org.tim.common.util.json.JsonKit;
import java.util.List;
import java.util.Objects;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.adapter.SortAdapter;
import xyz.mxd.wechat.tools.SideBar;
import xyz.mxd.wechat.util.ToastUtils;

public class FriendFragment extends Fragment {



    private ListView listView;
    private SideBar sideBar;


    @SuppressLint("StaticFieldLeak")
    private static View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_friend, container, false);

        initView(view);
        listView = view.findViewById(R.id.listView);
        sideBar = view.findViewById(R.id.side_bar);
        List<User> friends = TIMClient.getInstance().getUser().getFriends();
        SortAdapter adapter = new SortAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), friends);
        listView.setAdapter(adapter);
        sideBar.setOnStrSelectCallBack((index, selectStr) -> {
            for (int i = 0; i < friends.size(); i++) {
                if (friends.get(i).getNick().equals("新的朋友") || friends.get(i).getNick().equals("群聊") ||
                        friends.get(i).getNick().equals("标签") || friends.get(i).getNick().equals("公众号"))
                    continue;
                if (selectStr.equalsIgnoreCase(friends.get(i).getFirstLetter())) {
                    listView.setSelection(i); // 选择到首字母出现的位置
                    return;
                }
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {

            if (position > 3) {
                Log.i("onItemClick", String.valueOf(position));
                User user = friends.get(position);
                Intent intent = new Intent();
                intent.putExtra("user", JsonKit.toJSONString(user));
                intent.setClass(Objects.requireNonNull(getActivity()),
                        xyz.mxd.wechat.activity.ContactDetailActivity.class);
                startActivity(intent);
            }else if (position == 0) {
                ToastUtils.showToast("温馨提示", "新的朋友", getContext(), new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getContext(), "您点击了新的朋友");
                    }

                    @Override
                    public void fail() {

                    }
                });
            }else if (position == 1) {
                ToastUtils.showToast("温馨提示", "群聊", getContext(), new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getContext(), "您点击了群聊");
                    }

                    @Override
                    public void fail() {

                    }
                });
            }else if (position == 2) {
                ToastUtils.showToast("温馨提示", "标签", getContext(), new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getContext(), "您点击了标签");
                    }

                    @Override
                    public void fail() {

                    }
                });
            }else if (position == 3) {
                ToastUtils.showToast("温馨提示", "公众号", getContext(), new Callback() {
                    @Override
                    public void success() {
                        ToastUtils.showMsg(getContext(), "您点击了公众号");
                    }

                    @Override
                    public void fail() {

                    }
                });
            }

        });
        return view;
    }


    private void initView(View view) {
        TextView lblTitle= view.findViewById(R.id.common_toolbar_title);
        lblTitle.setText("通讯录");
        ImageView image_left = view.findViewById(R.id.image_left);
        image_left.setVisibility(View.GONE);
    }



}