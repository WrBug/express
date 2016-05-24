package cn.mandroid.express.UI1.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2016/2/25 0025.
 */
@EFragment(R.layout.dialog_search)
public class SearchDialog extends BaseDialogFragment {
    @ViewById
    EditText keywordEdit;
    private KeywordChangeListenner listenner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @AfterViews
    void afterView() {

    }
    @Click(R.id.container)
    void outClick(){
        dismiss();
    }
    @AfterTextChange(R.id.keywordEdit)
    void textChange(Editable text, TextView hello) {
        if (listenner != null) {
            listenner.onChanged(text.toString());
        }
    }

    public void setOnKeywordChangeListenner(KeywordChangeListenner listenner) {
        this.listenner = listenner;
    }

    public interface KeywordChangeListenner {
        public void onChanged(String keyword);
    }
}
