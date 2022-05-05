package com.example.meetify.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetify.databinding.HomeFragmentBinding
import com.example.meetify.view.adapters.MeetAdapter
import com.example.meetify.viewmodel.MeetsViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    val meetsModelView: MeetsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView = binding.recyclerViewMeets
        val adapter = MeetAdapter(listOf())

        meetsModelView.getMeets()
        meetsModelView.meets.observe(viewLifecycleOwner){
            adapter.updateMeets(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter


    }

}