package com.catalystmedia.half_life

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.catalystmedia.half_life.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       //TODO: Show Notif
        PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}