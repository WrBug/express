package cn.mandroid.express.Model1;

/**
 * Created by Administrator on 2015-12-12.
 */
public interface FetchCallBack<T> {
    void onSuccess(int code, T t);
//    true拦截事件
    boolean onFail(int code,T t);
    boolean onError();
}
