package dev.padam.notifytestproject

import android.app.Application

/**
 * Created by p.adam on 16/2/2018.
 */

class App : Application() {

    companion object {
        lateinit var instance : App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        NotifyManager.init(instance)
        listOf(2,2,3,4,4,5,6,6,6)
    }

}