package cn.mandroid.express.Model;

import java.util.List;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Model.Dao.TaskInfoDao;
import cn.mandroid.express.Model.Dao.UserDao;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Administrator on 2015/12/18.
 */
public class DaoManager {
    public static void saveUserInfo(String username, Integer sex, String name, String userAvatar) {
        Realm realm = Realm.getDefaultInstance();
        UserDao dao = new UserDao();
        dao.setUsername(username);
        dao.setSex(sex);
        dao.setName(name);
        dao.setUserAvatar(userAvatar);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(dao);
        realm.commitTransaction();
    }

    public static void saveTaskList(List<TaskInfoBean> list) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(TaskInfoDao.bean2dao(list));
        realm.commitTransaction();
    }

    public static List<TaskInfoBean> getTaskList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TaskInfoDao> taskInfoDaos = realm.where(TaskInfoDao.class).findAll();
        if (taskInfoDaos.isLoaded()) {
            return TaskInfoBean.dao2bean(taskInfoDaos);
        }
        return null;
    }

    public static UserBean getUserInfoByUsername(String username) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UserDao> userDaos = realm.where(UserDao.class).equalTo("username", username).findAll();
        if (userDaos.isLoaded()) {
            if (userDaos.size() > 0) {
                return UserBean.dao2Bean(userDaos.get(0));
            }
        }
        return null;
    }
}
