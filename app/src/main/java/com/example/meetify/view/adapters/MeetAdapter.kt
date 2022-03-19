package com.example.meetify.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.R
import com.example.meetify.databinding.ItemMeetBinding
import com.example.meetify.model.MeetModel

class MeetAdapter(val meetList:List<MeetModel>):RecyclerView.Adapter<MeetAdapter.MeetHolder>() {

    inner class MeetHolder(val view: View):RecyclerView.ViewHolder(view) {
        var binding: ItemMeetBinding = ItemMeetBinding.inflate(LayoutInflater.from(view.context))
        fun render(meet:MeetModel){
            view.findViewById<TextView>(R.id.tvtitle).text = meet.name
            view.findViewById<TextView>(R.id.tv_people_count).text = meet.people.toString()
            view.findViewById<TextView>(R.id.tv_hour).text = meet.hour.toString() + ":00"

            view.findViewById<Button>(R.id.btn_join_meet).setOnClickListener {

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MeetHolder(layoutInflater.inflate(R.layout.item_meet, parent, false))
    }

    override fun onBindViewHolder(holder: MeetHolder, position: Int){
        holder.render(meetList[position])
    }

    override fun getItemCount(): Int {
        return  meetList.size
    }
}




