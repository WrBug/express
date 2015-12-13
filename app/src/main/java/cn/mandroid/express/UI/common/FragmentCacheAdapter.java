package cn.mandroid.express.UI.common;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015-12-13.
 */
public class FragmentCacheAdapter extends FragmentPagerAdapter {
    private Activity context;
    private FragmentManager fm;
    private Fragment fragment;
    public FragmentCacheAdapter(Activity context, FragmentManager fm,Fragment fragment) {
        super(fm);
        this.fm=fm;
        this.context =context;
        this.fragment=fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        String fragmentTag = fragment.getTag();
//        if (position == 1) {
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.remove(fragment);
//                ft.add(container.getId(), fragment, fragmentTag);
//                ft.attach(fragment);
//                ft.commit();
//        }
        return fragment;
    }
}
