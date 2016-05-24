package cn.mandroid.express.UI1.activity.rongIM;

import android.net.Uri;
import android.support.v4.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2015/12/17.
 */
@EFragment(R.layout.fragment_conversation)
public class ChatFragment extends BasicChatFragment {
    @ViewById
    FloatingActionsMenu chatActionsMenu;

    @AfterViews
    void afterView() {
        ConversationListFragment fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);
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
