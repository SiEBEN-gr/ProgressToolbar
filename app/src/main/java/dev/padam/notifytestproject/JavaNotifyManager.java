package dev.padam.notifytestproject;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;

import org.jetbrains.annotations.NotNull;

/**
 * Created by p.adam on 22/2/2018.
 */

public class JavaNotifyManager implements INotify {

    private final SimpleArrayMap<String, String> registeredActivities
            = new SimpleArrayMap<String, String>();

    @Override
    public void init(@NotNull App app) {
        app.registerActivityLifecycleCallbacks(lifecycleCallback);
    }

    @Override
    public void registerActivity(@NotNull Activity activity) {
        registeredActivities.put(activity.getLocalClassName(), activity.getTitle().toString());

    }

    @Override
    public void changeTitle(@NotNull String message) {
//        Activity activity = lifecycleCallback.getActivity();
    }

    @Override
    public void restoreTitle() {

    }

    private Application.ActivityLifecycleCallbacks lifecycleCallback = new Application.ActivityLifecycleCallbacks() {

        private Activity activity = null;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            this.activity = activity;
        }

        @Override
        public void onActivityStarted(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if(this.activity == activity) this.activity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if(this.activity == activity) this.activity = null;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if(this.activity == activity) this.activity = null;
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if(this.activity == activity) this.activity = null;
        }

        public Activity getActivity() {
            return activity;
        }

    };
}
