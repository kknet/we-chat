package xyz.mxd.wechat.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.tim.client.TIMClient;
import org.tim.common.packets.User;
import org.tim.common.util.json.JsonKit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.activity.MainActivity;
import xyz.mxd.wechat.adapter.ImageAdapter;
import xyz.mxd.wechat.adapter.MyHandler;
import xyz.mxd.wechat.threadpool.TIMThreadPool;

public class HomeFragment extends Fragment {


    private String number;
    private ListView listView;
    private BaseAdapter adapter;

    @SuppressLint("StaticFieldLeak")
    private static View view;

    public HomeFragment() {

        // 启动sdk的监听
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        number = MainActivity.number;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        adapter = new ImageAdapter(getContext(), TIMClient.getInstance().getList(), TIMClient.getInstance().getMap());
        TIMThreadPool.execute(() -> httpUrlConnPost(String.valueOf(number)));
        listView = view.findViewById(R.id.listView);
        TIMClient.getInstance().set("handler", new MyHandler(adapter));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String s = TIMClient.getInstance().getList().get(TIMClient.getInstance().getList().size() - position - 1);
            final Map<String, String> stringStringMap = TIMClient.getInstance().getMap().get(s);
            Intent intent = new Intent();
            List<User> friends = TIMClient.getInstance().getUser().getFriends();
            final User[] from = new User[1];
            friends.forEach(user -> {
                if (user.getUserId() == null) {
                    return;
                }
                assert stringStringMap != null;
                if (user.getUserId().equals(stringStringMap.get("id"))) {
                    from[0] = user;
                }
            });
            intent.putExtra("user", JsonKit.toJSONString(from[0]));
            intent.setClass(Objects.requireNonNull(getActivity()),
                    xyz.mxd.wechat.activity.ChatActivity.class);
            startActivity(intent);
        });

        return view;
    }


    private void initView(View view) {
        TextView lblTitle= view.findViewById(R.id.common_toolbar_title);
        lblTitle.setText(R.string.app_name);
        ImageView image_left = view.findViewById(R.id.image_left);
        image_left.setVisibility(View.GONE);

    }

    private void httpUrlConnPost(String number) {

        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("pic", "https://gitee.com/mxd_2022/smartboot/raw/master/images/author.png");
        messageMap.put("title", "t-im作者");
        messageMap.put("content", "欢迎来到A信世界");
        messageMap.put("time", "2022-01-17");
        messageMap.put("code", "");
        messageMap.put("id", "15511090451");
        if ((boolean) TIMClient.getInstance().get("first") && !TIMClient.getInstance().getUser().getUserId().equals("15511090451")) {
            TIMClient.getInstance().getMap().put("15511090451", messageMap);
            TIMClient.getInstance().getList().add("15511090451");
            TIMClient.getInstance().set("first", false);
        }

    }




}