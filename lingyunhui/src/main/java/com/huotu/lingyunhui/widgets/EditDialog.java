package com.huotu.lingyunhui.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huotu.lingyunhui.R;
import com.huotu.lingyunhui.model.NewsColumn;

import java.util.List;

/**
 * Created by hzbc on 2016/5/24.
 */
public class EditDialog extends Dialog {


    private MyGridLayout glHQ;
    private LinearLayout ll_container;

    protected EditDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public EditDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public EditDialog(Context context) {
        super(context, R.style.EditDialog);
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // 设置顶部对齐
        lp.gravity = Gravity.TOP;
        // 点击背景对话框消失
        setCanceledOnTouchOutside(true);
        View dialoagView = View.inflate(context, R.layout.dialog_edit, null);
        RelativeLayout rl_finish= (RelativeLayout) dialoagView.findViewById(R.id.rl_finish);

       ll_container= (LinearLayout) dialoagView.findViewById(R.id.ll_container);

        rl_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //将dialog打入布局中！！！
        setContentView(dialoagView);
    }

    public void showNewsColumn(List<NewsColumn> lists){
        //动态添加字view到scrollView中
        for (int i=0;i<lists.size();i++){
            LinearLayout childView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.news_item, null);
            glHQ = (MyGridLayout) childView.findViewById(R.id.gl_HQ);
            TextView tv_title= (TextView) childView.findViewById(R.id.tv_title);
            tv_title.setText(lists.get(i).getTitle());
            glHQ.setOnItemClickListener(new MyGridLayout.OnItemClickListener() {
                @Override
                public void onItemClick(View v) {
                    Toast.makeText(getContext(), "" + ((TextView) v).getText(), Toast.LENGTH_LONG).show();
                    dismiss();
                }
            });
            glHQ.setItems(lists.get(i).getList() );
            ll_container.addView(childView);
        }
    }
}
