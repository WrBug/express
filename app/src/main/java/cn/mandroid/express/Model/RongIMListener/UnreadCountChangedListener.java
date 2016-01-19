package cn.mandroid.express.Model.RongIMListener;

import cn.mandroid.express.Event.UnreadEvent;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class UnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener{
    @Override
    public void onMessageIncreased(int i) {
        EventBus.getDefault().post(new UnreadEvent(i));
    }
}
