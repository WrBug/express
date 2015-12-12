package cn.mandroid.express.UI.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.Event.ExitApp;
import cn.mandroid.express.Model.JwcManager;
import cn.mandroid.express.R;
import cn.mandroid.express.UI.widget.ActionBar;
import cn.mandroid.express.Utils.MToast;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends BasicActivity implements ActionBar.OnHeadImgClickListenner,RadioGroup.OnCheckedChangeListener{
    @Bean
    JwcManager jwcManager;
    @ViewById
    ActionBar actionBar;
    @ViewById
    View fragmentContainer;
    @ViewById
    RadioGroup tabMenu;
    long exitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @AfterViews
    void afterView(){
        setActionBar();
        tabMenu.setOnCheckedChangeListener(this);
    }

    private void setActionBar() {
        actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setLeftImgVisible(View.GONE);
        actionBar.setOnHeadImgClickListenner(this);

    }
    private void setFragment(Fragment fragment)
    {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
    @Override
    public void leftImgClick(ImageView view) {

    }

    @Override
    public void rightImgClick(ImageView view) {

    }
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-exitTime>2000){
            showToast("再按一次退出");
            exitTime=System.currentTimeMillis();
        }else {
            EventBus.getDefault().post(new ExitApp());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rbChat:
                break;
            case R.id.rbCenter:
                break;
            case R.id.rbMy:
                UserInfoFragment fragment=UserInfoFragment_.builder().build();
                setFragment(fragment);
                break;
        }
    }
}
