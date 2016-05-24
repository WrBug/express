package cn.mandroid.express.ui.activity.rongIM;

import android.support.v4.app.Fragment;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import cn.mandroid.express.R;
import cn.mandroid.express.ui.common.BasicHomeFragment;

/**
 * Created by Administrator on 2016/1/29 0029.
 */
public abstract class BasicChatFragment extends BasicHomeFragment implements View.OnClickListener {
    private FloatingActionsMenu actionMenu;
    private FloatingActionButton conversationFB;
    private FloatingActionButton friendsFB;
    public static Fragment lastChatFragment = ChatFragment_.builder().build();

    protected abstract FloatingActionsMenu setActionMenu();

    protected abstract Fragment register();

    @Override
    public void onStart() {
        actionMenu = setActionMenu();
        lastChatFragment = register();
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
                setFragment(ChatFragment_.builder().build());
                setActionBar("最近联系人");
                break;
            case R.id.friendsFB:
                setFragment(FriendsFragment_.builder().build());
                setActionBar("好友");
                break;
        }
    }
}
