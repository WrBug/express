package cn.mandroid.express.UI.activity.rongIM;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cn.mandroid.express.R;
import cn.mandroid.express.UI.activity.MainActivity_;
import cn.mandroid.express.UI.common.BasicFragment;

/**
 * Created by Administrator on 2016/1/29 0029.
 */
public abstract class BasicChatFragment extends BasicFragment implements View.OnClickListener {
    private FloatingActionsMenu actionMenu;
    private FloatingActionButton conversationFB;
    private FloatingActionButton friendsFB;
    private MainActivity_ activity;
    public static Fragment lastChatFragment=ChatFragment_.builder().build();
    protected abstract FloatingActionsMenu setActionMenu();
    protected abstract Fragment register();
    @Override
    public void onStart() {
        actionMenu = setActionMenu();
        lastChatFragment=register();
        Context context = getActivity();
        if (context instanceof MainActivity_) {
            activity = (MainActivity_) context;
        }
        setActionButton();
        super.onStart();
    }

    private void setActionButton() {
        conversationFB = (FloatingActionButton) actionMenu.findViewById(R.id.conversationFB);
        friendsFB = (FloatingActionButton) actionMenu.findViewById(R.id.friendsFB);
        conversationFB.setOnClickListener(this);
        friendsFB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.conversationFB:
                activity.setFragment(ChatFragment_.builder().build());
                activity.setActionBarTitle("最近联系人");
                break;
            case R.id.friendsFB:
                activity.setFragment(FriendsFragment_.builder().build());
                activity.setActionBarTitle("好友");
                break;
        }
    }
}
