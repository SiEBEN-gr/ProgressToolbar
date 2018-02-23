package dev.padam.notifytestproject

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by p.adam on 16/2/2018.
 */
@SuppressLint("StaticFieldLeak") internal object NotifyLifecycleCallbacks
    : Application.ActivityLifecycleCallbacks {

    var activity: Activity? = null
        private set

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        this.activity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        this.activity = null
    }

    override fun onActivityStopped(activity: Activity) {
        if (this.activity == activity) this.activity = null
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        if (this.activity == activity) this.activity = null
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (this.activity == activity) this.activity = null
    }

}