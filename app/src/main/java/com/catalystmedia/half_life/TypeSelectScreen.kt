package com.catalystmedia.half_life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_type_select_screen.*

class TypeSelectScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_select_screen)

        btn_back_type.setOnClickListener {
            finish()
        }
        cv_meditation.setOnClickListener {
            val intent = Intent(this, MeditationActivity::class.java)
            startActivity(intent)
        }
        cv_yoga.setOnClickListener {
            val intent = Intent(this, PDFview::class.java)
            startActivity(intent)
        }
        cv_sleep.setOnClickListener {
            val intent = Intent(this, SleepActivity::class.java)
            startActivity(intent)
        }

    }
}