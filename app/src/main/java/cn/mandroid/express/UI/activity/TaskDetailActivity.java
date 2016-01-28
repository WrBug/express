package cn.mandroid.express.UI.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Model.Bean.TaskDetailBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.Dao.TaskInfoDao;
import cn.mandroid.express.Model.DaoManager;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.StepView;
import cn.mandroid.express.Utils.Base64;
import io.rong.imkit.RongIM;

@EActivity(R.layout.activity_task_detail)
public class TaskDetailActivity extends BasicActivity {
    @ViewById
    StepView stepView1;
    @ViewById
    StepView stepView2;
    @ViewById
    StepView stepView3;
    @ViewById
    StepView stepView4;
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
    @ViewById
    View releaseUserButContainer;
    @ViewById
    Button editTaskBut;
    @ViewById
    Button closeTaskBut;
    @ViewById
    Button deleteTaskBut;
    @ViewById
    Button saveTaskBut;
    @ViewById
    View receiveUserButContainer;
    @ViewById
    Button receiveTaskBut;
    @ViewById
    Button chatBut;
    @ViewById
    Button finishTaskBut;
    @Bean
    TaskManager mTaskManager;
    @Extra
    int id;
    String receiveToWatch;
    boolean isReceived;
    TaskDetailBean taskDetailBean;

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
                if (code == Constant.Code.TASK_IS_DELETE) {
                    showToast("该信息不存在!");
                    DaoManager.deleteTaskFormList(id);
                    finish();
                }
            }

            @Override
            public void onError() {
                hideProgressDialog();
            }
        });
    }

    @Click(R.id.chatBut)
    void chatClick() {
        RongIM.getInstance().startPrivateChat(context, taskDetailBean.getUsername(), "");
    }

    @Click(R.id.receiveTaskBut)
    void receiveTaskButClick() {
        showProgressDialog();
        mTaskManager.receiveTask(id + "", new FetchCallBack<TaskDetailBean>() {
            @Override
            public void onSuccess(int code, TaskDetailBean bean) {
                hideProgressDialog();
                showToast("您已领取该任务！");
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
        taskDetailBean = bean;
        setBottomButton(bean);
        initStep(bean.getStatus());
        isReceived = bean.isReceived() && (TextUtils.isEmpty(bean.getReceiveUser()) || (bean.getReceiveUser().equals(mPreferenceHelper.getUsername())) || (bean.getUsername().equals(mPreferenceHelper.getUsername())));
        expressCompanyText.setText(bean.getExpressCompany());
        courinerNumberText.setText(isReceived ? TextUtils.isEmpty(bean.getCourinerNumber()) ? "无" : bean.getCourinerNumber() : receiveToWatch);
        contactorText.setText(isReceived ? TextUtils.isEmpty(bean.getContactor()) ? "无" : bean.getContactor() : receiveToWatch);
        phoneNumberText.setText(isReceived ? TextUtils.isEmpty(bean.getPhoneNumber()) ? "无" : bean.getPhoneNumber() : receiveToWatch);
        heavyCheck.setChecked(bean.getHeavy() == 1);
        bigCheck.setChecked(bean.getBig() == 1);
        depositoryText.setText(bean.getDepository());
        destinationText.setText(bean.getDestination());
        depositoryDetailText.setText(isReceived ? TextUtils.isEmpty(bean.getDepositoryDetail()) ? "无" : bean.getDepositoryDetail() : receiveToWatch);
        expressPasswordText.setText(isReceived ? TextUtils.isEmpty(bean.getExpressPassword()) ? "无" : bean.getExpressPassword() : receiveToWatch);
        remarkText.setText(TextUtils.isEmpty(bean.getRemark()) ? "无" : bean.getRemark());
    }

    private void setBottomButton(TaskDetailBean bean) {
        if (bean.getUsername().equals(mPreferenceHelper.getUsername())) {
            if (bean.getStatus() == 2) {
                setViewVisibility(View.VISIBLE, releaseUserButContainer, deleteTaskBut);
                setViewVisibility(View.GONE, receiveUserButContainer, editTaskBut, closeTaskBut, saveTaskBut);
            } else {
                setViewVisibility(View.VISIBLE, releaseUserButContainer, editTaskBut, closeTaskBut, deleteTaskBut);
                setViewVisibility(View.GONE, receiveUserButContainer, saveTaskBut);
            }
            return;
        }
        if (TextUtils.isEmpty(bean.getReceiveUser())) {
            setViewVisibility(View.VISIBLE, receiveUserButContainer, receiveTaskBut, chatBut);
            setViewVisibility(View.GONE, releaseUserButContainer, finishTaskBut);
        } else {
            if (bean.getReceiveUser().equals(mPreferenceHelper.getUsername())) {
                if (bean.getStatus() == 1) {
                    setViewVisibility(View.VISIBLE, receiveUserButContainer, chatBut, finishTaskBut);
                    setViewVisibility(View.GONE, releaseUserButContainer, receiveTaskBut);
                } else {
                    setViewVisibility(View.VISIBLE, receiveUserButContainer, chatBut);
                    setViewVisibility(View.GONE, releaseUserButContainer, receiveTaskBut, finishTaskBut);
                }
            } else {
                setViewVisibility(View.VISIBLE, receiveUserButContainer, chatBut);
                setViewVisibility(View.GONE, releaseUserButContainer, receiveTaskBut, finishTaskBut);
            }
        }

    }

    private void setViewVisibility(int visibility, View... views) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }

    private void initStep(int status) {
        stepView1.setIsRunning(true);
        switch (status) {
            case 2:
                stepView4.setIsRunning(true);
            case 1:
                stepView3.setIsRunning(true);
            case 0:
                stepView2.setIsRunning(true);
                break;
            default:
                break;
        }
    }
}
