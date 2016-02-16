package cn.mandroid.express.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Event.RefreshEvent;
import cn.mandroid.express.Model.Bean.TaskDetailBean;
import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.Dao.TaskInfoDao;
import cn.mandroid.express.Model.DaoManager;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoMessage;
import cn.mandroid.express.Model.RongIMMessage.TaskInfoNoNoticeMessage;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.UI.widget.StepView;
import cn.mandroid.express.Utils.Base64;
import cn.mandroid.express.Utils.Cache;
import cn.mandroid.express.Utils.MLog;
import cn.pedant.sweetalert.SweetAlertDialog;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

@EActivity(R.layout.activity_task_detail)
public class TaskDetailActivity extends BasicActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewById
    StepView stepView1;
    @ViewById
    StepView stepView2;
    @ViewById
    StepView stepView3;
    @ViewById
    StepView stepView4;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
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
    Button evaluteBut;
    @ViewById
    Button problemBut;
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
    @Extra
    TaskInfoBean taskInfoBean;
    String receiveToWatch;
    boolean isReceived;
    TaskDetailBean taskDetailBean;

    @AfterViews
    void afterView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        receiveToWatch = getResources().getString(R.string.receive_to_watch);
        heavyCheck.setClickable(false);
        bigCheck.setClickable(false);
        chatBut.setText("联系" + (taskInfoBean.getUser().getSex() == 1 ? "她" : "他"));
        getTaskDetail();
    }

    private void getTaskDetail(boolean showProgress) {
        if (showProgress) {
            showProgressDialog();
        }
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
            public boolean onError() {
                hideProgressDialog();
                return true;
            }
        });
    }

    private void getTaskDetail() {
        getTaskDetail(true);
    }

    @Click(R.id.chatBut)
    void chatClick() {
        if (rongIMstatus == ConnectionStatus.CONNECTED) {
            if (!Cache.isSendTaskInfoWhenChat.contains(taskInfoBean.getId())) {
                Gson gson = new Gson();
                String taskInfo = gson.toJson(taskInfoBean);
                TaskInfoNoNoticeMessage.sendMessage(taskDetailBean.getUsername(), "该用户正在浏览：", taskInfo, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Cache.isSendTaskInfoWhenChat.add(taskInfoBean.getId());
                    }
                });
            }
            RongIM.getInstance().startPrivateChat(context, taskDetailBean.getUsername(), taskInfoBean.getUser().getName());
        } else {
            switch (rongIMstatus) {
                case NETWORK_UNAVAILABLE:
                    showToast("无可用网络");
                    break;
                case CONNECTING:
                    showToast("请稍后");
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT:
                    LoginActivity_.intent(context).start();
                    break;
            }
        }
    }

    @Click(R.id.receiveTaskBut)
    void receiveTaskButClick() {
        new SweetAlertDialog(context)
                .changeAlertType(SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确认领取，领取后不可取消")
                .setConfirmText("领取")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        receiveTask();
                        sweetAlertDialog.dismiss();
                    }
                })
                .setCancelText("返回")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @Click(R.id.finishTaskBut)
    void finishTaskButClick() {
        new SweetAlertDialog(context)
                .changeAlertType(SweetAlertDialog.WARNING_TYPE)
                .setTitleText("请确认已送达？")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finishTask();
                        sweetAlertDialog.dismiss();
                    }
                })
                .setCancelText("返回")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    private void finishTask() {
        showProgressDialog("请稍后");
        mTaskManager.finishTask(id + "", new FetchCallBack<TaskDetailBean>() {
            @Override
            public void onSuccess(int code, TaskDetailBean bean) {
                hideProgressDialog();
                new SweetAlertDialog(context)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setTitleText("提交成功")
                        .show();
                Gson gson = new Gson();
                String taskInfo = gson.toJson(taskInfoBean);
                TaskInfoMessage.sendMessage(taskDetailBean.getUsername(), "您的快递已送达，请确认", taskInfo, null);
                setData(bean);
            }

            @Override
            public void onFail(int code, TaskDetailBean bean) {
                hideProgressDialog();
                if (code == Constant.Code.SESSION_ERROR) {
                    showToast("身份已过期，请重新登录！");
                    LoginActivity_.intent(context).start();
                } else if (code == Constant.Code.TASK_IS_DELETE) {
                    showToast("该信息不存在!");
                    DaoManager.deleteTaskFormList(id);
                    Intent intent = new Intent();
                    intent.putExtra("action", new RefreshEvent(RefreshEvent.Action.UPDATELOCALTASKLIST));
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (code == Constant.Code.TASK_IS_NOT_RECEIVED) {
                    showToast("数据错误!");
                    getTaskDetail(false);
                }
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                return true;
            }
        });
    }

    private void receiveTask() {
        showProgressDialog("请稍后");
        mTaskManager.receiveTask(id + "", new FetchCallBack<TaskDetailBean>() {
            @Override
            public void onSuccess(int code, TaskDetailBean bean) {
                hideProgressDialog();
                new SweetAlertDialog(context)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setTitleText("您已成功领取该任务")
                        .show();
                if (rongIMstatus == ConnectionStatus.CONNECTED) {
                    Gson gson = new Gson();
                    String taskInfo = gson.toJson(taskInfoBean);
                    TaskInfoMessage.sendMessage(bean.getUsername(), "我领取你的任务啦", taskInfo, null);
                }
                setData(bean);
            }

            @Override
            public void onFail(int code, TaskDetailBean bean) {
                hideProgressDialog();
                if (code == Constant.Code.SESSION_ERROR) {
                    showToast("身份已过期，请重新登录！");
                    LoginActivity_.intent(context).start();
                } else if (code == Constant.Code.TASK_IS_DELETE) {
                    showToast("该信息不存在!");
                    DaoManager.deleteTaskFormList(id);
                    Intent intent = new Intent();
                    intent.putExtra("action", new RefreshEvent(RefreshEvent.Action.UPDATELOCALTASKLIST));
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (code == Constant.Code.TASK_IS_RECEIVED) {
                    showToast("该任务已被其他人领取");
                    setData(bean);
                }
            }

            @Override
            public boolean onError() {
                hideProgressDialog();
                return true;
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
                setViewVisibility(View.VISIBLE, releaseUserButContainer, evaluteBut, deleteTaskBut);
                setViewVisibility(View.GONE, receiveUserButContainer, editTaskBut, closeTaskBut, saveTaskBut, deleteTaskBut);
            } else if (bean.getStatus() == 1) {
                setViewVisibility(View.VISIBLE, releaseUserButContainer, editTaskBut);
                setViewVisibility(View.GONE, receiveUserButContainer, saveTaskBut, closeTaskBut, deleteTaskBut, evaluteBut, problemBut);
            } else if (bean.getStatus() == 0) {
                setViewVisibility(View.VISIBLE, releaseUserButContainer, editTaskBut, closeTaskBut, deleteTaskBut);
                setViewVisibility(View.GONE, receiveUserButContainer, saveTaskBut, evaluteBut, problemBut);
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

    @Override
    public void onRefresh() {
        getTaskDetail(false);
        swipeRefreshLayout.setRefreshing(false);
    }
}
