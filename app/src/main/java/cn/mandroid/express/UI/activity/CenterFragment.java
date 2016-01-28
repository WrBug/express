package cn.mandroid.express.UI.activity;

import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.DaoManager;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.ExpressListAdapter;
import cn.mandroid.express.UI.common.BasicFragment;
import cn.mandroid.express.UI.widget.LoadMoreListView;

/**
 * Created by Administrator on 2015/12/17.
 */
@EFragment(R.layout.fragment_center)
public class CenterFragment extends BasicFragment implements PullToRefreshView.OnRefreshListener, LoadMoreListView.PushToLoadListenner {
    @ViewById(R.id.pullToRefresh)
    PullToRefreshView pullToRefreshView;
    @ViewById(R.id.list_view)
    LoadMoreListView listView;
    @ViewById(R.id.releaseFB)
    FloatingActionButton releaseFB;
    @ViewById(R.id.filterFB)
    FloatingActionButton filterFB;
    @ViewById(R.id.searchFB)
    FloatingActionButton searchFB;
    List<TaskInfoBean> list = new ArrayList<>();
    ExpressListAdapter adapter;
    @Bean
    TaskManager mTaskManager;

    @AfterViews
    void afterView() {
        pullToRefreshView.setOnRefreshListener(this);
        listView.setOnPushToLoadListenner(this);
        setAdapter(DaoManager.getTaskList());
        loadInfo();
    }

    private void loadInfo() {
        mTaskManager.getTaskList(new FetchCallBack<List<TaskInfoBean>>() {
            @Override
            public void onSuccess(int code, List<TaskInfoBean> taskInfoBeans) {
                pullToRefreshView.setRefreshing(false);
                setAdapter(DaoManager.getTaskList());
            }

            @Override
            public void onFail(int code, List<TaskInfoBean> list) {
                pullToRefreshView.setRefreshing(false);
            }

            @Override
            public void onError() {
                pullToRefreshView.setRefreshing(false);
            }
        });
    }
    private void setAdapter(List<TaskInfoBean> taskInfoBeans) {
        if (taskInfoBeans == null) {
            return;
        }
        list = taskInfoBeans;
        adapter = new ExpressListAdapter(getActivity(),listView, list);
        listView.setAdapter(adapter);
    }

    @Click({R.id.releaseFB, R.id.searchFB, R.id.filterFB})
    void floatingButtonClick(View view) {
        switch (view.getId()) {
            case R.id.releaseFB:
                ReleaseTaskActivity_.intent(getActivity()).start();
                break;
            case R.id.searchFB:
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadInfo();
    }

    @Override
    public void pushToLoad() {
        int index = list.size();
        for (int i = 0; i < 3; i++) {
            TaskInfoBean info = new TaskInfoBean();
            UserBean userBean = new UserBean();
            userBean.setAvatarUrl("http://3.im.guokr.com/PH_23qTq4Yp3Cfg4bhu9lwI2firbTmCFJX_Dpe-vx5MiBAAAwAIAAEpQ.jpg");
            info.setUser(userBean);
            info.setDepository("测试" + (i + index));
            info.setDestination("目的地" + (i + index));
            info.setDate(System.currentTimeMillis());
            info.setExpressCompany("顺丰快递");
            info.setStatus(i % 3);
            list.add(info);
        }
        if (adapter == null) {
            adapter = new ExpressListAdapter(getActivity(),listView, list);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
