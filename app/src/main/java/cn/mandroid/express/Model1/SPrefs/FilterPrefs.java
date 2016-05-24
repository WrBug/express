package cn.mandroid.express.Model1.sPrefs;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface FilterPrefs {
    boolean pennding();
    boolean running();
    boolean finish();
    boolean complete();
    String depo();
    String dest();
}
