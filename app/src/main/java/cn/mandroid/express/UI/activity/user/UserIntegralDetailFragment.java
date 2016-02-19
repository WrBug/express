package cn.mandroid.express.UI.activity.user;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.IntegralDetailBean;
import cn.mandroid.express.Model.Bean.IntegralDetailItem;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.IntegralDetailListAdapter;
import cn.mandroid.express.UI.common.BasicFragment;
import cn.mandroid.express.UI.dialog.ProgressDialog;
import cn.mandroid.express.UI.widget.LoadMoreListView;

@EFragment(R.layout.fragment_user_integral_detail)
public class UserIntegralDetailFragment extends BasicFragment {
    @Bean
    UserManager mUserManager;
    @ViewById
    TextView integralText;
    @ViewById
    TextView allTab;
    @ViewById
    TextView getTab;
    @ViewById
    TextView useTab;
    @ViewById
    ViewPager viewPager;
    List<View> viewLists;

    @AfterViews
    void afterView() {
        getData();
    }

    private void getData() {
        showProgressDialog();
        UserBean userBean = preferenceHelper.getUser();
        integralText.setText(userBean.getIntegral() + "");
        mUserManager.getIntegralDetail(userBean.getUsername(), new FetchCallBack<IntegralDetailBean>() {
            @Override
            public void onSuccess(int code, IntegralDetailBean integralDetailBean) {
                hideProgressDialog();
                setData(integralDetailBean);
          }

        @Override
        public boolean onFail ( int code, IntegralDetailBean integralDetailBean) {
            hideProgressDialog();
            return false;
        }
            @Override
            public boolean onError () {
                hideProgressDialog();
                return true;
            }
        });
    }

    private void initViewPager() {
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTabColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        setTabColor(0);
    }


    @Click(R.id.backImg)
    void backClick() {
        getActivity().finish();
    }

    @Click({R.id.allTab, R.id.getTab, R.id.useTab})
    void tabClick(View view) {
        switch (view.getId()) {
            case R.id.allTab:
                viewPager.setCurrentItem(0);
//                setTabColor(0);
                break;
            case R.id.getTab:
                viewPager.setCurrentItem(1);
//                setTabColor(1);
                break;
            case R.id.useTab:
                viewPager.setCurrentItem(2);
//                setTabColor(2);
                break;
        }
    }

    private void setTabColor(int t) {
        allTab.setTextColor(Color.WHITE);
        getTab.setTextColor(Color.WHITE);
        useTab.setTextColor(Color.WHITE);
        switch (t) {
            case 0:
                allTab.setTextColor(getResources().getColor(R.color.ashy_bg));
                break;
            case 1:
                getTab.setTextColor(getResources().getColor(R.color.ashy_bg));
                break;
            case 2:
                useTab.setTextColor(getResources().getColor(R.color.ashy_bg));
                break;
        }
    }

    private void setData(IntegralDetailBean integralDetailBean) {
        viewLists = new ArrayList<>();
        integralText.setText(integralDetailBean.getIntegral() + "");
        List<IntegralDetailItem> getList = new ArrayList<>();
        List<IntegralDetailItem> useList = new ArrayList<>();
        List<IntegralDetailItem> allList = integralDetailBean.getItem();
        for (IntegralDetailItem item : allList) {
            if (item.getType() == 0) {
                getList.add(item);
            } else if (item.getType() == 1) {
                useList.add(item);
            }
        }
        for (int i = 0; i < 3; i++) {
            IntegralDetailListAdapter adapter;
            View view;
            LoadMoreListView listView;
            switch (i) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.view_pager_integral_detail_all, null);
                    listView = (LoadMoreListView) view.findViewById(R.id.list_view);
                    adapter = new IntegralDetailListAdapter(getActivity(), allList);
                    listView.setAdapter(adapter);
                    viewLists.add(view);
                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.view_pager_integral_detail_get, null);
                    listView = (LoadMoreListView) view.findViewById(R.id.list_view);
                    adapter = new IntegralDetailListAdapter(getActivity(), getList);
                    listView.setAdapter(adapter);
                    viewLists.add(view);

                    break;
                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.view_pager_integral_detail_use, null);
                    listView = (LoadMoreListView) view.findViewById(R.id.list_view);
                    adapter = new IntegralDetailListAdapter(getActivity(), useList);
                    listView.setAdapter(adapter);
                    viewLists.add(view);
                    break;
            }
        }
        initViewPager();
    }

    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View view, int position, Object object)                       //销毁Item
        {
            ((ViewPager) view).removeView(viewLists.get(position));
        }

        @Override
        public Object instantiateItem(View view, int position)                                //实例化Item
        {
            ((ViewPager) view).addView(viewLists.get(position), 0);
            return viewLists.get(position);
        }
    };
}
