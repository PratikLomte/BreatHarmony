package com.catalystmedia.half_life

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeRange
import com.akexorcist.snaptimepicker.TimeValue
import kotlinx.android.synthetic.main.activity_meditation.*

class MeditationActivity : AppCompatActivity() {
    private val SELECTED_TIME = "com.catalystmedia"
    private var recMin = 1
    private var recHr = 0
    private var selectedTime = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)

        btn_back_med.setOnClickListener {
            finish()
        }

        ib_chng_time.setOnClickListener {
            tv_recommended.visibility = View.GONE
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.title)
                setThemeColor(R.color.colorAccent)
                setTitleColor(R.color.colorWhite)
                setPreselectedTime(TimeValue(recHr, recMin))
                setSelectableTimeRange(TimeRange(TimeValue(0, 1), TimeValue(10, 0)))
            }.build().apply{
                setListener { hour, minute ->
                    var selectedHour = hour*60
                    selectedTime = (selectedHour+minute)
                    setTimeText(hour,minute)
                }
            }.show(supportFragmentManager, "TimePicked")
        }
        btn_begin.setOnClickListener {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putInt(SELECTED_TIME, selectedTime)
            editor.apply()
            val intent = Intent(this, TimerActivity::class.java)
            intent.putExtra("musicSelected", "sparkles" )
            startActivity(intent)

        }

    }

    private fun setTimeText(hour: Int, minute: Int) {
        if(minute >= 10) {
            tv_timer.text = "0$hour:$minute"
        }
        else if (minute < 10){
            tv_timer.text = "0$hour:0$minute"
        }
    }
}