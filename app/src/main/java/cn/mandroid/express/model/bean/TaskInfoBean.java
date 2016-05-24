package cn.mandroid.express.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.model.dao.TaskDao;

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

    public static List<TaskInfoBean> dao2bean(List<TaskDao> list) {
        List<TaskInfoBean> beans = new ArrayList<>();
        for (TaskDao dao : list) {
            TaskInfoBean bean = new TaskInfoBean();
            bean.setDate(dao.date);
            bean.setDestination(dao.destination);
            bean.setDepository(dao.depository);
            bean.setExpressCompany(dao.expressCompany);
            bean.setId(dao.id);
            bean.setStatus(dao.status);
            bean.setUser(UserBean.dao2Bean(dao.user));
            beans.add(bean);
        }
        return beans;
    }
}
