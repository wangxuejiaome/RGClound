package com.rgcloud.application;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;


public class AppActivityManager {

    private static final String TAG = "AppActivityManager";
    private static Stack<Activity> activityStack;

    private static AppActivityManager instance;

    private AppActivityManager() {
    }

    public static AppActivityManager getActivityManager() {
        if (instance == null) {
            instance = new AppActivityManager();
        }
        return instance;
    }

    public void popActivity(Activity activity) {

        if (activity != null) {
            
            activity.finish();
            activityStack.remove(activity);
       //     Log.e(TAG, "popActivity: 删除"+activity.getClass().getName() +"现在大小"+activityStack.size());
            activity = null;
        }

    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        } else {
            if (!activityStack.empty())
                activity = activityStack.lastElement();
        }
        return activity;
    }

    
    public void pushActivity(Activity activity) {

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
       Log.e(TAG, "pushActivity: 加入"+activity.getClass().getName()+"现在大小"+activityStack.size() );
    }

    public void getAllActivity(){
        Log.e(TAG,"现在全部的activity有:");
        for(int i=0;i<activityStack.size();i++){
            Log.e(TAG,i+" :"+activityStack.get(i).getClass().getName()+activityStack.get(i)+"---->task :"+activityStack.get(i).getTaskId());
        }
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void popActivityByClass(Class cls) {
        for (int i = 0; i < activityStack.size(); i++) {
          Log.i("AppActivityManager", "activityStack中有" + activityStack.size() + "个activity");
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                popActivity(activity);
            }
        }
    }

    public boolean activityIsExist(Class cls) {
        if (activityStack == null) {
            return false;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    public void popAllActivity() {
        while (true) {
            Activity activity = getCurrentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
