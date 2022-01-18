package xyz.mxd.wechat.util;



import android.content.Context;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;

import org.tim.client.intf.Callback;

/**
 * Toast工具类，统一Toast样式，处理重复显示的问题，处理7.1.x版本crash的问题
 */
public class ToastUtils {


    /**
     * 带有按钮的提示框
     * @param title 标题
     * @param content 内容
     * @param context 上下文
     * @param callback 回调
     */
    public static void showToast(String title, String content, Context context, Callback callback) {

        new XPopup.Builder(context)
                .asConfirm(title, content, callback::success)
                .show();
    }

    /**
     * 无按钮提示框
     * @param context 上下文
     * @param title 标题
     */
    public static void showMsg (Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加载框
     * @param s 内容
     * @param context 上下文
     */
    public static void showLoading(String s, Context context) {

        new XPopup.Builder(context).asLoading(s).show();
    }
}
