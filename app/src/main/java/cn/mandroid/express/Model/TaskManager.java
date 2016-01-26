package cn.mandroid.express.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.TreeMap;

import cn.mandroid.express.Model.Bean.TaskInfoBean;
import cn.mandroid.express.Model.Bean.TaskDetailBean;

/**
 * Created by Administrator on 2016/1/18 0018.
 */
@EBean
public class TaskManager extends ApiManager {
    private Context context;

    public TaskManager(Context context) {
        super(context);
        this.context = context;
    }

    public void getTaskList(final FetchCallBack<List<TaskInfoBean>> callBack) {
        Ion.with(context).load(Constant.API_URL + "/Task/getTaskList").asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    if (isSuccess(result)) {
                        List<TaskInfoBean> list;
                        Gson gson = new Gson();
                        list = gson.fromJson(result.get("data").getAsJsonArray(), new TypeToken<List<TaskInfoBean>>() {
                        }.getType());
                        DaoManager.saveTaskList(list);
                        callBack.onSuccess(1, 1, list);
                    }
                }
            }
        });
    }

    public void getTaskListByUsername(final FetchCallBack<List<TaskInfoBean>> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        Ion.with(context).load(Constant.API_URL + "/Task/getTaskListByUsername").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    if (isSuccess(result)) {
                        List<TaskInfoBean> list;
                        Gson gson = new Gson();
                        list = gson.fromJson(result.get("data").getAsJsonArray(), new TypeToken<List<TaskInfoBean>>() {
                        }.getType());
                        callBack.onSuccess(1, 1, list);
                    } else {
                        callBack.onSuccess(result.get("status").getAsInt(), result.get("code").getAsInt(), null);
                    }
                }
            }
        });
    }

    public void releaseTask(TaskDetailBean bean, final FetchCallBack<Integer> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        Gson gson = new Gson();
        String data = gson.toJson(bean);
        map.put("data", data);
        Ion.with(context).load(Constant.API_URL + "/Task/releaseTask").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    if (isSuccess(result)) {
                        callBack.onSuccess(1, 1, 1);
                    } else {
                        callBack.onSuccess(result.get("status").getAsInt(), result.get("code").getAsInt(), 0);
                    }
                } else {
                    callBack.onError();
                }
            }
        });
    }

    public void getTaskDetail(String id, final FetchCallBack<TaskDetailBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("id", id);
        Ion.with(context).load(Constant.API_URL + "/Task/getTaskDetail").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    if (isSuccess(result)) {
                        Gson gson = new Gson();
                        TaskDetailBean bean = gson.fromJson(result.get("data").getAsJsonObject(), TaskDetailBean.class);
                        callBack.onSuccess(1, 1, bean);
                    }
                }
            }
        });
    }
}
