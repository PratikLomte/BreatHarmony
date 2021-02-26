package com.catalystmedia.half_life

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeRange
import com.akexorcist.snaptimepicker.TimeValue
import kotlinx.android.synthetic.main.activity_meditation.*

class MeditationActivity : AppCompatActivity() {
    private val SELECTED_TIME = "com.catalystmedia"
    //TODO: get recMin from Phase
    private var recMin = 2
    private var recHr = 0
    private var selectedTime = 2
    private var selectedMusic = "None"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation)
        val music = resources.getStringArray(R.array.Music)

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, music)
        music_spinner.adapter = adapter

        music_spinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
               selectedMusic =  music[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }
        }
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
            intent.putExtra("musicSelected", selectedMusic )
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