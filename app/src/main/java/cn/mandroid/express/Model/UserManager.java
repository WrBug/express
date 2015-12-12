package cn.mandroid.express.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EBean;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.mandroid.express.Model.Bean.UserBean;

/**
 * Created by Administrator on 2015-12-13.
 */
@EBean
public class UserManager extends ApiManager {
    private Context context;

    public UserManager(Context context) {
        super(context);
        this.context = context;
    }

    public void updateUser(String username, String sessionId, final FetchCallBack<UserBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("sessionId", sessionId);
        Ion.with(context).load(Constant.API_URL + "/User/updateUser")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            if (result.get("status").getAsInt() == 1) {
                                UserBean userBean = new Gson().fromJson(result.get("data").getAsJsonObject(), UserBean.class);
                                callBack.onSuccess(1, result.get("code").getAsInt(), userBean);
                            } else {
                                callBack.onSuccess(result.get("status").getAsInt(), result.get("code").getAsInt(), null);
                            }
                        } else {
                            callBack.onError();
                        }
                    }
                });
    }
}
