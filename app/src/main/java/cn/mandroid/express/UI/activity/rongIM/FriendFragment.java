package cn.mandroid.express.UI.activity.rongIM;


import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mandroid.express.Model.Bean.Content;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.adapter.FriendsListAdapter;
import cn.mandroid.express.UI.widget.SideBar;
import cn.mandroid.express.Utils.PinyinComparator;

@EFragment(R.layout.fragment_friends)
public class FriendFragment extends BasicChatFragment {
    @ViewById
    FloatingActionsMenu chatActionsMenu;
    @ViewById
    ListView friendListView;
    @ViewById
    SideBar sideBar;

    @AfterViews
    void afterView() {
        TextView mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.list_position, null);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_view_friends_head, null);
        friendListView.addHeaderView(headView);
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
        //初始化数据
        List<Content> list = new ArrayList<Content>();
        for (int i = 0; i < 10; i++) {
            Content m;
            if (i < 3)
                m = new Content("A", "选项" + i);
            else if (i < 6)
                m = new Content("F", "选项" + i);
            else
                m = new Content("D", "选项" + i);
            list.add(m);
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
}
