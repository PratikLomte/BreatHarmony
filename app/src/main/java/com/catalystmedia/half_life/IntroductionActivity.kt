package com.catalystmedia.half_life

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_introduction.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class IntroductionActivity : AppCompatActivity() {
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        space2.visibility = View.GONE
        ll_1.visibility = View.VISIBLE
        tv_bottom_text.visibility = View.VISIBLE
        btn_next.visibility = View.VISIBLE


        btn_next.setOnClickListener {
            count++
            checkCount()
        }

        btn_begin.setOnClickListener {
            initializeDaysDB()
        }

        }

    private fun initializeDaysDB() {
        val user = FirebaseAuth.getInstance().currentUser
        val userUid = user!!.uid
        val daysRef = FirebaseDatabase.getInstance().reference.child("Users").child(userUid.toString()).child("days").child("1")
        val daysMap = HashMap<String, Any>()
        daysMap["day"] = "1"
        daysMap["timeMeditated"] = ""
        daysMap["date"] = ""
        daysMap["mood"] = ""
        daysMap["goalCompleted"] = false
       daysRef.setValue(daysMap).addOnCompleteListener { task ->
           if(task.isSuccessful){
               addDate()
               Toast.makeText(this, "Welcome Onboard!", Toast.LENGTH_SHORT).show()
               val intent = Intent(this, HomeActivity::class.java)
               startActivity(intent)
               finish()
           }

       }
    }

    private fun addDate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            var myDate: String =  current.format(formatter)
            Toast.makeText(this@IntroductionActivity, myDate, Toast.LENGTH_LONG).show()
            addStartDate(myDate)
        } else {
            var date = Date()
            val formatter = SimpleDateFormat("yyyyMMdd")
            val myDate: String = formatter.format(date)
            Toast.makeText(this@IntroductionActivity, myDate, Toast.LENGTH_LONG).show()
            addStartDate(myDate)
        }

    }

    private fun addStartDate(date: String){
        val user = FirebaseAuth.getInstance().currentUser
     FirebaseDatabase.getInstance().reference.child("Users").child(user!!.uid.toString()).child("startDate").setValue(date)
    }
    private fun checkCount() {
        if(count == 0){
            ll_1.visibility = View.VISIBLE
        }
        if(count == 1){
            iv_image.setImageResource(R.drawable.meditation)
            tv_bottom_text.text = "We help you practise Mindfullness, \nthrough meditation and yoga!"
        }
        if(count == 2){
            btn_begin.visibility = View.VISIBLE
            space2.visibility = View.VISIBLE
            ll_1.visibility = View.GONE
            tv_bottom_text.visibility = View.GONE
            btn_next.visibility = View.GONE
        }
    }
}