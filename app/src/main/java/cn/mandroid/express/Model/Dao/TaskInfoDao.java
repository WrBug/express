package cn.mandroid.express.Model.Dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.UserBean;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/1/14 0014.
 */
public class TaskInfoDao extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private UserDao user;
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

    public UserDao getUser() {
        return user;
    }

    public void setUser(UserDao user) {
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

    public static List<TaskInfoDao> bean2dao(List<TaskInfoBean> list) {
        List<TaskInfoDao> daos = new ArrayList<>();
        for (TaskInfoBean bean : list) {
            TaskInfoDao dao = new TaskInfoDao();
            dao.setDate(bean.getDate());
            dao.setDestination(bean.getDestination());
            dao.setDepository(bean.getDepository());
            dao.setExpressCompany(bean.getExpressCompany());
            dao.setId(bean.getId());
            dao.setStatus(bean.getStatus());
            dao.setUser(UserDao.bean2dao(bean.getUser()));
            daos.add(dao);
        }
        return daos;
    }
}
