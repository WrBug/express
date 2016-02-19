package cn.mandroid.express.Model;

/**
 * Created by Administrator on 2015-12-11.
 */
public class Constant {
        public static  String API_URL="http://10.1.51.53";
//    public static String API_URL = "http://192.168.1.195";

    public static class Code {
        public static final int JWC_PSWD_ERROR = 0;
        public static final int NO_USER = 1;
        public static final int JWC_NAME_ERROR = 2;
        public static final int JWC_IDCARD_ERROR = 3;
        public static final int JWC_COOKIE_ERROR = 4;
        public static final int PASSWORD_ERROR = 5;
        public static final int SESSION_ERROR = 6;
        public static final int UPLOAD_FAILED = 7;
        public static final int GET_TOKEN_ERROR = 8;
        public static final int SING_IN_ERROR = 9;
        public static final int TASK_IS_DELETE=10;
        public static final int TASK_IS_RECEIVED=11;
        public static final int TASK_IS_NOT_RECEIVED=12;
        public static final int PHONE_IS_EXIST=13;
        public static final int TASK_USER_ERROR=14;
        public static final int NOT_TO_EVALUTE=15;

    }
}
