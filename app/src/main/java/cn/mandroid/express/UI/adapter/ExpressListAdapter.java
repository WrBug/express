package cn.mandroid.express.UI.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.mandroid.express.Model.Bean.ExpressInfo;
import cn.mandroid.express.R;
import cn.mandroid.express.Utils.DateUtil;
import cn.mandroid.express.Utils.UiUtil;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class ExpressListAdapter extends BaseAdapter {
    private Context context;
    private List<ExpressInfo> list;
    private LayoutInflater inflater;
    int[] colors = new int[3];

    public ExpressListAdapter(Context context, List<ExpressInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        setColor();
    }

    private void setColor() {
        colors[0] = Color.rgb(0xf1, 0xb1, 0x36);
        colors[1] = Color.rgb(0x66, 0x3d, 0x4e);
        colors[2] = Color.rgb(0xe7, 0x5a, 0x5a);
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
        ExpressInfo info = list.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_express_info, null);
            holder = new ViewHolder();
            holder.container = (LinearLayout) convertView.findViewById(R.id.container);
            holder.userIcoImg = (ImageView) convertView.findViewById(R.id.userIcoImg);
            holder.whereText = (TextView) convertView.findViewById(R.id.whereText);
            holder.destText = (TextView) convertView.findViewById(R.id.destText);
            holder.dateText = (TextView) convertView.findViewById(R.id.dateText);
            holder.companyText = (TextView) convertView.findViewById(R.id.companyText);
            holder.statusText = (TextView) convertView.findViewById(R.id.statusText);
            holder.userIcoImg.setDrawingCacheEnabled(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.container.setBackgroundColor(colors[position % 3]);
        if (TextUtils.isEmpty(info.getUser().getAvatarUrl())) {
            UiUtil.loadImage(context, holder.userIcoImg, info.getUser().getSex() == 1 ? R.drawable.ic_user_default_man : R.drawable.ic_user_default_woman);
        } else {
            UiUtil.loadImage(context, holder.userIcoImg, info.getUser().getAvatarUrl());
        }
        holder.whereText.setText(info.getDepository());
        holder.destText.setText(info.getDestination());
        holder.dateText.setText(DateUtil.timeToStrMDHM_ZH(info.getDate()));
        holder.companyText.setText(info.getExpressCompany());
        setStatus(holder.statusText, info.getStatus());
        return convertView;
    }

    private void setStatus(TextView statusText, int status) {
        switch (status) {
            case 1:
                statusText.setText("待接取");
                statusText.setTextColor(Color.RED);
                break;
            case 2:
                statusText.setText("进行中");
                statusText.setTextColor(Color.BLUE);
                break;
            case 3:
                statusText.setText("已完成");
                statusText.setTextColor(Color.GREEN);
                break;
            default:
                statusText.setText("未知状态");
                statusText.setTextColor(Color.WHITE);
                break;
        }
    }

    class ViewHolder {
        public LinearLayout container;
        public ImageView userIcoImg;
        public TextView whereText;
        public TextView destText;
        public TextView dateText;
        public TextView companyText;
        public TextView statusText;
    }
}
