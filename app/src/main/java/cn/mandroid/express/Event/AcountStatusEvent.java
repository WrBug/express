package cn.mandroid.express.Event;

import io.rong.imlib.RongIMClient;

/**
 * Created by Administrator on 2015/12/17.
 */
public class AcountStatusEvent {
    private RongIMClient.ConnectionStatusListener.ConnectionStatus status;
    private Action action;
    public enum Action{
        LOGOUT
    }

    public RongIMClient.ConnectionStatusListener.ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        this.status = status;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
