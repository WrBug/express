package cn.mandroid.express.Model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import cn.mandroid.express.UI.common.BasicActivity;
import cn.mandroid.express.Utils.MD5;
import cn.mandroid.express.Utils.PreferenceHelper;

/**
 * Created by Administrator on 2015-12-11.
 */
public class ApiManager {
    private Context context;
    PreferenceHelper preferenceHelper;

    public ApiManager(Context context) {
        this.context = context;
        preferenceHelper = PreferenceHelper.instance(context);
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
            if (context instanceof BasicActivity) {
                if (e instanceof TimeoutException) {
                    ((BasicActivity) context).showToast("连接超时,请稍后再试!");
                } else {
                    ((BasicActivity) context).showToast("网络连接失败,请稍后再试!");
                }
            }
            callBack.onError();
            return false;
        }
        return true;
    }

    protected int getCode(JsonObject re) {
        return re.get("code").getAsInt();
    }

    protected JsonObject getDataAsJsonObject(JsonObject result) {
        return getData(result).getAsJsonObject();
    }
    protected JsonArray getDataAsJsonArray(JsonObject result) {
        return getData(result).getAsJsonArray();
    }
    protected JsonElement getData(JsonObject result) {
        return result.get("data");
    }
}
