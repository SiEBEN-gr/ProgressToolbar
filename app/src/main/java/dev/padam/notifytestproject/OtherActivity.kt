package dev.padam.notifytestproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_other.*

class OtherActivity : AppCompatActivity() {

    var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other)

        setSupportActionBar(toolbar2)

        registerForNotifications()

        button.setOnClickListener({
            if (flag) {
                NotifyManager.notify("boil 'em")
            } else {
                NotifyManager.endNotification()
            }
            flag = !flag
        })
    }
}
