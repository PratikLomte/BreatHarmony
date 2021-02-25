package com.catalystmedia.half_life

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_reset.setOnClickListener {
            resetData()
        }

        btn_back_settings.setOnClickListener {
            finish()
        }

    }

    private fun resetData() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
        builder.setTitle("Are you want to reset your Progress?")
        builder.setPositiveButton("CANCEL", DialogInterface.OnClickListener { dialog, id -> super@SettingsActivity.onBackPressed()
        })
        builder.setNegativeButton("CONFIRM",
                DialogInterface.OnClickListener { dialog, id ->
                    startActivity(Intent(this@SettingsActivity, IntroductionActivity::class.java))})
                    builder.show()

                }
    }

