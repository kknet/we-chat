package xyz.mxd.wechat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Map;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.tools.GetImageByUrl;


public class ImageAdapter extends BaseAdapter {
    private Map<String, Map<String, String>> data;
    private List<String> list;
    private Context context;
    private ViewHolder viewHolder;

    public ImageAdapter(Context context, List<String> list, Map<String, Map<String, String>> map) {
        this.context = context;
        this.data = map;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.weixin_item, null);
            viewHolder.img1 = convertView
                    .findViewById(R.id.img1);
            viewHolder.title = convertView
                    .findViewById(R.id.title);
            viewHolder.content = convertView
                    .findViewById(R.id.content);
            viewHolder.time = convertView
                    .findViewById(R.id.time);
            viewHolder.code = convertView
                    .findViewById(R.id.code);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String s = String.valueOf(getItem(position));
        Map<String, String> stringStringMap = data.get(s);
        assert stringStringMap != null;
        String pic = stringStringMap.get("pic");
        GetImageByUrl getImageByUrl = new GetImageByUrl();
        getImageByUrl.setImage(viewHolder.img1, pic);
        String title = stringStringMap.get("title");
        viewHolder.title.setText(title);
        String content = stringStringMap.get("content");
        viewHolder.content.setText(content);
        String time = stringStringMap.get("time");
        viewHolder.time.setText(time);
        String code = stringStringMap.get("code");
//        GetImageByUrl getImageByUrl2 = new GetImageByUrl();
//        getImageByUrl2.setImage(viewHolder.code, code);

        return convertView;
    }

    class ViewHolder {
        ImageView img1;
        public TextView title;
        public TextView content;
        TextView time;
        ImageView code;
    }
}
