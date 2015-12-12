package cn.mandroid.express.Model;

import android.content.Context;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-11.
 */
public class ApiManager {
    private Context context;
    public ApiManager(Context context){
        this.context=context;
    }
    public Map<String,List<String>> getFinalMap(Map<String ,String> map){
        String time =System.currentTimeMillis()/1000+"";
        map.put("timestamp",time);
        Map<String, List<String>> params = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<String> list = new ArrayList<>();
            list.add(entry.getValue());
            params.put(entry.getKey(), list);
        }
        return params;
    }
}
