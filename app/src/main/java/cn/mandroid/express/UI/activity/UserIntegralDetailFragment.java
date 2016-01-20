package cn.mandroid.express.UI.activity;

import android.widget.ExpandableListView;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.TimeLineListAdapter;
import cn.mandroid.express.UI.common.BasicFragment;
import cn.mandroid.express.UI.widget.LoadMoreListView;

@EFragment(R.layout.fragment_user_integral_detail)
public class UserIntegralDetailFragment extends BasicFragment {
    //    @ViewById(R.id.pull_to_refresh)
//    PullToRefreshView pullToRefreshView;
    @ViewById(R.id.list_view)
    LoadMoreListView listView;

    @AfterViews
    void afterView() {
        listView.setAdapter(new TimeLineListAdapter(getActivity()));
    }
}
