package xyz.mxd.wechat.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.tim.common.packets.User;
import java.util.List;
import xyz.mxd.wechat.R;
import xyz.mxd.wechat.tools.GetImageByUrl;


public class SortAdapter extends BaseAdapter {


    private List<User> list;
    private Context mContext;

    public SortAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    public View getView(final int position, View view, ViewGroup arg2) {

        final User user = list.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.contactlist_item, null);
            viewHolder.img = view.findViewById(R.id.img);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.catalog = view.findViewById(R.id.catalog);
            viewHolder.contact_count = view.findViewById(R.id.contact_count);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (position == 0 || position == 1 || position == 2 || position == 3) {
            viewHolder.catalog.setVisibility(View.GONE);
            viewHolder.img.setImageResource(Integer.parseInt(this.list.get(position).getAvatar()));
            viewHolder.name.setText(this.list.get(position).getNick());
            viewHolder.contact_count.setVisibility(View.GONE);
        }else  {
            String catalog = list.get(position).getFirstLetter();
            if(position == getPositionForSection(catalog)){
                viewHolder.catalog.setVisibility(View.VISIBLE);
                viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
            }else{
                viewHolder.catalog.setVisibility(View.GONE);
            }

            GetImageByUrl getImageByUrl = new GetImageByUrl();
            getImageByUrl.setImage(viewHolder.img, list.get(position).getAvatar());
            viewHolder.name.setText(this.list.get(position).getNick());
            if (position == list.size() - 1) {
                viewHolder.contact_count.setVisibility(View.VISIBLE);
                viewHolder.contact_count.setText((list.size() - 4) + "个朋友");
            }else {
                viewHolder.contact_count.setVisibility(View.GONE);
            }
        }
        return view;
    }

    final static class ViewHolder {
        TextView catalog;
        ImageView img;
        TextView name;
        TextView contact_count;
    }

    /**
     * 获取catalog首次出现位置
     */
    private int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            if (!list.get(i).getNick().equals("新的朋友") && !list.get(i).getNick().equals("群聊") &&
                    !list.get(i).getNick().equals("标签") && !list.get(i).getNick().equals("公众号")) {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
        }
        return -1;
    }







}
