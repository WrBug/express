package cn.mandroid.express.Model.Dao;

import cn.mandroid.express.Model.Bean.UserBean;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2015/12/18.
 */
public class UserDao extends RealmObject{
    @PrimaryKey
    private String username;
    private Integer sex;
    private String name;
    private String userAvatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public static UserDao bean2dao(UserBean bean){
        UserDao dao=new UserDao();
        dao.setName(bean.getName());
        dao.setSex(bean.getSex());
        dao.setUserAvatar(bean.getAvatarUrl());
        dao.setUsername(bean.getUsername());
        return  dao;
    }
}
