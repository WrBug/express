package cn.mandroid.express.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.event.RefreshEvent;
import cn.mandroid.express.model.bean.FilterBean;
import cn.mandroid.express.model.bean.TaskInfoBean;
import cn.mandroid.express.model.bean.UserBean;
import cn.mandroid.express.model.DaoManager;
import cn.mandroid.express.model.FetchCallBack;
import cn.mandroid.express.model.TaskManager;
import cn.mandroid.express.R;
import cn.mandroid.express.ui.adapter.TaskListAdapter;
import cn.mandroid.express.ui.common.BasicHomeFragment;
import cn.mandroid.express.ui.dialog.FilterDialog;
import cn.mandroid.express.ui.dialog.SearchDialog;
import cn.mandroid.express.ui.dialog.SearchDialog_;
import cn.mandroid.express.ui.widget.LoadMoreListView;

/**
 * Created by Administrator on 2015/12/17.
 */
@EFragment(R.layout.fragment_center)
public class CenterFragment extends BasicHomeFragment implements PullToRefreshView.OnRefreshListener, LoadMoreListView.PushToLoadListenner {
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
    List<TaskInfoBean> taskInfoBeanList = new ArrayList<>();
    List<TaskInfoBean> cacheList = new ArrayList<>();
    TaskListAdapter adapter;
    @Bean
    TaskManager mTaskManager;
    boolean isRefresh;
    public final int TASK_DETAIL_REQUEST = 1;
    private final int RELEASE_TASK_REQUEST = 2;
    SearchDialog searchDialog;

    @AfterViews
    void afterView() {
        pullToRefreshView.setOnRefreshListener(this);
        listView.setOnPushToLoadListenner(this);
        setAdapter();
        loadInfo();
    }

    public void loadInfo() {
        mTaskManager.getTaskList(new FetchCallBack<List<TaskInfoBean>>() {
            @Override
            public void onSuccess(int code, List<TaskInfoBean> taskInfoBeans) {
                pullToRefreshView.setRefreshing(false);
                setAdapter(taskInfoBeans);
            }

            @Override
            public boolean onFail(int code, List<TaskInfoBean> list) {
                pullToRefreshView.setRefreshing(false);
                return true;
            }

            @Override
            public boolean onError() {
                pullToRefreshView.setRefreshing(false);
                return isRefresh;
            }
        });
    }

    public void setAdapter() {
        List<TaskInfoBean> taskInfoBeans=DaoManager.getTaskList();
        if(taskInfoBeans==null||taskInfoBeans.isEmpty()){
            pullToRefreshView.setRefreshing(true);
            showToast("正在加载数据");
        }
        setAdapter(taskInfoBeans);
    }

    private void setAdapter(List<TaskInfoBean> taskInfoBeans) {
        if (taskInfoBeans == null) {
            return;
        }
        taskInfoBeanList = taskInfoBeans;
        cacheList.clear();
        cacheList.addAll(taskInfoBeans);
        adapter = new TaskListAdapter(this, listView, cacheList);
        listView.setAdapter(adapter);
    }

    @Click({R.id.releaseFB, R.id.searchFB, R.id.filterFB})
    void floatingButtonClick(View view) {
        switch (view.getId()) {
            case R.id.releaseFB:
                ReleaseTaskActivity_.intent(this).startForResult(RELEASE_TASK_REQUEST);
                break;
            case R.id.filterFB:
                setFilter();
                break;
            case R.id.searchFB:
                if (searchDialog == null) {
                    searchDialog = SearchDialog_.builder().build();
                    searchDialog.setOnKeywordChangeListenner(new SearchDialog.KeywordChangeListenner() {
                        @Override
                        public void onChanged(String keyword) {
                            searchKeyword(keyword);
                        }
                    });
                }
                searchDialog.show(getFragmentManager());
                break;
        }
    }

    private void setFilter() {
        FilterDialog filterDialog = new FilterDialog(getActivity(), mPreferenceHelper.getFilter());
        filterDialog.setFilterCallback(new FilterDialog.FilterCallback() {
            @Override
            public void onClick(FilterBean bean) {
                mPreferenceHelper.saveFilter(bean);
                setFilter(bean);
            }
        });
        filterDialog.show();
    }

    private void setFilter(FilterBean bean) {
        int pennding = bean.isPennding() ? 0 : -1;
        int running = bean.isRunning() ? 1 : -1;
        int finish = bean.isFinish() ? 2 : -1;
        int complete = bean.isComplete() ? 3 : -1;
        cacheList.clear();
        for (TaskInfoBean info : taskInfoBeanList) {
            if (info.getStatus() != -1 && (info.getStatus() == pennding || info.getStatus() == running || info.getStatus() == finish || info.getStatus() == complete)) {
                if (TextUtils.isEmpty(bean.getDepo()) || info.getDepository().contains(bean.getDepo())) {
                    if (TextUtils.isEmpty(bean.getDest()) || info.getDestination().contains(bean.getDest())) {
                        cacheList.add(info);
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void searchKeyword(String keyword) {

    }

    @OnActivityResult(TASK_DETAIL_REQUEST)
    void onDetailResult(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            RefreshEvent event = (RefreshEvent) data.getSerializableExtra("action");
            updateEvent(event);
        }
    }

    @OnActivityResult(RELEASE_TASK_REQUEST)
    void onReleaseResult(int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            RefreshEvent event = (RefreshEvent) data.getSerializableExtra("action");
            updateEvent(event);
        }
    }

    public void updateEvent(RefreshEvent event) {
        if (event == null) {
            return;
        }
        switch (event.getAction()) {
            case REFRESHTASKLIST:
                loadInfo();
                break;
            case UPDATELOCALTASKLIST:
                setAdapter();
                break;
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        loadInfo();
    }

    @Override
    public void pushToLoad() {
        int index = taskInfoBeanList.size();
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
            taskInfoBeanList.add(info);
        }
        cacheList.clear();
        cacheList.addAll(taskInfoBeanList);
        if (adapter == null) {
            adapter = new TaskListAdapter(this, listView, cacheList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
