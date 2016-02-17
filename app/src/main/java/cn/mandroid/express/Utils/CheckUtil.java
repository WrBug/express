package cn.mandroid.express.Utils;

import android.text.TextUtils;

import org.androidannotations.annotations.EBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.mandroid.express.Model.Bean.UserBean;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CheckUtil {

    public static boolean userIsInvid(UserBean userBean) {
        return !(TextUtils.isEmpty(userBean.getUsername()) || TextUtils.isEmpty(userBean.getSessionId()));
    }

    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
