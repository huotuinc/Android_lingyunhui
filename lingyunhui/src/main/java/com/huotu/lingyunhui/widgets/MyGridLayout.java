package com.huotu.lingyunhui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.model.NewsColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzbc on 2016/5/24.
 */
public class MyGridLayout extends GridLayout {

    private OnItemClickListener mOnItemClickListener;

    public static interface OnItemClickListener {
        void onItemClick(View v);
    }

    public MyGridLayout(Context context) {
        this(context, null);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private static final int COLUMN_COUNT = 4;

    private void init() {
        //设置一行几列
        setColumnCount(COLUMN_COUNT);

    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setItems(List<NewsColumn> items) {
        // 让gl中添加tv

        for (NewsColumn text : items) {
            addItem(text);
        }

    }
    public List<String> getSortedItems(){
        List<String> result = new ArrayList<>();

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView child = (TextView) getChildAt(i);
            String text =child.getText().toString() ;
            result.add(text );
        }

        return result;
    }

    public void addItem(NewsColumn title) {
        TextView tv = newTV(title.getTitle());
        int margin = 25;
        // 平均分布 = GridLayout在安卓5之前，权重不起作用； 指定宽度 LayoutParamas
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = getResources().getDisplayMetrics().widthPixels
                / getColumnCount() - margin * 2;
        lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
        // 间距
        lp.setMargins(margin, margin, margin, margin);

        addView(tv, lp);

        tv.setOnClickListener(clickListener);
    }

    private TextView newTV(String text) {
        TextView result = new TextView(getContext());
        // 边框 = 背景
        result.setBackgroundResource(R.drawable.bt_press_selector);
        // 字体大小
        result.setTextSize(14);
        // 对齐
        result.setGravity(Gravity.CENTER);
        result.setPadding(5,5,5,5);
        result.setText(text);
        return result;
    }

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener !=null){
                mOnItemClickListener.onItemClick(v);
            }
        }
    };
}
