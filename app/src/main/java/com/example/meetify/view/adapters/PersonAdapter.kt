package com.example.tabs.Person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.R
import com.example.meetify.databinding.ItemPersonBinding
import com.example.meetify.model.PersonModel
import com.example.tabs.Person.PersonAdapter.*

class PersonAdapter(val persons: List<PersonModel>) :
    RecyclerView.Adapter<PersonHolder>() {

    class PersonHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPersonBinding.bind(view)
        fun render(person: PersonModel){
            //binding.tvName.text = person.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        val layoutInflater =  LayoutInflater.from(parent.context)
        return PersonHolder(layoutInflater.inflate(R.layout.item_person,parent,false))
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.render(persons[position])
    }

    override fun getItemCount(): Int {
        return persons.size
    }
}