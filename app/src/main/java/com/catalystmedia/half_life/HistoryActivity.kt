package com.catalystmedia.half_life

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catalystmedia.half_life.adapters.HistoryAdapter
import com.catalystmedia.half_life.models.History
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_history.*
import java.util.*
import kotlin.collections.ArrayList

class HistoryActivity : AppCompatActivity(){

    private var historyAdapter: HistoryAdapter ?= null
     private var itemList: MutableList<History> ?= null
    private var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        var recyclerView: RecyclerView? = null
        recyclerView = findViewById(R.id.recyler_history)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        itemList = ArrayList()
        historyAdapter = HistoryAdapter(this, itemList as ArrayList<History>)
        recyclerView.adapter = historyAdapter
        getHistory()

        btn_back_history.setOnClickListener {
            finish()
        }
    }
    private fun getHistory() {
       val historyRef = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid.toString())
               .child("days")
        historyRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
             if(dataSnapshot.exists()){
                 itemList!!.clear()
                 for(snapshot in dataSnapshot.children){
                     val item = snapshot.getValue(History::class.java)
                     itemList!!.add(item!!)
                 }
                 historyAdapter!!.notifyDataSetChanged()
             }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}