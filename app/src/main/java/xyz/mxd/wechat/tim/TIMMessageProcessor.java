package xyz.mxd.wechat.tim;

import android.annotation.SuppressLint;
import org.tim.client.TIMClient;
import org.tim.client.intf.MessageProcessor;
import org.tim.common.packets.ChatBody;
import org.tim.common.packets.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import xyz.mxd.wechat.adapter.MyHandler;


public class TIMMessageProcessor implements MessageProcessor {


    @Override
    public void afterLogin() {
//        System.out.println("登陆成功");

    }

    @Override
    public void OnMessage(ChatBody body) {
        User user = getUserById(body.getFrom());
        if (body.getCreateTime() == null) {
            // 同步回执消息，不用理会
            return;
        }
        handlerMessage(body.getCreateTime(), user.getAvatar(), user.getNick(), body.getContent(), body.getFrom());
        handlerMessage(body);
    }

    @Override
    public void authSuccess(String key) {

        System.out.println(key);
    }

    @Override
    public void getMessageDataAfter(String data) {

        System.out.println(data);

    }

    @Override
    public void getOnlineUserIdAfter(String ids) {

        System.out.println(ids);
    }

    @Override
    public void ackTimeOut(Integer integer) {

    }

    @Override
    public void connectException() {
    }


    public static void handlerMessage(Long createTime, String avatar, String nick, String msg, String from) {
        Date date = new Date(createTime);
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("pic", avatar);
        messageMap.put("title", nick);
        messageMap.put("content", msg);
        messageMap.put("time", df.format(date));
        messageMap.put("code", "");
        messageMap.put("id", from);
        if (!TIMClient.getInstance().getList().contains(from)){
            TIMClient.getInstance().getList().add(from);
            TIMClient.getInstance().getMap().put(from,messageMap);
        }else {
            TIMClient.getInstance().getList().remove(from);
            TIMClient.getInstance().getMap().remove(from);
            TIMClient.getInstance().getList().add(from);
            TIMClient.getInstance().getMap().put(from, messageMap);
        }
        // 刷新
        MyHandler handler = (MyHandler) TIMClient.getInstance().get("handler");
        handler.sendEmptyMessage(1);
    }

    public static void handlerMessage(ChatBody body) {
        TIMClient.getInstance().set("content", body);
        if (TIMClient.getInstance().getExtraMap().get(body.getFrom()) == null) {
            TIMClient.getInstance().getExtraList().add(body);
            return;
        }
        MyHandler handler1 = (MyHandler) TIMClient.getInstance().get(body.getFrom());
        handler1.sendEmptyMessage(2);
    }
    public static User getUserById(String userId) {
        final User[] from = new User[1];
        TIMClient.getInstance().getUser().getFriends().forEach(user -> {
            if (user.getUserId() == null) {
                return;
            }
            if (user.getUserId().equals(userId)) {
                from[0] = user;
            }
        });
        return from[0];
    }
}
