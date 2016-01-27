package cn.mandroid.express.UI.activity.user;


import android.text.TextUtils;

import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.ExpressListAdapter;
import cn.mandroid.express.UI.common.BasicFragment;
import cn.mandroid.express.UI.widget.LoadMoreListView;

@EFragment(R.layout.fragment_user_task_detail)
public class UserTaskDetailFragment extends BasicFragment {
    @ViewById(R.id.pullToRefresh)
    PullToRefreshView pullToRefreshView;
    @ViewById(R.id.list_view)
    LoadMoreListView listView;
    ExpressListAdapter adapter;
    @FragmentArg
    String username;
    @Bean
    TaskManager mTaskManager;
    UserBean user;
    List<TaskInfoBean> list;
    @AfterViews
    void afterView() {
        if (TextUtils.isEmpty(username)) {
            user = preferenceHelper.getUser();
            username = user.getUsername();
        }
        getData();
    }

    private void getData() {
        showProgressDialog();
        mTaskManager.getTaskListByUsername(new FetchCallBack<List<TaskInfoBean>>() {
            @Override
            public void onSuccess(int code, List<TaskInfoBean> taskInfoBeans) {
                hideProgressDialog();
                    list = taskInfoBeans;
                    adapter = new ExpressListAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
            }

            @Override
            public void onFail(int code, List<TaskInfoBean> list) {
                hideProgressDialog();
            }

            @Override
            public void onError() {
                hideProgressDialog();
            }
        });
    }
}