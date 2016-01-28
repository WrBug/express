package cn.mandroid.express.Model.RongIMMessage;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.activity.TaskDetailActivity_;
import cn.mandroid.express.Utils.DateUtil;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by Administrator on 2016/1/28 0028.
 */
@ProviderTag(messageContent = TaskInfoMessage.class, showPortrait = false, centerInHorizontal = true)
public class TaskInfoMessageItemProvider extends IContainerItemProvider.MessageProvider<TaskInfoMessage> {
    private Context context;

    @Override
    public void bindView(View view, int i, TaskInfoMessage taskInfoMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Gson gson = new Gson();
        TaskInfoBean info = gson.fromJson(taskInfoMessage.getTaskInfo(), TaskInfoBean.class);
        holder.titleText.setText(taskInfoMessage.getContent());
        holder.whereText.setText(info.getDepository());
        holder.destText.setText(info.getDestination());
        holder.dateText.setText(DateUtil.timeToStrMDHM_ZH(info.getDate() * 1000));
        holder.companyText.setText(info.getExpressCompany());
    }

    @Override
    public Spannable getContentSummary(TaskInfoMessage taskInfoMessage) {
        return new SpannableString(taskInfoMessage.getContent());
    }

    @Override
    public void onItemClick(View view, int i, TaskInfoMessage taskInfoMessage, UIMessage uiMessage) {
        Gson gson = new Gson();
        TaskInfoBean info = gson.fromJson(taskInfoMessage.getTaskInfo(), TaskInfoBean.class);
        TaskDetailActivity_.intent(context).id(info.getId()).taskInfoBean(info).start();
    }

    @Override
    public void onItemLongClick(View view, int i, TaskInfoMessage taskInfoMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.rc_layout_task_info, null);
        ViewHolder holder = new ViewHolder();
        holder.titleText = (TextView) view.findViewById(R.id.titleText);
        holder.whereText = (TextView) view.findViewById(R.id.depositoryText);
        holder.destText = (TextView) view.findViewById(R.id.destinationText);
        holder.dateText = (TextView) view.findViewById(R.id.dateText);
        holder.companyText = (TextView) view.findViewById(R.id.expressCompanyText);
        view.setTag(holder);
        return view;
    }

    class ViewHolder {
        public TextView titleText;
        public TextView whereText;
        public TextView destText;
        public TextView dateText;
        public TextView companyText;
    }
}
