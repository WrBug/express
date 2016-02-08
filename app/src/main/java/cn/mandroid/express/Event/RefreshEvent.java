package cn.mandroid.express.Event;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/4.
 */
public class RefreshEvent implements Serializable{
    private Action action;

    public RefreshEvent(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        REFRESHTASKLIST, UPDATELOCALTASKLIST
    }
}
