package com.example.meetify.view.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.model.MeetModel

class MeetAdapter(val meetList:List<MeetModel>):RecyclerView.Adapter<MeetAdapter.MeetHolder>() {
    inner class MeetHolder(val view: View):RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MeetHolder, position: Int){

    }

    override fun getItemCount(): Int {
        return  meetList.size
    }
}




