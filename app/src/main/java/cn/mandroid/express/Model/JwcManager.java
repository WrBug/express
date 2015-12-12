package cn.mandroid.express.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.Multimap;
import com.koushikdutta.async.util.FileCache;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.cookie.CookieMiddleware;

import org.androidannotations.annotations.EBean;
import org.w3c.dom.Document;

import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import cn.mandroid.express.Model.Bean.UserBean;
import cn.mandroid.express.Utils.MLog;
import cn.mandroid.express.Utils.PreferenceHelper;

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
        Map<String, String> map = new HashMap<>();
        map.put("username", studentNum);
        map.put("password", password);
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/login")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    callBack.onSuccess(result.get("status").getAsInt(), result);
                } else {
                    callBack.onError();
                }
            }
        });
    }

    public void getCookie(String ticket, final FetchCallBack<String> callBack) {
        String url = "http://jxgl.hdu.edu.cn/index.aspx?ticket=" + ticket;
        Map<String, String> map = new HashMap<>();
        map.put("__VIEWSTATE", "/wEPDwUKLTUxMTcwNzgxMGRk");
        PreferenceHelper.instance(context).cleanCookie();
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
                            PreferenceHelper.instance(context).cleanCookie();
                            Multimap multimap = result.getHeaders().getHeaders().getMultiMap();
                            String session = multimap.get("set-cookie").get(0);
                            String route = multimap.get("set-cookie").get(1);
                            callBack.onSuccess(1, session.substring(0, session.indexOf(';') + 1) + route.substring(0, route.indexOf(';')));
                        }
                    }
                });
    }

    public void checkInfo(String username, String name, String idcard, String cookie, final FetchCallBack<Integer> callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("name", name);
        map.put("idcard", idcard);
        PreferenceHelper.instance(context).cleanCookie();
        CookieMiddleware cookieMiddleware = Ion.getDefault(context).getCookieMiddleware();
        cookieMiddleware.clear();
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/getInfo")
                .addHeader("cookie", cookie)
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            callBack.onSuccess(result.get("status").getAsInt(), result.get("code").getAsInt());
                        }
                    }
                });
    }

    public void register(String username, String password, String name, String idcard, final FetchCallBack<UserBean> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("name", name);
        map.put("idcard", idcard);
        Ion.with(context).load(Constant.API_URL + "/JwcInfo/register")
                .setBodyParameters(getFinalMap(map))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            Gson gson = new Gson();
                            UserBean userBean = gson.fromJson(result, UserBean.class);
                            callback.onSuccess(result.get("status").getAsInt(), userBean);
                        } else {
                            callback.onError();
                        }
                    }
                });

    }

}
