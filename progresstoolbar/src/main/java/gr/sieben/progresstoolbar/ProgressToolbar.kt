package gr.sieben.progresstoolbar

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
object ProgressToolbar {

    private val registeredActivities by lazy { SimpleArrayMap<String, Pair<String, Int>>() }
    private var isNotificationOn = false
    private var isProgressBarOn = false
    private var message = ""

    @JvmStatic fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    @JvmStatic fun register(activity: AppCompatActivity) {
        if (isRegisteredActivity(activity)) return
        val resourceId = getSupportToolbarId(activity)
        val toolbar = getSupportToolbar(activity, resourceId)
        val originalTitle = toolbar.title?.toString() ?: activity.title?.toString() ?: ""
        registeredActivities.put(activity.localClassName, Pair(originalTitle, resourceId))
    }

    @JvmStatic fun unregister(activity: AppCompatActivity) {
        registeredActivities.remove(activity.localClassName)
    }

    @JvmStatic fun notify(message: String) {
        cacheState(message = message,  isNotificationOn = true)
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return

        addProgressBarAndMessageToToolbar(activity as AppCompatActivity, message)
        cacheState(isProgressBarOn = true)
    }

    private fun addProgressBarAndMessageToToolbar(activity: AppCompatActivity, message: String) {
        val (_, resourceId) = registeredActivities.get(activity.localClassName)
        val toolbar = getSupportToolbar(activity, resourceId)
        toolbar.title = message
        if (!isProgressBarOn) toolbar.addView(ProgressBar(activity), 0)
    }

    private fun cacheState(
            message: String = this.message,
            isProgressBarOn: Boolean = this.isProgressBarOn,
            isNotificationOn: Boolean = this.isNotificationOn
    ) {
        this.message = message
        this.isProgressBarOn = isProgressBarOn
        this.isNotificationOn = isNotificationOn
    }

    @JvmStatic fun endNotification() {
        cacheState(message = "", isNotificationOn = false)
        val activity = lifecycleCallback.activity ?: return
        if (!isRegisteredActivity(activity)) return

        restoreToolbar(activity as AppCompatActivity)
        cacheState(isProgressBarOn = false)
    }

    private fun restoreToolbar(activity: AppCompatActivity) {
        val (originalTitle, resourceId) = registeredActivities.get(activity.localClassName)
        val toolbar = getSupportToolbar(activity, resourceId)
        toolbar.title = originalTitle
        if (isProgressBarOn) toolbar.removeViewAt(0)
    }

    private fun getSupportToolbar(activity: AppCompatActivity, resourceId: Int): Toolbar {
        return activity.findViewById(resourceId)
    }

    private fun getSupportToolbarId(activity: AppCompatActivity): Int {
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
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
            this.activity = activity
            isProgressBarOn = false
            if (isRegisteredActivity(activity) && isNotificationOn) notify(message)
        }

        override fun onActivityPaused(activity: Activity) {
            if (isRegisteredActivity(activity) && isNotificationOn) {
                restoreToolbar(activity as AppCompatActivity)
            }
            if (this.activity === activity) this.activity = null
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            // Used to automatically unregister the destroyed activity. Turns out if you exit and
            // reenter an activity quickly enough this method can end up being called after the
            // its new instance is created, thus unregistering immediately after its registration.
        }
    }

}