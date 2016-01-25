package cn.mandroid.express.Model.Bean;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Dao.TaskInfoDao;
import cn.mandroid.express.Model.Dao.UserDao;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class TaskInfoBean implements Serializable {
    private int id;
    private UserBean user;
    private String depository;
    private String destination;
    private long date;
    private String expressCompany;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public static List<TaskInfoBean> dao2bean(AbstractList<TaskInfoDao> list) {
        List<TaskInfoBean> beans = new ArrayList<>();
        for (TaskInfoDao dao : list) {
            TaskInfoBean bean = new TaskInfoBean();
            bean.setDate(dao.getDate());
            bean.setDestination(dao.getDestination());
            bean.setDepository(dao.getDepository());
            bean.setExpressCompany(dao.getExpressCompany());
            bean.setId(dao.getId());
            bean.setStatus(dao.getStatus());
            bean.setUser(UserBean.dao2Bean(dao.getUser()));
            beans.add(bean);
        }
        return beans;
    }
}
