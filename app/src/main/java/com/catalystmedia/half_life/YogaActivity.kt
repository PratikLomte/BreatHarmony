package com.catalystmedia.half_life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_yoga.*

class YogaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoga)

        btn_yoga_1.setOnClickListener {
            val intent = Intent(this, PDFview::class.java)
            startActivity(intent)
        }
        btn_back_yoga.setOnClickListener {
            finish()
        }
    }
}