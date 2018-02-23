package dev.padam.notifytestproject

import android.app.Activity

/**
 * Created by p.adam on 22/2/2018.
 */

fun Activity.registerForNotifications() {
    NotifyManager.registerActivity(this)
}