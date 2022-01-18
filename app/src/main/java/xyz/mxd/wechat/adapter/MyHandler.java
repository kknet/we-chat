package xyz.mxd.wechat.adapter;

import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import org.tim.client.TIMClient;
import org.tim.common.packets.ChatBody;
import java.util.UUID;
import xyz.mxd.wechat.bean.MsgSendStatus;
import xyz.mxd.wechat.bean.MsgType;
import xyz.mxd.wechat.bean.TextMsgBody;

/**
 *  在Android中不可以在线程中直接修改UI，只能借助Handler机制来完成对UI的操作
 */
public class MyHandler extends Handler {

    private BaseAdapter adapter;

    private ChatAdapter chatAdapter;

    public MyHandler(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public MyHandler(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                // 刷新
                adapter.notifyDataSetChanged();
                break;
            case 2:
                ChatBody content = (ChatBody) TIMClient.getInstance().get("content");
                TIMClient.getInstance().remove("content");
                xyz.mxd.wechat.bean.Message mMessgaeText=getBaseReceiveMessage(MsgType.TEXT, content.getFrom(), content.getTo());
                TextMsgBody mTextMsgBody=new TextMsgBody();
                mTextMsgBody.setMessage(content.getContent());
                mMessgaeText.setBody(mTextMsgBody);
                chatAdapter.addData(mMessgaeText);
                break;
        }
    }

    private xyz.mxd.wechat.bean.Message getBaseReceiveMessage(MsgType msgType, String mSenderId, String mTargetId){
        xyz.mxd.wechat.bean.Message mMessgae=new xyz.mxd.wechat.bean.Message();
        mMessgae.setUuid(UUID.randomUUID()+"");
        mMessgae.setSenderId(mSenderId);
        mMessgae.setTargetId(mTargetId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }
}