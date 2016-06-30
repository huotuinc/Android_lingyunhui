package com.huotu.lingyunhui.utils;

import android.app.Activity;

import java.util.Stack;

/**
 *
 * 进行单例
 * <p>
 * 添加、删除当前、删除指定的activity、清空栈、求栈大小
 */
public class AppManager {

    public static Stack<Activity> activityStack = new Stack<Activity>();

    public static AppManager appManager = null;

    private AppManager() {

    }

    public static AppManager getInstance() {
        if (appManager == null) {
            appManager = new AppManager();
        }
        return appManager;
    }

    /**
     * 添加一个activity
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activityStack.add(activity);
    }


    /**
     * 删除指定activity
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass() == activity.getClass()) {
                activity.finish();
                activityStack.remove(activity);
                break;
            }
        }
    }

    /**
     * 删除当前activity
     */
    public static void removeCurrent() {
        Activity lastElement = activityStack.lastElement();
        lastElement.finish();
        activityStack.remove(lastElement);
    }

    /**
     * 清空栈
     */
    public static void clear() {
        for (Activity activity : activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }


    /**
     * 求栈大小
     * @return
     */
    public static int getSize() {
        return activityStack.size();
    }

}
