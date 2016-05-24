package cn.mandroid.express.model;

/**
 * Created by Administrator on 2015-12-11.
 */
public class Constant {
    public static String HOST_IP = "192.168.1.195";
    public static String API_URL = String.format("http://%s", HOST_IP);

    public static class Code {
        //  教务处密码错误
        public static final int JWC_PSWD_ERROR = 0;
        //  用户不存在
        public static final int NO_USER = 1;
        //  姓名有误
        public static final int JWC_NAME_ERROR = 2;
        //  身份证号码有误
        public static final int JWC_IDCARD_ERROR = 3;
        //  教务处获取cookie有误
        public static final int JWC_COOKIE_ERROR = 4;
        //  密码错误
        public static final int PASSWORD_ERROR = 5;
        //  session错误
        public static final int SESSION_ERROR = 6;
        //  上传失败
        public static final int UPLOAD_FAILED = 7;
        //  获取token失败
        public static final int GET_TOKEN_ERROR = 8;
        //  签到失败
        public static final int SING_IN_ERROR = 9;
        //  任务已删除
        public static final int TASK_IS_DELETE = 10;
        //  任务已被领取
        public static final int TASK_IS_RECEIVED = 11;
        //  任务未被领取
        public static final int TASK_IS_NOT_RECEIVED = 12;
        //  手机号码已存在
        public static final int PHONE_IS_EXIST = 13;
        //
        public static final int TASK_USER_ERROR = 14;
        //  未评价
        public static final int NOT_TO_EVALUTE = 15;
        //  任务状态发生改变
        public static final int TASK_STATUS_CHANGE = 16;
    }
}
