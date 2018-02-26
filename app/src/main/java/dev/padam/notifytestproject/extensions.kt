package dev.padam.notifytestproject

import android.support.v7.app.AppCompatActivity

/**
 * Created by p.adam on 22/2/2018.
 */

fun AppCompatActivity.registerForNotifications() {
    ProgressToolbar.register(this)
}

fun AppCompatActivity.unregisterFromNotifications() {
    ProgressToolbar.unregister(this)
}