package com.catalystmedia.half_life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_timer_complete_screen.*

class TimerCompleteScreen : AppCompatActivity() {
    private var timeComplete = "NA"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_complete_screen)

         timeComplete = intent.getStringExtra("timeCompleted").toString()
        setText(timeComplete)

        btn_finish.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setText(timeComplete: String) {
        tv_oldTIme.text = "$timeComplete\nminutes"
    }

}