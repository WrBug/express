package cn.mandroid.express.Model;

/**
 * Created by Administrator on 2015-12-12.
 */
public interface  FetchCallBack<T> {
    public void onSuccess(int status,int code,T t);
    public void onError();
}
