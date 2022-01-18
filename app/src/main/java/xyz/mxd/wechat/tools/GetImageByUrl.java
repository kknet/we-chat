package xyz.mxd.wechat.tools;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据图片url路径获取图片
 *
 *
 *
 */
public class GetImageByUrl {

    private PicHandler pic_hdl;
    private ImageView imgView;
    private String url;

    /**
     * 通过图片url路径获取图片并显示到对应控件上
     *
     *
     *
     */
    public void setImage(ImageView imgView, String url) {
        this.url = url;
        this.imgView = imgView;
        pic_hdl = new PicHandler();
        Thread t = new LoadPicThread();
        t.start();
    }

    class LoadPicThread extends Thread {
        @Override
        public void run() {
            Bitmap img = getUrlImage(url);
            Message msg = pic_hdl.obtainMessage();
            msg.what = 0;
            msg.obj = img;
            pic_hdl.sendMessage(msg);
        }
    }

    /**
     * 异步线程请求到的图片数据，利用Handler，在主线程中显示
     */
     class PicHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bitmap myimg = (Bitmap) msg.obj;
            imgView.setImageBitmap(myimg);
        }

    }

    private Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            URL picurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) picurl
                    .openConnection();
            conn.setConnectTimeout(6000);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.connect();
            if (conn.getResponseCode() == 200){     // if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                img = BitmapFactory.decodeStream(is);
                is.close();
                return img;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
