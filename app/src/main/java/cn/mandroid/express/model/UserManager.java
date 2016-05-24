package cn.mandroid.express.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import cn.mandroid.express.model.bean.IntegralDetailBean;
import cn.mandroid.express.model.bean.UserBean;
import cn.mandroid.express.ui.common.Application;
import cn.mandroid.express.utils.FileUtils;
import cn.mandroid.express.utils.MLog;

/**
 * Created by Administrator on 2015-12-13.
 */
@EBean
public class UserManager extends ApiManager {
    private Context context;
    @Bean
    RongImManager rongImManager;
    @App
    Application mApp;
    public UserManager(Context context) {
        super(context);
        this.context = context;
    }

    public void updateUser(String username, final FetchCallBack<UserBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        Ion.with(context).load(Constant.API_URL + "/User/updateUser")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                UserBean userBean = new Gson().fromJson(getData(result), UserBean.class);
                                rongImManager.setUserinfo(userBean);
                                callBack.onSuccess(getCode(result), userBean);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void getUserInfoByUsername(String user, String username, final FetchCallBack<UserBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("user", user);
        map.put("username", username);
        Ion.with(context).load(Constant.API_URL + "/User/getUserInfoByUsername")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                UserBean userBean = new Gson().fromJson(getData(result), UserBean.class);
                                callBack.onSuccess(getCode(result), userBean);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void uploadAvatar(String username, final String name, File file, final FetchCallBack<String> callBack) {
        final TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("name", name);
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
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                UserBean userBean = mApp.getUser();
                                String url = result.get("data").getAsString();
                                userBean.setAvatarUrl(url);
                                mApp.saveUser(userBean);
                                rongImManager.setUserinfo(userBean);
                                callBack.onSuccess(getCode(result), url);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void getToken(String username, final FetchCallBack<String> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        Ion.with(context).load(Constant.API_URL + "/RongIm/getToken")
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                callBack.onSuccess(getCode(result), getData(result).getAsString());
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void getIntegralDetail(String username, final FetchCallBack<IntegralDetailBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        Ion.with(context).load(Constant.API_URL + "/User/getIntegralDetail")
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                Gson gson = new Gson();
                                IntegralDetailBean bean = gson.fromJson(getData(result), IntegralDetailBean.class);
                                callBack.onSuccess(getCode(result), bean);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void signIn(String username, final FetchCallBack<UserBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        Ion.with(context).load(Constant.API_URL + "/User/signIn")
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                Gson gson = new Gson();
                                UserBean userBean = gson.fromJson(getData(result), UserBean.class);
                                callBack.onSuccess(getCode(result), userBean);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void checkPhone(String phone, final FetchCallBack callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("phone", phone);
        Ion.with(context).load(Constant.API_URL + "/User/checkPhone")
                .setMultipartParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                callBack.onSuccess(getCode(result), null);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void getFriendList(final FetchCallBack<List<UserBean>> callBack) {
        Ion.with(context).load(Constant.API_URL + "/User/getFriendsList")
                .setMultipartParameters(getFinalMap(new TreeMap<String, String>()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                List<UserBean> list;
                                Gson gson = new Gson();
                                FileUtils.writeFile(context, FileUtils.FRIEND_LIST, getData(result).toString());
                                list = gson.fromJson(getDataAsJsonArray(result), new TypeToken<List<UserBean>>() {
                                }.getType());
                                callBack.onSuccess(getCode(result), list);
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }
}
