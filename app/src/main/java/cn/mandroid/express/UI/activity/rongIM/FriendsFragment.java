package cn.mandroid.express.UI.activity.rongIM;


import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mandroid.express.Model.Bean.Content;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Constant;
import cn.mandroid.express.Model.FetchCallBack;
import cn.mandroid.express.Model.UserManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.FriendsListAdapter;
import cn.mandroid.express.UI.widget.SideBar;
import cn.mandroid.express.Utils.FileUtils;
import cn.mandroid.express.Utils.PinyinComparator;

@EFragment(R.layout.fragment_friends)
public class FriendsFragment extends BasicChatFragment implements SwipeRefreshLayout.OnRefreshListener {
    @ViewById
    FloatingActionsMenu chatActionsMenu;
    @ViewById
    ListView friendListView;
    @ViewById
    SideBar sideBar;
    @ViewById
    SwipeRefreshLayout swipeRefreshLayout;
    @Bean
    UserManager mUsermanager;

    @AfterViews
    void afterView() {
        TextView mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.list_position, null);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_view_friends_head, null);
        friendListView.addHeaderView(headView);
        swipeRefreshLayout.setOnRefreshListener(this);
        mDialogText.setVisibility(View.INVISIBLE);
        WindowManager mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        sideBar.setTextView(mDialogText);
        getFriendsList();
        getCacheList();
    }

    private void getCacheList() {
        String listStr = FileUtils.readFile(getActivity(), FileUtils.FRIEND_LIST);
        Gson gson = new Gson();
        List<UserBean> list = gson.fromJson(listStr, new TypeToken<List<UserBean>>() {
        }.getType());
        initData(list);
    }

    private void getFriendsList() {
        mUsermanager.getFriendList(new FetchCallBack<List<UserBean>>() {
            @Override
            public void onSuccess(int code, List<UserBean> data) {
                swipeRefreshLayout.setRefreshing(false);
                initData(data);
            }


            @Override
            public void onFail(int code, List<UserBean> userBeen) {
                swipeRefreshLayout.setRefreshing(false);
                if (code == Constant.Code.SESSION_ERROR) {
                    showToast("身份已过期，请重新登录！");
                }
            }

            @Override
            public boolean onError() {
                swipeRefreshLayout.setRefreshing(false);
                return true;
            }
        });
    }

    private void initData(List<UserBean> data) {
        if(data==null||data.size()==0){
            return;
        }
        //初始化数据
        List<Content> list = new ArrayList<Content>();
        for (UserBean been : data) {
            Content content = new Content(been);
            list.add(content);
        }
        //根据a-z进行排序
        Collections.sort(list, new PinyinComparator());
        // 实例化自定义内容适配类
        FriendsListAdapter adapter = new FriendsListAdapter(getActivity(), list);
        // 为listView设置适配
        friendListView.setAdapter(adapter);
        //设置SideBar的ListView内容实现点击a-z中任意一个进行定位
        sideBar.setListView(friendListView);
    }

    @Override
    protected FloatingActionsMenu setActionMenu() {
        return chatActionsMenu;
    }

    @Override
    protected Fragment register() {
        return this;
    }

    @Override
    public void onRefresh() {
        getFriendsList();
    }
}
