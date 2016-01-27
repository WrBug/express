package cn.mandroid.express.Model;

/**
 * Created by Administrator on 2015-12-12.
 */
public interface FetchCallBack<T> {
    void onSuccess(int code, T t);
    void onFail(int code,T t);
    void onError();
}
