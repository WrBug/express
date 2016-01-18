package cn.mandroid.express.UI.activity;

import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.ExpressInfo;
import cn.mandroid.express.Model.Bean.UserBean;
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
    @ViewById(R.id.pull_to_refresh)
    PullToRefreshView pullToRefreshView;
    @ViewById(R.id.list_view)
    LoadMoreListView listView;
    List<ExpressInfo> list = new ArrayList<>();
    ExpressListAdapter adapter;
    @Bean
    TaskManager mTaskManager;

    @AfterViews
    void afterView() {
        pullToRefreshView.setOnRefreshListener(this);
        listView.setOnPushToLoadListenner(this);
        loadInfo();
    }

    private void loadInfo() {
        mTaskManager.getTaskList(new FetchCallBack<List<ExpressInfo>>() {
            @Override
            public void onSuccess(int status, int code, List<ExpressInfo> expressInfos) {
                pullToRefreshView.setRefreshing(false);
                list = expressInfos;
                adapter = new ExpressListAdapter(getActivity(), list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onError() {
                pullToRefreshView.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadInfo();
    }

    @Override
    public void pushToLoad() {
        int index = list.size();
        for (int i = 0; i < 3; i++) {
            ExpressInfo info = new ExpressInfo();
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
            adapter = new ExpressListAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
