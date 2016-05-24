package cn.mandroid.express.UI1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.mandroid.express.Model1.Bean1.IntegralDetailItem;
import cn.mandroid.express.R;
import cn.mandroid.express.Utils1.DateUtil;

/**
 * Created by Administrator on 2016/1/21 0021.
 */
public class IntegralDetailListAdapter extends BaseAdapter {
    private List<IntegralDetailItem> list;
    private Context context;
    private LayoutInflater inflater;

    public IntegralDetailListAdapter(Context context,List<IntegralDetailItem> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        IntegralDetailItem item = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_integral_detail, null);
            holder = new ViewHolder();
            holder.countText = (TextView) convertView.findViewById(R.id.countText);
            holder.noteText = (TextView) convertView.findViewById(R.id.noteText);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.countText.setText((item.getType() == 0 ? "+" : "-") + item.getCount());
        holder.noteText.setText(item.getNote());
        holder.dateText.setText(DateUtil.timeToStrYMDHMS_EN(item.getDate()*1000));
        return convertView;
    }

    class ViewHolder {
        public TextView countText;
        public TextView noteText;
        public TextView dateText;
    }
}
