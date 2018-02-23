package dev.padam.notifytestproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        registerForNotifications()

        changeTitle.setOnClickListener({
            if (flag) {
                NotifyManager.showProgressBar("potato")
            } else {
                NotifyManager.restoreActionBar()
            }
            flag = !flag
        })

        changeActivity.setOnClickListener({
            startActivity(Intent(this, OtherActivity::class.java))
        })
    }
}
