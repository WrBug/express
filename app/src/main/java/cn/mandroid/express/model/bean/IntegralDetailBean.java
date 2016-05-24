package cn.mandroid.express.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/1/21 0021.
 */
public class IntegralDetailBean implements Serializable {
    private int integral;
    private List<IntegralDetailItem> item;

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public List<IntegralDetailItem> getItem() {
        return item;
    }

    public void setItem(List<IntegralDetailItem> item) {
        this.item = item;
    }
}
