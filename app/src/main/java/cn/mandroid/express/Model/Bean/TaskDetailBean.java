package cn.mandroid.express.Model.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/25 0025.
 */
public class TaskDetailBean implements Serializable {
    int id;
    String username;
    String expressCompany;
    String courinerNumber;
    String phoneNumber;
    String contactor;
    int heavy;
    int big;
    boolean isReceived;
    String depository;
    String depositoryDetail;
    String destination;
    String expressPassword;
    String remark;
    UserBean receiveUser;
    long date;
    long receiveTime;
    long finishTime;
    int status;

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepositoryDetail() {
        return depositoryDetail;
    }

    public void setDepositoryDetail(String depositoryDetail) {
        this.depositoryDetail = depositoryDetail;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getCourinerNumber() {
        return courinerNumber;
    }

    public void setCourinerNumber(String courinerNumber) {
        this.courinerNumber = courinerNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getHeavy() {
        return heavy;
    }

    public void setHeavy(int heavy) {
        this.heavy = heavy;
    }

    public int getBig() {
        return big;
    }

    public void setBig(int big) {
        this.big = big;
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

    public String getExpressPassword() {
        return expressPassword;
    }

    public void setExpressPassword(String expressPassword) {
        this.expressPassword = expressPassword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserBean getReceiveUser() {
        return receiveUser==null?new UserBean():receiveUser;
    }

    public void setReceiveUser(UserBean receiveUser) {
        this.receiveUser = receiveUser;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
