package dev.padam.notifytestproject

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.util.SimpleArrayMap
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.ProgressBar

/**
* Created by Paul Adam on 16/2/2018.
*/
object NotifyManager {

    private val registeredActivities by lazy { SimpleArrayMap<String, String>() }

    fun init(app: App) {
        app.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    fun registerActivity(activity: Activity) {
        val originalTitle = if (activity is AppCompatActivity) {
            activity.supportActionBar?.title.toString()
        } else {
            activity.actionBar?.title.toString()
        }
        registeredActivities.put(activity.localClassName, originalTitle)
    }

    fun showProgressBar(message: String) {
        val activity = lifecycleCallback.activity ?: return
        if (registeredActivities.containsKey(activity.localClassName)) {

            if (activity is AppCompatActivity) {
                val toolbar = getSupportToolbar(activity)
                toolbar.title = message
                toolbar.addView(ProgressBar(activity),0)
            } else {
                TODO()
            }
        }
    }

    fun restoreActionBar() {
        val activity = lifecycleCallback.activity ?: return
        if (registeredActivities.containsKey(activity.localClassName)) {

            if (activity is AppCompatActivity) {
                val toolbar = getSupportToolbar(activity)
                toolbar.title = registeredActivities.get(activity.localClassName)
                toolbar.removeViewAt(0)
            } else {
                TODO()
            }
        }
    }

    private fun getSupportToolbar(activity: Activity) : Toolbar {
        val layout = activity.findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0) as ViewGroup
        return (0 until layout.childCount)
                .map { layout.getChildAt(it) }
                .filterIsInstance<Toolbar>()
                .first()
    }

    private val lifecycleCallback = object : Application.ActivityLifecycleCallbacks {
        var activity: Activity? = null

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
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
            if (this.activity === activity) this.activity = null
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
            if (this.activity === activity) this.activity = null
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (this.activity === activity) this.activity = null
        }
    }

}