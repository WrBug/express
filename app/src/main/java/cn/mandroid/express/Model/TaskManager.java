package cn.mandroid.express.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.mandroid.express.Model.Bean.ExpressInfo;
import cn.mandroid.express.Model.Bean.UserBean;

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

    public void getTaskList(final FetchCallBack<List<ExpressInfo>> callBack) {
        Ion.with(context).load(Constant.API_URL + "/task/getTaskList").asJsonArray().setCallback(new FutureCallback<JsonArray>() {
            @Override
            public void onCompleted(Exception e, JsonArray result) {
                if (e == null) {
                   List<ExpressInfo> list;
                    Gson gson = new Gson();
                    list = gson.fromJson(result, new TypeToken<List<ExpressInfo>>(){}.getType());
                    callBack.onSuccess(1, 1,list);
                }
            }
        });
    }
}
