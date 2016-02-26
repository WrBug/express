package cn.mandroid.express.UI.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.mandroid.express.R;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
public class EditSpinner extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private EditText editText;
    private ImageView imageView;
    private Context context;
    ListPopupWindow popupWindow;
    private List<String> data;
    private List<String> cacheData = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //    AutoCompleteTextView
    public EditSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.edit_spinner, this);
        initView();
        TypedArray tArray = context.obtainStyledAttributes(attrs,
                R.styleable.EditSpinner);
        editText.setHint(tArray.getString(R.styleable.EditSpinner_hint));
    }

    public void setText(String text) {
        editText.setText(text);
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.autoCompleteTextView);
        imageView = (ImageView) findViewById(R.id.edit_spinner_expand);
        imageView.setOnClickListener(this);
        editText.addTextChangedListener(this);
    }

    private void setAdapter(BaseAdapter adapter) {
        if (popupWindow == null) {
            initPopupWindow();
        }
        popupWindow.setAdapter(adapter);
    }

    public void setItemData(List<String> data) {
        this.data = data;
        cacheData.clear();
        cacheData.addAll(this.data);
        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, cacheData);
        setAdapter(adapter);
    }

    private void initPopupWindow() {
        popupWindow = new ListPopupWindow(context);
        popupWindow.setOnItemClickListener(this);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(editText);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public String getEditString() {
        return editText.getText().toString();
    }

    @Override
    public void onClick(View v) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            cacheData.clear();
            cacheData.addAll(data);
            adapter.notifyDataSetChanged();
            popupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText.setText(cacheData.get(position));
        popupWindow.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String key = s.toString();
        if (!TextUtils.isEmpty(key)) {
            findData(key);
        } else {
            cacheData.clear();
            cacheData.addAll(data);
            adapter.notifyDataSetChanged();
            popupWindow.show();

        }
    }

    private void findData(String key) {
        cacheData.clear();
        for (String s : data) {
            if (s.contains(key)) {
                cacheData.add(s);
            }
        }
        if (cacheData.size() > 0) {
            adapter.notifyDataSetChanged();
            popupWindow.show();
        }
    }
}
