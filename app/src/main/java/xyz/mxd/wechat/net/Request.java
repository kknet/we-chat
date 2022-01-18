package xyz.mxd.wechat.net;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class Request {

    private static final String uri = "http://mixiaodong.xyz:8080"; //10.0.2.2    https://mixiaodong.xyz

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    public static JSONObject get(Map<String, String> arg, String api) {
        JSONObject json = new JSONObject();
        arg.forEach((s1, s2) ->{
            try {
                json.put(s1, URLEncoder.encode(s2, StandardCharsets.UTF_8.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String jsonstr = json.toString();
        return connect(METHOD_GET, api, jsonstr);
    }


    public static JSONObject post(Map<String, String> arg, String api) {
        JSONObject json = new JSONObject();
        arg.forEach((s1, s2) ->{
            try {
                json.put(s1, URLEncoder.encode(s2, StandardCharsets.UTF_8.toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String jsonstr = json.toString();
        return connect(METHOD_POST, api, jsonstr);
    }

    private static JSONObject connect(String method, String api, String arg) {
        HttpURLConnection urlConnection = null;
        URL url;
        JSONObject rjson = null;
        try {
            url = new URL(
                    uri + api);
            urlConnection = (HttpURLConnection) url.openConnection();// 打开http连接
            urlConnection.setConnectTimeout(10000);// 连接的超时时间
            urlConnection.setUseCaches(false);// 不使用缓存
            HttpURLConnection.setFollowRedirects(false);   // 是否系统自动处理重定向
            urlConnection.setInstanceFollowRedirects(true);// 是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
            urlConnection.setReadTimeout(10000);// 响应的超时时间
            urlConnection.setDoInput(true);// 设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);// 设置这个连接是否可以输出数据
            urlConnection.setRequestMethod(method);// 设置请求的方式
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");// 设置消息的类型
            urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            // ------------字符流写入数据------------
            OutputStream out = urlConnection.getOutputStream();// 输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));// 创建字符流对象并用高效缓冲流包装它，便获得最高的效率,发送的是字符串推荐用字符流，其它数据就用字节流
            bw.write(arg);// 把json字符串写入缓冲区中
            bw.flush();// 刷新缓冲区，把数据发送出去，这步很重要
            out.close();
            bw.close();// 使用完关闭
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {// 得到服务端的返回码是否连接成功
                // ------------字符流读取服务端返回的数据------------
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str;
                StringBuilder buffer = new StringBuilder();
                while ((str = br.readLine()) != null) {// BufferedReader特有功能，一次读取一行数据
                    buffer.append(str);
                }
                in.close();
                br.close();
                rjson = new JSONObject(buffer.toString());


            }else {
                Log.e("connect", "返回状态码：" + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();// 使用完关闭TCP连接，释放资源
            }
        }
        return rjson;
    }
}
