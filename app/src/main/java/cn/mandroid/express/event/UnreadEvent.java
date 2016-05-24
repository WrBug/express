package cn.mandroid.express.event;

/**
 * Created by Administrator on 2016/1/19 0019.
 */
public class UnreadEvent {
    private int count;

    public UnreadEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
