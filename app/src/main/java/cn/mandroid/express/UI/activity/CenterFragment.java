package cn.mandroid.express.UI.activity;

import android.widget.ListView;

import com.yalantis.phoenix.PullToRefreshView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.ExpressInfo;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.ExpressListAdapter;
import cn.mandroid.express.UI.common.BasicFragment;

/**
 * Created by Administrator on 2015/12/17.
 */
@EFragment(R.layout.fragment_center)
public class CenterFragment extends BasicFragment implements PullToRefreshView.OnRefreshListener {
    @ViewById(R.id.pull_to_refresh)
    PullToRefreshView pullToRefreshView;
    @ViewById(R.id.list_view)
    ListView listView;
    List<ExpressInfo> list = new ArrayList<>();
    ExpressListAdapter adapter;

    @AfterViews
    void afterView() {
        pullToRefreshView.setOnRefreshListener(this);
        loadInfo();
    }

    private void loadInfo() {
        for (int i = 0; i < 10; i++) {
            ExpressInfo info = new ExpressInfo();
            UserBean userBean = new UserBean();
            userBean.setAvatarUrl("");
            info.setUser(userBean);
            info.setWhere("测试" + i);
            info.setDest("目的地" + i);
            list.add(info);
        }
        adapter = new ExpressListAdapter(getActivity(), list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        int index = list.size();
        for (int i = 0; i < 3; i++) {
            ExpressInfo info = new ExpressInfo();
            UserBean userBean = new UserBean();
            userBean.setAvatarUrl("");
            info.setUser(userBean);
            info.setWhere("测试" + i + index);
            info.setDest("目的地" + i + index);
            list.add(info);
        }
        if (adapter == null) {
            adapter = new ExpressListAdapter(getActivity(), list);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        pullToRefreshView.setRefreshing(false);
    }
}
