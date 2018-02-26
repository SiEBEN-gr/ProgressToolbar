package dev.padam.notifytestproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_other.*

class OtherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        setSupportActionBar(toolbar2)

        registerForNotifications()

        button.setOnClickListener({ ProgressToolbar.notify("Loading something else.") })
        button2.setOnClickListener({ ProgressToolbar.endNotification() })
    }
}
