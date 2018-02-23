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

    //todo remove values from registeredActivities
    private val registeredActivities by lazy { SimpleArrayMap<String, Pair<String, Int>>() }
    private var isNotificationOn = false
    private var isProgressBarOn = false
    private var message = ""

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
        if (isRegisteredActivity(activity)) return
        val resourceId = getSupportToolbarId(activity)
        val toolbar = getSupportToolbar(activity, resourceId)
        val originalTitle = toolbar.title?.toString() ?: activity.title?.toString() ?: ""
        registeredActivities.put(activity.localClassName, Pair(originalTitle, resourceId))
    }

    private fun registerActivity(activity: Activity) {
        TODO()
    }

    fun notify(message: String) {
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return

        addProgressBarAndMessage(activity, message)
        cacheValues(message, true, true)
    }

    private fun addProgressBarAndMessage(activity: Activity, message: String) {
        if (activity is AppCompatActivity) {
            addProgressBarAndMessageToSupportToolbar(activity, message)
        } else {
            addProgressBarAndMessageToToolbar(activity, message)
        }
    }

    private fun addProgressBarAndMessageToSupportToolbar(activity: Activity, message: String) {
        val (_, resourceId) = registeredActivities.get(activity.localClassName)
        val toolbar = getSupportToolbar(activity, resourceId)
        toolbar.title = message
        if (!isProgressBarOn) toolbar.addView(ProgressBar(activity), 0)
    }

    private fun addProgressBarAndMessageToToolbar(activity: Activity, message: String) {
        TODO()
    }

    private fun cacheValues(message: String, isProgressBarOn: Boolean, isNotificationOn: Boolean) {
        this.message = message
        this.isProgressBarOn = isProgressBarOn
        this.isNotificationOn = isNotificationOn
    }

    fun endNotification() {
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return
        if (!isProgressBarOn) return

        restoreToolbar(activity)
        cacheValues("", false, false)
    }

    private fun restoreToolbar(activity: Activity) {
        if (activity is AppCompatActivity) {
            val (originalTitle, resourceId) = registeredActivities.get(activity.localClassName)
            val toolbar = getSupportToolbar(activity, resourceId)
            toolbar.title = originalTitle
            toolbar.removeViewAt(0)
        } else {
            TODO()
        }
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
            private set

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//            this.activity = activity
        }

        override fun onActivityStarted(activity: Activity) {
//            this.activity = activity
        }

        override fun onActivityResumed(activity: Activity) {
//            if (this.activity === activity) return
            this.activity = activity
            if (isRegisteredActivity(activity) && isNotificationOn) {
                isProgressBarOn = false
                notify(message)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            if (isRegisteredActivity(activity) && isNotificationOn) {
                restoreToolbar(activity)
            }
            if (this.activity === activity) this.activity = null
        }

        override fun onActivityStopped(activity: Activity) {
//            if (this.activity === activity) this.activity = null
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
//            if (this.activity === activity) this.activity = null
        }

        override fun onActivityDestroyed(activity: Activity) {
//            if (this.activity === activity) this.activity = null
        }
    }

}