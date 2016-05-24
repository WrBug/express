package cn.mandroid.express.UI1.activity.rongIM;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import cn.mandroid.express.R;
import cn.mandroid.express.UI1.activity.MainActivity;
import cn.mandroid.express.UI1.activity.MainActivity_;
import cn.mandroid.express.UI1.common.BasicActivity;
import cn.mandroid.express.UI1.widget.ActionBar;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class ConversationActivity extends BasicActivity implements RongIMClient.TypingStatusListener {

    /**
     * 目标 Id
     */
    private String mTargetId;
    private String title;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;
    private ActionBar actionBar;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    private final int SET_TEXT_TYPING_TITLE = 1;
    private final int SET_VOICE_TYPING_TITLE = 2;
    private final int SET_TARGETID_TITLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        actionBar = (ActionBar) findViewById(R.id.action_bar);
        Intent intent = getIntent();
        RongIMClient.setTypingStatusListener(this);
        getIntentDate(intent);
    }

    private void getIntentDate(Intent intent) {
        title = intent.getData().getQueryParameter("title");
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getDate().getLastPathSegment();//获得当前会话类型
        actionBar.setTitle(title);
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        enterFragment(mConversationType, mTargetId);
    }

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    protected void onDestroy() {
        if (!MainActivity.isRunning) {
            MainActivity_.intent(context).start();
        }
        super.onDestroy();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    actionBar.setTitle("对方正在输入...");
                    break;
                case SET_VOICE_TYPING_TITLE:
                    actionBar.setTitle("对方正在讲话...");
                    break;
                case SET_TARGETID_TITLE:
                    actionBar.setTitle(title);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
        //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
        if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
            //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
            int count = typingStatusSet.size();
            if (count > 0) {
                Iterator iterator = typingStatusSet.iterator();
                TypingStatus status = (TypingStatus) iterator.next();
                String objectName = status.getTypingContentType();

                MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                //匹配对方正在输入的是文本消息还是语音消息
                if (objectName.equals(textTag.value())) {
                    //显示“对方正在输入”
                    mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                } else if (objectName.equals(voiceTag.value())) {
                    //显示"对方正在讲话"
                    mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                }
            } else {
                //当前会话没有用户正在输入，标题栏仍显示原来标题
                mHandler.sendEmptyMessage(SET_TARGETID_TITLE);
            }
        }
    }
}
