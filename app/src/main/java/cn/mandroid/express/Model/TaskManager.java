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
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        List<TaskInfoBean> list;
                        Gson gson = new Gson();
                        list = gson.fromJson(getDataAsJsonArray(result), new TypeToken<List<TaskInfoBean>>() {
                        }.getType());
                        DaoManager.saveTaskList(list);
                        callBack.onSuccess(getCode(result), list);
                    } else {
                        callBack.onFail(getCode(result), null);
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
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        List<TaskInfoBean> list;
                        Gson gson = new Gson();
                        list = gson.fromJson(getDataAsJsonArray(result), new TypeToken<List<TaskInfoBean>>() {
                        }.getType());
                        callBack.onSuccess(getCode(result), list);
                    } else {
                        callBack.onFail(getCode(result), null);
                    }
                }
            }
        });
    }

    public void getReceiveTaskList(final FetchCallBack<List<TaskInfoBean>> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        Ion.with(context).load(Constant.API_URL + "/Task/getReceiveTaskList").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        List<TaskInfoBean> list;
                        Gson gson = new Gson();
                        list = gson.fromJson(getDataAsJsonArray(result), new TypeToken<List<TaskInfoBean>>() {
                        }.getType());
                        callBack.onSuccess(getCode(result), list);
                    } else {
                        callBack.onFail(getCode(result), null);
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
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        callBack.onSuccess(getCode(result), 1);
                    } else {
                        callBack.onFail(getCode(result), 0);
                    }
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
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        Gson gson = new Gson();
                        TaskDetailBean bean = gson.fromJson(getDataAsJsonObject(result), TaskDetailBean.class);
                        callBack.onSuccess(getCode(result), bean);
                    } else {
                        callBack.onFail(getCode(result), null);
                    }
                }
            }
        });
    }

    public void receiveTask(String id, final FetchCallBack<TaskDetailBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("id", id);
        Ion.with(context).load(Constant.API_URL + "/Task/receiveTask").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        Gson gson = new Gson();
                        TaskDetailBean bean = gson.fromJson(getDataAsJsonObject(result), TaskDetailBean.class);
                        callBack.onSuccess(getCode(result), bean);
                    } else {
                        JsonObject object = getDataAsJsonObject(result);
                        if (object.isJsonNull()) {
                            callBack.onFail(getCode(result), null);
                        } else {
                            Gson gson = new Gson();
                            TaskDetailBean bean = gson.fromJson(object, TaskDetailBean.class);
                            callBack.onFail(getCode(result), bean);
                        }
                    }
                }
            }
        });
    }
}
