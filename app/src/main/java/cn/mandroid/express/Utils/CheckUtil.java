package cn.mandroid.express.Utils;

import android.text.TextUtils;

import org.androidannotations.annotations.EBean;

import cn.mandroid.express.Model.Bean.UserBean;

/**
 * Created by Administrator on 2015/12/16.
 */
public class CheckUtil {

    public static boolean userIsInvid(UserBean userBean) {
        return !(TextUtils.isEmpty(userBean.getUsername()) || TextUtils.isEmpty(userBean.getSessionId()));
    }
}
