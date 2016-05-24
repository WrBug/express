package cn.mandroid.express.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.TreeMap;

import cn.mandroid.express.model.bean.TaskInfoBean;
import cn.mandroid.express.model.bean.TaskDetailBean;

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
                        showFailedToast(callBack, result);
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
                        showFailedToast(callBack, result);
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
                        showFailedToast(callBack, result);
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
                        showFailedToast(callBack, result);
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
                        showFailedToast(callBack, result);
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
                        if (object == null || object.isJsonNull()) {
                            showFailedToast(callBack, result);
                        } else {
                            Gson gson = new Gson();
                            TaskDetailBean bean = gson.fromJson(object, TaskDetailBean.class);
                            showFailedToast(callBack, result, bean);
                        }
                    }
                }
            }
        });
    }

    public void finishTask(String id, final FetchCallBack<TaskDetailBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("id", id);
        Ion.with(context).load(Constant.API_URL + "/Task/finishTask").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        Gson gson = new Gson();
                        TaskDetailBean bean = gson.fromJson(getDataAsJsonObject(result), TaskDetailBean.class);
                        callBack.onSuccess(getCode(result), bean);
                    } else {
                        JsonObject object = getDataAsJsonObject(result);
                        if (object == null || object.isJsonNull()) {
                            showFailedToast(callBack, result);
                        } else {
                            Gson gson = new Gson();
                            TaskDetailBean bean = gson.fromJson(object, TaskDetailBean.class);
                            showFailedToast(callBack, result, bean);
                        }
                    }
                }
            }
        });
    }

    public void evaluteTask(String id, String rating, final FetchCallBack<TaskDetailBean> callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("id", id);
        map.put("rating", rating);
        Ion.with(context).load(Constant.API_URL + "/Task/evaluteTask").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (isExceptionNull(e, callBack)) {
                    if (isSuccess(result)) {
                        Gson gson = new Gson();
                        TaskDetailBean bean = gson.fromJson(getDataAsJsonObject(result), TaskDetailBean.class);
                        callBack.onSuccess(getCode(result), bean);
                    } else {
                        JsonObject object = getDataAsJsonObject(result);
                        if (object == null || object.isJsonNull()) {
                            showFailedToast(callBack, result);
                        } else {
                            Gson gson = new Gson();
                            TaskDetailBean bean = gson.fromJson(object, TaskDetailBean.class);
                            showFailedToast(callBack, result, bean);
                        }
                    }
                }
            }
        });
    }

    public void deleteTask(String id, final FetchCallBack callBack) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("id", id);
        Ion.with(context).load(Constant.API_URL + "/Task/deleteTask").setMultipartParameters(getFinalMap(map)).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
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
}
