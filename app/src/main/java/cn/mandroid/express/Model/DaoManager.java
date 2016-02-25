package cn.mandroid.express.Model;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Dao.TaskDao;
import cn.mandroid.express.Model.Dao.UserDao;
import cn.mandroid.express.Utils.MLog;

/**
 * Created by Administrator on 2015/12/18.
 */
public class DaoManager {
    public static void saveUserInfo(String username, Integer sex, String name, String userAvatar) {
        UserDao dao = new UserDao();
        dao.username = username;
        dao.sex = sex;
        dao.name = name;
        dao.userAvatar = userAvatar;
        dao.save();
    }

    public static UserDao getUserDao(String username) {
        return new Select()
                .from(UserDao.class)
                .where("username = ?", username)
                .executeSingle();
    }

    public static void saveTaskList(List<TaskInfoBean> list) {
        ActiveAndroid.beginTransaction();
        try {
            for (TaskInfoBean bean : list) {
                TaskDao taskDao = new TaskDao();
                taskDao.saveBean(bean);
                taskDao.save();
                MLog.i(taskDao.getId());
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static List<TaskInfoBean> getTaskList() {
        List<TaskDao> list = new Select()
                .from(TaskDao.class)
                .orderBy("tid DESC")
                .execute();
        return TaskInfoBean.dao2bean(list);
    }

    public static void deleteTaskFormList(int id) {
        new Delete().from(TaskDao.class).where("id = ?", id).execute();
    }

    public static UserBean getLocalUserInfo(String username) {
        return UserBean.dao2Bean(getUserDao(username));
    }
}
