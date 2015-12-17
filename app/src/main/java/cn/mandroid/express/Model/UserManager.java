package cn.mandroid.express.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.TreeMap;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.UI.common.App;
import cn.mandroid.express.Utils.MLog;

/**
 * Created by Administrator on 2015-12-13.
 */
@EBean
public class UserManager extends ApiManager {
    private Context context;
    @Bean
    RongImManager rongImManager;
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
                                rongImManager.setUserinfo(userBean);
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

    public void uploadAvatar(String username, String sessionId, File file, final FetchCallBack<String> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("sessionId", sessionId);
        map.put("host", Constant.API_URL);
        map.put("fileType", file.getName().substring(file.getName().lastIndexOf('.')));
        Ion.with(context).load(Constant.API_URL + "/User/uploadAvatar")
                .setTimeout(1000 * 60 * 5)
                .uploadProgress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        MLog.i(downloaded + ":" + total);
                    }
                })
                .setMultipartFile("userfile", "image/png", file)
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            if (result.get("status").getAsInt() == 1) {
                                UserBean userBean = App.INSTANCE.getUser();
                                String url = result.get("data").getAsString();
                                userBean.setAvatarUrl(url);
                                App.INSTANCE.saveUser(userBean);
                                rongImManager.setUserinfo(userBean);
                                callBack.onSuccess(1, 1, url);
                            } else {
                                callBack.onSuccess(result.get("status").getAsInt(), result.get("code").getAsInt(), null);
                            }
                        } else {
                            callBack.onError();
                        }
                    }
                });
    }

    public void getToken(String username, String sessionId, final FetchCallBack<String> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("sessionId", sessionId);
        Ion.with(context).load(Constant.API_URL + "/RongIm/getToken")
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            if (result.get("status").getAsInt() == 1) {
                                callBack.onSuccess(1, 1, result.get("data").getAsString());
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
