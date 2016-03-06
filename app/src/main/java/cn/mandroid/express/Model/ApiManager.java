package cn.mandroid.express.Model;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

import cn.mandroid.express.Model.SPrefs.PreferenceHelper_;
import cn.mandroid.express.UI.activity.LoginActivity_;
import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.Utils.MD5;
import cn.mandroid.express.Utils.MToast;
import cn.mandroid.express.Model.SPrefs.PreferenceHelper;

/**
 * Created by Administrator on 2015-12-11.
 */
public class ApiManager {
    private Context context;
    PreferenceHelper_ preferenceHelper;

    public ApiManager(Context context) {
        this.context = context;
        preferenceHelper = PreferenceHelper_.getInstance_(context);
    }

    public Map<String, List<String>> getFinalMap(TreeMap<String, String> map) {
        String time = System.currentTimeMillis() / 1000 + "";
        map.put("timestamp", time);
        String sessionId = preferenceHelper.getSessionId();
        if (!TextUtils.isEmpty(sessionId)) {
            map.put("sessionId", sessionId);
        }
        String username = preferenceHelper.getUsername();
        if (!TextUtils.isEmpty(username) && !map.containsKey("username")) {
            map.put("username", username);
        }
        Map<String, List<String>> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list = new ArrayList<>();
            list.add(entry.getValue());
            stringBuilder.append(entry.getValue());
            params.put(entry.getKey(), list);
        }
        list = new ArrayList<>();
        list.add(MD5.encode(stringBuilder.toString()));
        params.put("code", list);
        return params;
    }

    protected boolean isSuccess(JsonObject result) {
        return result.get("status").getAsInt() == 1;
    }

    protected boolean isExceptionNull(Exception e, FetchCallBack callBack) {
        if (e != null) {
            if (callBack.onError() && context instanceof BasicActivity) {
                if (e instanceof TimeoutException) {
                    ((BasicActivity) context).showToast("连接超时,请稍后再试!");
                } else {
                    ((BasicActivity) context).showToast("网络连接失败,请稍后再试!");
                }
            }
            return false;
        }
        return true;
    }

    protected int getCode(JsonObject re) {
        return re.get("code").getAsInt();
    }

    protected JsonObject getDataAsJsonObject(JsonObject result) {
        JsonElement element = getData(result);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element.getAsJsonObject();
    }

    protected JsonArray getDataAsJsonArray(JsonObject result) {
        JsonElement element = getData(result);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return getData(result).getAsJsonArray();
    }

    protected JsonElement getData(JsonObject result) {
        return result.get("data");
    }

    protected void showFailedToast(FetchCallBack callBack, JsonObject result) {
        showFailedToast(callBack, result, null);
    }

    protected void showFailedToast(FetchCallBack callBack, JsonObject result, @Nullable Object o) {
        if (!callBack.onFail(getCode(result), o)) {
            switch (getCode(result)) {
                case Constant.Code.JWC_PSWD_ERROR:
                    MToast.show(context, "教务处密码有误");
                    break;
                case Constant.Code.NO_USER:
                    MToast.show(context, "请填写正确的姓名和身份证");
                    break;
                case Constant.Code.JWC_NAME_ERROR:
                    MToast.show(context, "姓名有误,请重新输入");
                    break;
                case Constant.Code.JWC_IDCARD_ERROR:
                    MToast.show(context, "身份证有误,请重新输入");
                    break;
                case Constant.Code.JWC_COOKIE_ERROR:
                    MToast.show(context, "验证失败,请再试一次");
                    break;
                case Constant.Code.PASSWORD_ERROR:
                    MToast.show(context, "登录密码有误,请重新输入");
                    break;
                case Constant.Code.SESSION_ERROR:
                    MToast.show(context, "身份已过期，请重新登录！");
                    preferenceHelper.cleanUser();
                    LoginActivity_.intent(context).start();
                    break;
                case Constant.Code.TASK_IS_DELETE:
                    MToast.show(context, "该信息不存在!");
                    break;
                case Constant.Code.TASK_IS_RECEIVED:
                    MToast.show(context, "该任务已被其他人领取");
                    break;
                case Constant.Code.TASK_IS_NOT_RECEIVED:
                    MToast.show(context, "数据错误!");
                    break;
                case Constant.Code.PHONE_IS_EXIST:
                    MToast.show(context, "手机号码已存在，请换个手机再试");
                    break;
                case Constant.Code.TASK_USER_ERROR:
                    MToast.show(context, "数据错误!");
                    break;
                case Constant.Code.NOT_TO_EVALUTE:
                    MToast.show(context, "无需评价!");
                    break;
                case Constant.Code.TASK_STATUS_CHANGE:
                    MToast.show(context, "任务状态发生变化");
                    break;
            }
        }
    }
}
