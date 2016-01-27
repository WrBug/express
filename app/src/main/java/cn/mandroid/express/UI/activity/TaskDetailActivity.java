package cn.mandroid.express.UI.activity;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Model.Bean.TaskDetailBean;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;

@EActivity(R.layout.activity_task_detail)
public class TaskDetailActivity extends BasicActivity {
    @ViewById
    TextView expressCompanyText;
    @ViewById
    TextView courinerNumberText;
    @ViewById
    TextView contactorText;
    @ViewById
    TextView phoneNumberText;
    @ViewById
    CheckBox heavyCheck;
    @ViewById
    CheckBox bigCheck;
    @ViewById
    TextView depositoryText;
    @ViewById
    TextView destinationText;
    @ViewById
    TextView depositoryDetailText;
    @ViewById
    TextView expressPasswordText;
    @ViewById
    TextView remarkText;
    @Bean
    TaskManager mTaskManager;
    @Extra
    int id;
    String receiveToWatch;

    @AfterViews
    void afterView() {
        receiveToWatch = getResources().getString(R.string.receive_to_watch);
        heavyCheck.setClickable(false);
        bigCheck.setClickable(false);
        getTaskDetail();
    }

    private void getTaskDetail() {
        showProgressDialog();
        mTaskManager.getTaskDetail(id + "", new FetchCallBack<TaskDetailBean>() {
            @Override
            public void onSuccess(int code, TaskDetailBean bean) {
                hideProgressDialog();
                setData(bean);
            }

            @Override
            public void onFail(int code, TaskDetailBean bean) {
                hideProgressDialog();
            }

            @Override
            public void onError() {
                hideProgressDialog();
            }
        });
    }

    private void setData(TaskDetailBean bean) {
        expressCompanyText.setText(bean.getExpressCompany());
        courinerNumberText.setText(TextUtils.isEmpty(bean.getCourinerNumber()) ? receiveToWatch : bean.getCourinerNumber());
        contactorText.setText(TextUtils.isEmpty(bean.getContactor()) ? receiveToWatch : bean.getContactor());
        phoneNumberText.setText(TextUtils.isEmpty(bean.getPhoneNumber()) ? receiveToWatch : bean.getPhoneNumber());
        heavyCheck.setChecked(bean.getHeavy() == 1);
        bigCheck.setChecked(bean.getBig() == 1);
        depositoryText.setText(bean.getDepository());
        destinationText.setText(bean.getDestination());
        depositoryDetailText.setText(TextUtils.isEmpty(bean.getDepositoryDetail()) ? receiveToWatch : bean.getDepositoryDetail());
        expressPasswordText.setText(TextUtils.isEmpty(bean.getExpressPassword()) ? receiveToWatch : bean.getExpressPassword());
        remarkText.setText(bean.getRemark());
    }
}
