package com.catalystmedia.half_life

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeActivity : AppCompatActivity(){
    private val user = FirebaseAuth.getInstance().currentUser
    private var todaysDate = ""
    private val CURRENT_DAY = "com.catalystmedia.todays_date"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userInfo()
        checkDays()
        checkGrowth()
        checkDate()
        getCurrentDay()

        iv_home_profile.setOnClickListener {
         val intent = Intent(this@HomeActivity, SettingsActivity::class.java)
            startActivity(intent)

        }

        btn_settings.setOnClickListener {
            val intent = Intent(this@HomeActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        btn_start.setOnClickListener {
            val intent = Intent(this, TypeSelectScreen::class.java)
            startActivity(intent)
        }

    }

    private fun getCurrentDay() {
        checkDate()
        var dayRef = FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid).child("startDate")
        dayRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    val startDate = dataSnapshot.value.toString()
                    val currentday = ((todaysDate.toInt() - startDate.toInt())+1).toString()
                    addCurrentDayToPref(currentday)
                    phaseCheck(currentday)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun addCurrentDayToPref(currentday: String) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(CURRENT_DAY, currentday.toString())
        editor.apply()
    }

    private fun checkDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            todaysDate =  current.format(formatter)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("yyyyMMdd")
            todaysDate = formatter.format(date)
        }
    }

    private fun checkGrowth() {
        val user = FirebaseAuth.getInstance().currentUser
        val treeRef = FirebaseDatabase.getInstance().reference.child("Users")
                .child(user!!.uid).child("treeGrowth")
        treeRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    var growth = dataSnapshot.value.toString()
                    var growthInt = growth.toInt()
                   var growthProgress = 0

                    when {
                        growthInt <= 3 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_1)
                             growthProgress = 10
                            tv_init.visibility = View.VISIBLE
                             updateProgress(growthProgress)
                        }
                        growthInt <= 6 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_2)
                            growthProgress = 20
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 9 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_3)
                            growthProgress = 30
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 12 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_4)
                            growthProgress = 40
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 15 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_5)
                            growthProgress = 50
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 18 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_6)
                            growthProgress = 60
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 21 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_7)
                            growthProgress = 70
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 24 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_8)
                            growthProgress = 80
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 27 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_9)
                            growthProgress = 90
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                        growthInt <= 30 -> {
                            iv_tree_img.setImageResource(R.drawable.ic_tree_10)
                            growthProgress = 100
                            tv_init.visibility = View.GONE
                            updateProgress(growthProgress)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateProgress(growthProgress: Int) {
        progress_tree.animateProgress(2000,0, growthProgress)
    }

    private fun phaseCheck(currentday: String) {
        when {
            currentday.toInt() in 0..30 -> {
                Toast.makeText(this, "Phase 1", Toast.LENGTH_SHORT).show()
            }
            currentday.toInt() in 30..60 -> {
                Toast.makeText(this, "Phase 2", Toast.LENGTH_SHORT).show()

            }
            currentday.toInt() in 60..90 -> {

                Toast.makeText(this, "Phase 3", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkDays() {
        val user = FirebaseAuth.getInstance().currentUser
       val daysRef = FirebaseDatabase.getInstance().reference.child("Users")
           .child(user!!.uid).child("days")
        daysRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               if(dataSnapshot.exists()){
                   val day =  dataSnapshot.childrenCount.toLong()
                   checkGoals(day)
                   if(day < 10){
                       tv_days.text = "DAY 0$day"
                   }
                   else{
                       tv_days.text = "DAY $day"
                   }
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkGoals(day: Long){
        val dayCurrent = day.toString()
        val user = FirebaseAuth.getInstance().currentUser
        val userUID = user!!.uid
        val goalsRef = FirebaseDatabase.getInstance().reference.child("Users").child(userUID.toString()).child("days")
                .child(dayCurrent).child("goalCompleted")
        goalsRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               if(dataSnapshot.exists()){
                   val isGoalComplete = dataSnapshot.value.toString()
                   if(isGoalComplete == "true"){
                       meditation_check.visibility = View.VISIBLE
                       tv_meditation.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
                   }
                   else if(isGoalComplete == "false"){
                       meditation_check.visibility = View.GONE
                       tv_meditation.paintFlags = tv_meditation.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                   }
               }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }

    private fun userInfo(){
        val userName = user!!.displayName
        tv_salutaion.text = "Welcome, $userName"
        Glide.with(this@HomeActivity).load(user.photoUrl).into(iv_home_profile)
    }

}