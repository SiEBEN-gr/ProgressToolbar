package dev.padam.notifytestproject

import android.support.v7.app.AppCompatActivity

/**
 * Created by p.adam on 22/2/2018.
 */

fun AppCompatActivity.registerForNotifications() {
    NotifyManager.register(this)
}

fun AppCompatActivity.unregisterFromNotifications() {
    NotifyManager.unregister(this)
}