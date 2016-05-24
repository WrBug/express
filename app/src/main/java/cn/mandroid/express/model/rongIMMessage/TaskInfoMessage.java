package cn.mandroid.express.model.rongIMMessage;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imkit.RLog;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;

/**
 * Created by Administrator on 2016/1/28 0028.
 */
@MessageTag(value = "app:taskinfo", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class TaskInfoMessage extends MessageContent {
    private String content;//消息属性，可随意定义
    private String taskInfo;

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public TaskInfoMessage(String content, String taskInfo) {
        this.content = content;
        this.taskInfo = taskInfo;
    }

    public TaskInfoMessage(byte[] data) {
        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
        }
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("content")) {
                content = jsonObj.optString("content");

            }
            if (jsonObj.has("taskInfo")) {
                taskInfo = jsonObj.optString("taskInfo");
            }
        } catch (JSONException e) {
            RLog.e(this, "JSONException", e.getMessage());
        }
    }

    public TaskInfoMessage(Parcel in) {
        content = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        taskInfo = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
    }

    public static final Creator<TaskInfoMessage> CREATOR = new Creator<TaskInfoMessage>() {

        @Override
        public TaskInfoMessage createFromParcel(Parcel source) {
            return new TaskInfoMessage(source);
        }

        @Override
        public TaskInfoMessage[] newArray(int size) {
            return new TaskInfoMessage[size];
        }
    };

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("content", content);
            jsonObj.put("taskInfo", taskInfo);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);//该类为工具类，对消息中属性进行序列化
        ParcelUtils.writeToParcel(dest, taskInfo);//该类为工具类，对消息中属性进行序列化
    }

    public static void sendMessage(String targetId, String title, String json, RongIMClient.SendMessageCallback callback) {
        RongIM.getInstance().getRongIMClient().sendMessage(Conversation.ConversationType.PRIVATE, targetId, new TaskInfoMessage(title, json), null, null, callback);
    }
}
