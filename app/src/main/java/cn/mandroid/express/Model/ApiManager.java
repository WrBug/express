package cn.mandroid.express.Model;

import android.content.Context;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015-12-11.
 */
public class ApiManager {
    private Context context;
    public ApiManager(Context context){
        this.context=context;
    }
    public Map<String,List<String>> getFinalMap(TreeMap<String ,String> map){
        String time =System.currentTimeMillis()/1000+"";
        map.put("timestamp",time);
        Map<String, List<String>> params = new HashMap<>();
        StringBuilder stringBuilder=new StringBuilder();
        List<String> list=null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list = new ArrayList<>();
            list.add(entry.getValue());
            stringBuilder.append(entry.getValue());
            params.put(entry.getKey(), list);
        }
        list = new ArrayList<>();
        list.add(stringBuilder.toString());
        params.put("code", list);
        return params;
    }
}
