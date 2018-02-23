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

    private val registeredActivities by lazy { SimpleArrayMap<String, Pair<String, Int>>() }
    private var showingProgressBar = false

    fun init(app: App) {
        app.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    fun register(activity: Activity) {
        if (activity is AppCompatActivity) {
            registerAppCompatActivity(activity)
        } else {
            registerActivity(activity)
        }
    }

    private fun registerAppCompatActivity(activity: AppCompatActivity) {
        val resourceId = getSupportToolbarId(activity)
        val toolbar = getSupportToolbar(activity, resourceId)
        val originalTitle = toolbar.title.toString()
        registeredActivities.put(activity.localClassName, Pair(originalTitle, resourceId))
    }

    private fun registerActivity(activity: Activity) {
        TODO()
    }

    fun showProgressBar(message: String) {
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return

        if (activity is AppCompatActivity) {
            val (_, resourceId) = registeredActivities.get(activity.localClassName)
            val toolbar = getSupportToolbar(activity, resourceId)
            toolbar.title = message
            toolbar.addView(ProgressBar(activity), 0)
        } else {
            TODO()
        }
        showingProgressBar = true
    }

    fun restoreActionBar() {
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return

        if (activity is AppCompatActivity) {
            val (originalTitle, resourceId) = registeredActivities.get(activity.localClassName)
            val toolbar = getSupportToolbar(activity, resourceId)
            toolbar.title = originalTitle
            toolbar.removeViewAt(0)
        } else {
            TODO()
        }
        showingProgressBar = false
    }

    private fun getSupportToolbar(activity: Activity, resourceId: Int): Toolbar {
        return activity.findViewById(resourceId)
    }

    private fun getSupportToolbarId(activity: Activity): Int {
        val layout = activity.findViewById<ViewGroup>(android.R.id.content)
                .getChildAt(0) as ViewGroup
        return (0 until layout.childCount)
                .map { layout.getChildAt(it) }
                .filterIsInstance<Toolbar>()
                .map { it.id }
                .first()
    }

    private fun isRegisteredActivity(activity: Activity): Boolean {
        return registeredActivities.containsKey(activity.localClassName)
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