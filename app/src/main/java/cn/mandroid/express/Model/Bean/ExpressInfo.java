package cn.mandroid.express.Model.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class ExpressInfo implements Serializable {
    private UserBean user;
    private String where;
    private String dest;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
