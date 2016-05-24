package cn.mandroid.express.model.dao;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.mandroid.express.model.bean.TaskInfoBean;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
@Table(name = "Task")
public class TaskDao extends Model {
    @Column(name = "tid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int id;
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE)
    public UserDao user;
    @Column(name = "depository")
    public String depository;
    @Column(name = "destination")
    public String destination;
    @Column(name = "date")
    public long date;
    @Column(name = "expressCompany")
    public String expressCompany;
    @Column(name = "status")
    public int status;

    public void saveBean(TaskInfoBean bean) {
        UserDao uDao = UserDao.bean2dao(bean.getUser());
        uDao.save();
        this.user = uDao;
        this.id = bean.getId();
        this.date = bean.getDate();
        this.depository = bean.getDepository();
        this.destination = bean.getDestination();
        this.expressCompany = bean.getExpressCompany();
        this.status = bean.getStatus();
    }
}
