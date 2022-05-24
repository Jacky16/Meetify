package com.example.meetify.view.adapters

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.MeetActivity
import com.example.meetify.R
import com.example.meetify.databinding.ItemMeetBinding
import com.example.meetify.model.MeetModel
import com.example.meetify.viewmodel.MeetViewModel
import com.example.meetify.viewmodel.MeetsViewModel


class MeetAdapter(var meetList: List<MeetModel>) : RecyclerView.Adapter<MeetAdapter.MeetHolder>() {

    inner class MeetHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemMeetBinding.bind(view)

        fun render(meet: MeetModel) {
            binding.tvtitle.text = meet.title
            val peopleInMeet = meet.peopleJoined?.size ?: 0
            binding.tvPeopleCount.text =peopleInMeet.toString()

            //Set datetime to tvHour
            val dateTimeString = DateUtils.formatDateTime(view.context, meet.dateTime?.time ?:0L, DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE)
            binding.tvHour.text = dateTimeString

            //Join Button
            binding.btnJoinMeet.setOnClickListener {
                val mvm = MeetViewModel()
                mvm.joinMeet(meet)

            }
            //View Click
            view.setOnClickListener {
                val intent = Intent(view.context, MeetActivity::class.java);
                intent.putExtra("idMeet", meet.id)
                view.context.startActivity(intent)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MeetHolder(layoutInflater.inflate(R.layout.item_meet, parent, false))
    }

    override fun onBindViewHolder(holder: MeetHolder, position: Int) {
        holder.render(meetList[position])
    }

    override fun getItemCount(): Int {
        return meetList.size
    }

    fun updateMeets(it: ArrayList<MeetModel>) {
        meetList = it
        notifyDataSetChanged()
    }
}




