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
        final User[] from = new User[1];
        TIMClient.getInstance().getUser().getFriends().forEach(user -> {
            if (user.getUserId() == null) {
                return;
            }
            if (user.getUserId().equals(body.getFrom())) {
                from[0] = user;
            }
        });
        if (body.getCreateTime() == null) {
            // 同步回执消息，不用理会
            return;
        }
        handlerMessage(body.getCreateTime(), from[0].getAvatar(), from[0].getNick(), body.getContent(), body.getFrom());
        // 刷新
        MyHandler handler = (MyHandler) TIMClient.getInstance().get("handler");
        handler.sendEmptyMessage(1);
        TIMClient.getInstance().set("content", body);
        if (TIMClient.getInstance().getExtraMap().get(body.getFrom()) == null) {
            TIMClient.getInstance().getExtraList().add(body);
            return;
        }
        MyHandler handler1 = (MyHandler) TIMClient.getInstance().get(body.getFrom());
        handler1.sendEmptyMessage(2);
    }

    @Override
    public void authSuccess(String key) {

        System.out.println(key);
    }

    @Override
    public void getMessageDataAfter(String data) {

    }

    @Override
    public void getOnlineUserIdAfter(String ids) {

        System.out.println(ids);
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
    }
}
