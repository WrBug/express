package cn.mandroid.express.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.cookie.CookieMiddleware;

import org.androidannotations.annotations.EBean;

import java.util.TreeMap;

import cn.mandroid.express.model.bean.UserBean;

/**
 * Created by Administrator on 2015-12-11.
 */
@EBean
public class JwcManager extends ApiManager {
    private Context context;

    public JwcManager(Context context) {
        super(context);
        this.context = context;
    }

    public void login(String studentNum, String password, final FetchCallBack<JsonObject> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", studentNum);
        map.put("password", password);
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/login")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        callBack.onSuccess(getCode(result), result);
                    } else {
                        showFailedToast(callBack, result, result);
                    }
                }
            }
        });
    }

    //    获取cookie
    public void getCookie(String ticket, final FetchCallBack<String> callBack) {
        String url = "http://jxgl.hdu.edu.cn/index.aspx?ticket=" + ticket;
        TreeMap<String, String> map = new TreeMap<>();
        //        必传参数
        map.put("__VIEWSTATE", "/wEPDwUKLTUxMTcwNzgxMGRk");
        preferenceHelper.cleanCookie();
        CookieMiddleware cookieMiddleware = Ion.getDefault(context).getCookieMiddleware();
        cookieMiddleware.clear();
        Ion.with(context).load(url)
                .setBodyParameters(getFinalMap(map))
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e == null) {
                            preferenceHelper.cleanCookie();
                            Multimap multimap = result.getHeaders().getHeaders().getMultiMap();
                            String session = multimap.get("set-cookie").get(0);
                            String route = multimap.get("set-cookie").get(1);
                            callBack.onSuccess(1, session.substring(0, session.indexOf(';') + 1) + route.substring(0, route.indexOf(';')));
                        }
                    }
                });
    }

    public void checkInfo(String username, String name, String idcard, String cookie, final FetchCallBack<Integer> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("name", name);
        map.put("idcard", idcard);
        preferenceHelper.cleanCookie();
        CookieMiddleware cookieMiddleware = Ion.getDefault(context).getCookieMiddleware();
        cookieMiddleware.clear();
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/getInfo")
                .addHeader("cookie", cookie)
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callBack)) {
                            if (isSuccess(result)) {
                                callBack.onSuccess(getCode(result), getData(result) == null || TextUtils.isEmpty(getData(result).getAsString()) ? 0 : getData(result).getAsInt());
                            } else {
                                showFailedToast(callBack, result);
                            }
                        }
                    }
                });
    }

    public void register(String username, String password, String name, String phone, String idcard, String sex, final FetchCallBack<UserBean> callback) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("phone", phone);
        map.put("name", name);
        map.put("sex", sex);
        map.put("idcard", idcard);
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/register")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (isExceptionNull(e, callback)) {
                            if (isSuccess(result)) {
                                Gson gson = new Gson();
                                UserBean userBean = gson.fromJson(getData(result), UserBean.class);
                                callback.onSuccess(getCode(result), userBean);
                            } else {
                                showFailedToast(callback, result);
                            }
                        }
                    }
                });

    }

}
