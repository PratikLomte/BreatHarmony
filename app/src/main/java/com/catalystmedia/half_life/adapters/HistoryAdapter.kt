package com.catalystmedia.half_life.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.catalystmedia.half_life.R
import com.catalystmedia.half_life.models.History
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class HistoryAdapter(private var mContext: Context, private var mList: ArrayList<History>):
RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    private var mCtx: Context? = null
    private var historyList: MutableList<History>? = null
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        return ViewHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.history_item,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val item = mList[position]
        val date = item!!.getDate()
        val dayNumber = item!!.getDay()
        val timeMeditated = item!!.getTimeMeditated()
        val isComplete: Boolean = item!!.getGoalCompleted()
        val mood = item!!.getMood()
        holder.timeMeditated.text = timeMeditated
        holder.date.text = date
        if(dayNumber.toLong() < 10){
            holder.dayNumber.text = "DAY 0$dayNumber"
        }
        else{
            holder.dayNumber.text = "DAY $dayNumber"
        }
        holder.moodIcon.setImageResource(R.drawable.ic_baseline_tag_faces_24)
        if(isComplete){
            holder.cardBg.setImageResource(R.drawable.history_green)
        }
        else if (!isComplete){
           holder.cardBg.setImageResource(R.drawable.history_red)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder (@NonNull itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var dayNumber: TextView = itemView.findViewById(R.id.tv_day_no)
        var date: TextView = itemView.findViewById(R.id.tv_date)
        var timeMeditated: TextView = itemView.findViewById(R.id.tv_time_meditated)
        var moodIcon: ImageView = itemView.findViewById(R.id.mood_icon)
        var cardBg: ImageView = itemView.findViewById(R.id.card_bg)

    }

}