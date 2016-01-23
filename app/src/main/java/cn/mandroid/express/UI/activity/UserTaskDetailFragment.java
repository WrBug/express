package cn.mandroid.express.UI.activity;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.mandroid.express.Model.Bean.ExpressInfo;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.Model.UserManager;
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
    List<ExpressInfo> list;

    @AfterViews
    void afterView() {
        if (TextUtils.isEmpty(username)) {
            user = preferenceHelper.getUser();
            username = user.getUsername();
        }
        getData();
    }

    private void getData() {
        mTaskManager.getTaskListByUsername(username, new FetchCallBack<List<ExpressInfo>>() {
            @Override
            public void onSuccess(int status, int code, List<ExpressInfo> expressInfos) {
                if (status == 1) {
                    list = expressInfos;
                    adapter = new ExpressListAdapter(getActivity(), list);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}
