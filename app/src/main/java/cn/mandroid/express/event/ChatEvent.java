package cn.mandroid.express.event;

/**
 * Created by Administrator on 2015/12/17.
 */
public class ChatEvent {
    public ChatEvent(Action action) {
        this.action = action;
    }

    public Action action;

    public enum Action {
        CONNECT, DISCONNECT
    }
}
