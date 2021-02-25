package com.catalystmedia.half_life.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.Preference
import android.preference.PreferenceManager
import com.catalystmedia.half_life.TimerActivity

class PrefUtil {


    companion object{
        fun getTimerLength(context: Context): Int{
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPref.getInt(SELECTED_TIME, 2)
        }

        private const val  PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.catalystmedia.timer.previous_timer_length"
        var SELECTED_TIME = "com.catalystmedia"
        var CURRENT_DAY = "com.catalystmedia.todays_date"

        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }
        fun setPreviousTimerLengthSeconds(seconds:Long , context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }
        private const val TIMER_STATE_ID = "com.catalystmedia.timer.timer_state"

        fun getTimerState(context: Context): TimerActivity.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return  TimerActivity.TimerState.values()[ordinal]
        }
        fun setTimerState(state: TimerActivity.TimerState, context: Context){
            val editor  = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }
        private const val SECONDS_REMAINING_ID = "com.catalystmedia.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }
        fun setSecondsRemaining(seconds:Long , context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }
        private const val ALARM_SET_TIME_ID = "com.catalystmedia.timer.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val prefrences = PreferenceManager.getDefaultSharedPreferences(context)
            return prefrences.getLong(ALARM_SET_TIME_ID, 0)
        }
        fun setAlarmSetTime(time:Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}