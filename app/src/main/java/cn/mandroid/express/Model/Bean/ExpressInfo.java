package cn.mandroid.express.Model.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class ExpressInfo implements Serializable {
    private UserBean user;
    private String depository;
    private String destination;
    private long date;
    private String expressCompany;
    private int status;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getDepository() {
        return depository;
    }

    public void setDepository(String depository) {
        this.depository = depository;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
