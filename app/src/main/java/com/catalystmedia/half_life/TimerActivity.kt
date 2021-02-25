package com.catalystmedia.half_life

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.catalystmedia.half_life.util.NotificationUtil
import com.catalystmedia.half_life.util.PrefUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_timer.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimerActivity : AppCompatActivity() {
    private val SELECTED_TIME = "com.catalystmedia"
    private val CURRENT_DAY = "com.catalystmedia.todays_date"

    private var musicSelected = "none"
    private var mediaPlayer: MediaPlayer ?= null
    private var dateFormat = ""
    companion object{
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }
        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }
          val nowSeconds: Long
          get() = Calendar.getInstance().timeInMillis / 1000
    }
    enum class TimerState{
        Stopped, Paused, Running
    }
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var  timerState = TimerState.Stopped
    private var secondsRemaining = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        Toast.makeText(this, "Press the play button when you are Ready!", Toast.LENGTH_SHORT).show()
        musicSelected = intent.getStringExtra("musicSelected").toString()
        playMusic(musicSelected)
        checkDate()

        mute_bg.setOnClickListener {
            mediaPlayer?.stop()
        }
    btn_start_timer.setOnClickListener {
        btn_start_timer.visibility = View.GONE
        btn_pause.visibility = View.VISIBLE
        startTimer()
        timerState = TimerState.Running
        updateButtons()
    }
        btn_pause.setOnClickListener {

        }
        back_from_timer.setOnClickListener {
            onBackPressed()
        }
    }

    private fun playMusic(musicSelected: String) {
        if(musicSelected == "rain" ){
            mediaPlayer = MediaPlayer.create(this, R.raw.audio_rain)
        }
        else if (musicSelected == "sparkles"){
            mediaPlayer = MediaPlayer.create(this, R.raw.audio_sparkles)
        }
        mediaPlayer?.setOnPreparedListener {
            mediaPlayer?.start()
        }

    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Are you sure to go back?")
        builder.setMessage("This will stop your Timer!")
        builder.setPositiveButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
            super@TimerActivity.onBackPressed()
        })
        builder.setNegativeButton("Stop",
            DialogInterface.OnClickListener { dialog, id ->
                removePref()
                super@TimerActivity.onBackPressed() })
        builder.show()
    }

    private fun removePref() {
      timer.cancel()
       onTimerFinished()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }


    override fun onResume() {
        super.onResume()
        initTimer()
        removeAlarm(this)
       NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()

        if(timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }


            else if (timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
            }
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
        }
    private fun initTimer(){
        timerState = PrefUtil.getTimerState(this)
        if(timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()
        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime
        if (secondsRemaining <= 0 )
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        if(timerState == TimerState.Running)
            startTimer()
            updateButtons()
            updateCountDownUI()
    }

    private fun onTimerFinished(){
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val oldTimeToDB =  sharedPref.getInt(PrefUtil.SELECTED_TIME, 2)
        addOldTimetoDB(oldTimeToDB)
        Toast.makeText(this, "$oldTimeToDB",Toast.LENGTH_SHORT).show()
        timerState = TimerState.Stopped
        setNewTimerLength()
        progress_time.animateProgress(2000,0,0)
        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountDownUI()

    }

    private fun addOldTimetoDB(oldTimeToDB: Int) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val currentday = sharedPref.getString(PrefUtil.CURRENT_DAY, "2")
        //TODO: CHECK IF DATA ALREADY EXISTED
        val user = FirebaseAuth.getInstance().currentUser

        FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
            .child("treeGrowth").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        var currentGrowth = (dataSnapshot.value).toString()
                        var newGrowth = ((currentGrowth.toInt()) + 1).toInt()
                        changeGrowth(newGrowth)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        val dayRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
                .child("days")
        val dayMap = HashMap<String, Any>()
        dayMap["day"] = currentday.toString()
        dayMap["date"] = dateFormat.toString()
        dayMap["goalCompleted"] = true
        dayMap["timeMeditated"] = oldTimeToDB.toString() + "mins"
        dayRef.child(currentday.toString()).setValue(dayMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, TimerCompleteScreen::class.java)
                    intent.putExtra("timeCompleted", oldTimeToDB.toString())
                    startActivity(intent)

                }

            }
    }
    private fun checkDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            dateFormat =  current.format(formatter)

        } else {
            var date = Date()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            dateFormat = formatter.format(date)
        }

    }

    private fun changeGrowth(newGrowth: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid)
            .child("treeGrowth").setValue(newGrowth.toString())
    }


    private fun startTimer()
    {
        timerState = TimerState.Running
        timer = object: CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountDownUI()
            }
            override fun onFinish() {
                        onTimerFinished()
            }
        }.start()
    }

    private fun setNewTimerLength()
    {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_time.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
       timerLengthSeconds  = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_time.max = timerLengthSeconds.toInt()
    }

    private fun updateCountDownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinutesUntillFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinutesUntillFinished.toString()
        tv_timer.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
                        else "0" + secondsStr}"
        progress_time.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }
    private fun updateButtons(){
     //TODO: Enable pause and stop disable pause
    }
}