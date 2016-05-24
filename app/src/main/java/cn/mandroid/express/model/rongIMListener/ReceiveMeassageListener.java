package cn.mandroid.express.model.rongIMListener;

import de.greenrobot.event.EventBus;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class ReceiveMeassageListener implements RongIMClient.OnReceiveMessageListener{
    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param i    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int i) {
        EventBus.getDefault().post(message);
        return false;
    }
}
