package com.huotu.lingyunhui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.huotu.lingyunhui.widgets.AlarmDailog;


public class ToastUtils {
    private static AlarmDailog alarmDialog;

    public static void showShortToast(Context context, String showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.setDuration(Toast.LENGTH_SHORT);
        alarmDialog.show();

    }

    public static void showLongToast(Context context, String showMsg) {
        if (null != alarmDialog) {
            alarmDialog = null;
        }
        alarmDialog = new AlarmDailog(context, showMsg);
        alarmDialog.show();
    }

    public static void showMomentToast(final Activity activity, final Context context, final String showMsg) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (null != alarmDialog) {
                    alarmDialog = null;
                }
                alarmDialog = new AlarmDailog(context, showMsg);
                alarmDialog.setDuration(Toast.LENGTH_SHORT);
                alarmDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        alarmDialog.cancel();
                    }
                }, 500);
            }
        });
    }
}
