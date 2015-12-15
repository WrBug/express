package cn.mandroid.express.UI.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2015/12/15.
 */
@EFragment(R.layout.dialog_bottom)
public class BottomDialog extends BaseDialogFragment {
    @ViewById
    LinearLayout container;
    @FragmentArg
    String[] itemTexts;
    ItemClickCallback itemClickCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void afterView() {
        for (int i = 0; i < itemTexts.length; i++) {
            String itemText = itemTexts[i];
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.sublayout_bottom_dialog_item, null);
            TextView textView = (TextView) v.findViewById(R.id.itemText);
            textView.setId(i + 1);
            textView.setOnClickListener(itemClick());
            textView.setText(itemText);
            container.addView(v);
        }
    }

    public void setItemClickListenner(ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    private View.OnClickListener itemClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickCallback.itemClick(v.getId());
            }
        };
    }

    @Click({R.id.cancleText, R.id.layout})
    void cacle(View view) {
        dismissAllowingStateLoss();
    }

    public interface ItemClickCallback {
        public void itemClick(int id);
    }
}
