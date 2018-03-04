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

    /**
     * Register to the applications activity lifecycle callbacks in order to show or hide
     * notification depending on lifecycle changes.
     */
    @JvmStatic fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    /**
     * Register activity to receive notifications.
     * Activity must be appCompat and contain a [Toolbar] in its layout.
     */
    @JvmStatic fun register(activity: AppCompatActivity) {
        if (isRegisteredActivity(activity)) return
        val resourceId = getSupportToolbarId(activity)
        val toolbar = getSupportToolbar(activity, resourceId)
        val originalTitle = toolbar.title?.toString() ?: activity.title?.toString() ?: ""
        registeredActivities.put(activity.localClassName, Pair(originalTitle, resourceId))
    }

    /** Unregister activity from notifications.*/
    @JvmStatic fun unregister(activity: AppCompatActivity) {
        registeredActivities.remove(activity.localClassName)
    }

    /**
     * Display notification in the activity's toolbar.
     * @param message with replace the title and a [ProgressBar] will appear next to it.
     */
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

    /**
     * Remove notification displayed with [notify].
     * Original title will be restored and the displayed [ProgressBar] will be removed.
     */
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

        /**
         * Cache the current visible activity & show cached notification.
         * Notifications will not be shown before this method is called.
         * If [notify] is called earlier in the activity lifecycle the message will be cached and
         * shown right after the activity resumes.
         */
        override fun onActivityResumed(activity: Activity) {
            this.activity = activity
            isProgressBarOn = false
            if (isRegisteredActivity(activity) && isNotificationOn) notify(message)
        }

        /** Release reference to cached activity and restore the toolbar to its original state. */
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