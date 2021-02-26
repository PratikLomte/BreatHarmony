package com.catalystmedia.half_life

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sleep.*

class SleepActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep)

        btn_back_sleep.setOnClickListener {
            finish()
        }

        cv_audio_1.setOnClickListener {
            mediaPlayer = MediaPlayer.create(this, R.raw.audio_sleep_story)
            mediaPlayer?.start()
            btn_play_story.visibility = View.GONE
            btn_pause_story.visibility = View.VISIBLE
        }

        btn_play_story.setOnClickListener {
            btn_play_story.visibility = View.GONE
            btn_pause_story.visibility = View.VISIBLE
            mediaPlayer?.start()
        }

        btn_pause_story.setOnClickListener {
            btn_play_story.visibility = View.VISIBLE
            btn_pause_story.visibility = View.GONE
            mediaPlayer?.pause()
        }

    }

}