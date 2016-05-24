package cn.mandroid.express.Model1.Dao1;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import cn.mandroid.express.Model1.Bean1.UserBean;
import cn.mandroid.express.Model1.DaoManager;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
@Table(name = "User")
public class UserDao extends Model {
    @Column(name = "username", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String username;
    @Column(name = "sex")
    public Integer sex;
    @Column(name = "name")
    public String name;
    @Column(name = "userAvatar")
    public String userAvatar;

    public static UserDao bean2dao(UserBean bean) {
        UserDao dao = DaoManager.getUserDao(bean.getUsername());
        if (dao == null) {
            dao = new UserDao();
        }
        dao.name = bean.getName();
        dao.sex = bean.getSex();
        dao.userAvatar = bean.getAvatarUrl();
        dao.username = bean.getUsername();
        return dao;
    }
}
