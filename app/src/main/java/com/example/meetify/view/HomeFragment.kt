package com.example.meetify.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meetify.databinding.HomeFragmentBinding
import com.example.meetify.model.CacheMeets
import com.example.meetify.model.MeetProvider
import com.example.meetify.view.adapters.MeetAdapter
import com.example.meetify.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding

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
        val adapter = MeetAdapter(CacheMeets.meetList)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


    }

}