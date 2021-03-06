package cn.mandroid.express.ui.activity.rongIM;

import android.os.Bundle;

import cn.mandroid.express.R;
import cn.mandroid.express.ui.activity.MainActivity;
import cn.mandroid.express.ui.activity.MainActivity_;
import cn.mandroid.express.ui.common.BasicActivity;
import cn.mandroid.express.ui.widget.ActionBar;
import io.rong.imlib.model.Conversation;

public class PublicServiceActivity extends BasicActivity {

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
        setContentView(R.layout.activity_public_service);
//        actionBar = (ActionBar) findViewById(R.id.action_bar);
//        Intent intent = getIntent();
//        getIntentDate(intent);
    }

//    private void getIntentDate(Intent intent) {
//        title = intent.getData().getQueryParameter("title");
//        mTargetId = intent.getData().getQueryParameter("targetId");
//        mTargetIds = intent.getData().getQueryParameter("targetIds");
//        //intent.getDate().getLastPathSegment();//获得当前会话类型
//        actionBar.setTitle(title);
//        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
//        enterFragment(mConversationType, mTargetId);
//    }
//
//    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
//        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
//
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
//                .appendQueryParameter("targetId", mTargetId).build();
//
//        fragment.setUri(uri);
//    }

    @Override
    protected void onDestroy() {
        if (!MainActivity.isRunning) {
            MainActivity_.intent(context).start();
        }
        super.onDestroy();
    }
}
