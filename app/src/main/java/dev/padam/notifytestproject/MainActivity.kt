package dev.padam.notifytestproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        registerForNotifications()

        notify.setOnClickListener({ NotifyManager.notify("potato") })
        endNotify.setOnClickListener({ NotifyManager.endNotification() })

        changeActivity.setOnClickListener({
            startActivity(Intent(this, OtherActivity::class.java))
        })
    }
}
