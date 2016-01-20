package cn.mandroid.express.UI.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2016/1/20 0020.
 */
public class TimeLineListAdapter extends BaseAdapter {
    LayoutInflater inflater;

    public TimeLineListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return inflater.inflate(R.layout.item_time_line, null);
    }
}
