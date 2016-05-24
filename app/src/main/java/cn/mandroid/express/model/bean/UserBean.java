package cn.mandroid.express.model.bean;

import java.io.Serializable;

import cn.mandroid.express.model.dao.UserDao;

/**
 * Created by Administrator on 2015-12-12.
 */
public class UserBean implements Serializable {
    private String username;
    private String name;
    private String phone;
    private int integral;
    private int releaseCount;
    private int receiveCount;
    private String sessionId;
    private int signInCount;
    private long signInDate;
    private int sex;
    private String avatarUrl;
    private String token;

    public long getSignInDate() {
        return signInDate;
    }

    public void setSignInDate(long signInDate) {
        this.signInDate = signInDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }

    public int getSex() {
        return sex;
    }
    public boolean isMan(){
        return sex==0;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getReleaseCount() {
        return releaseCount;
    }

    public void setReleaseCount(int releaseCount) {
        this.releaseCount = releaseCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public static UserBean dao2Bean(UserDao dao) {
        UserBean userBean=null;
        if (dao != null) {
            userBean = new UserBean();
            userBean.setSex(dao.sex);
            userBean.setUsername(dao.username);
            userBean.setAvatarUrl(dao.userAvatar);
            userBean.setName(dao.name);
        }
        return userBean;
    }

}
