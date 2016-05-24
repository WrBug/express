package cn.mandroid.express.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.mandroid.express.model.bean.TaskInfoBean;
import cn.mandroid.express.R;
import cn.mandroid.express.ui.activity.CenterFragment;
import cn.mandroid.express.ui.activity.TaskDetailActivity_;
import cn.mandroid.express.utils.DateUtil;
import cn.mandroid.express.utils.UiUtil;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class TaskListAdapter extends BaseAdapter implements OnItemClickListener {
    private Context context;
    private List<TaskInfoBean> list;
    private LayoutInflater inflater;
    private CenterFragment fragment;
    int[] colors = new int[3];
    private ListView listView;

    public TaskListAdapter(CenterFragment fragment, ListView listView, List<TaskInfoBean> list) {
        this(fragment.getActivity(), listView, list);
        this.fragment = fragment;
    }

    public TaskListAdapter(Context context, ListView listView, List<TaskInfoBean> list) {
        this.context = context;
        this.list = list;
        this.listView = listView;
        this.listView.setOnItemClickListener(this);
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
        TaskInfoBean info = list.get(position);
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
        if (info.getUser() != null) {
            if (TextUtils.isEmpty(info.getUser().getAvatarUrl())) {
                UiUtil.loadImage(context, holder.userIcoImg, info.getUser().isMan() ? R.drawable.ic_user_default_man : R.drawable.ic_user_default_woman);
            } else {
                UiUtil.loadImage(context, holder.userIcoImg, info.getUser().getAvatarUrl());
            }
        }
        holder.whereText.setText(info.getDepository());
        holder.destText.setText(info.getDestination());
        holder.dateText.setText(DateUtil.timeToStrMDHM_ZH(info.getDate() * 1000));
        holder.companyText.setText(info.getExpressCompany());
        setStatus(holder.statusText, info.getStatus());
        return convertView;
    }

    private void setStatus(TextView statusText, int status) {
        switch (status) {
            case 0:
                statusText.setText("待接取");
                statusText.setTextColor(Color.RED);
                break;
            case 1:
                statusText.setText("进行中");
                statusText.setTextColor(Color.BLUE);
                break;
            case 2:
                statusText.setText("已完成");
                statusText.setTextColor(Color.GREEN);
                break;
            case 3:
                statusText.setText("已结束");
                statusText.setTextColor(Color.WHITE);
                break;
            default:
                statusText.setText("未知状态");
                statusText.setTextColor(Color.WHITE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (fragment == null) {
            TaskDetailActivity_.intent(context).id(list.get(position).getId()).taskInfoBean(list.get(position)).start();
        } else {
            TaskDetailActivity_.intent(fragment).id(list.get(position).getId()).taskInfoBean(list.get(position)).startForResult(fragment.TASK_DETAIL_REQUEST);
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
