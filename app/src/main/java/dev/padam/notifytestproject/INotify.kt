package dev.padam.notifytestproject

import android.app.Activity

/**
 * Created by p.adam on 16/2/2018.
 */

interface INotify {
    fun init(app: App)
    fun registerActivity(activity: Activity)
    fun changeTitle(message: String)
    fun restoreTitle()
}