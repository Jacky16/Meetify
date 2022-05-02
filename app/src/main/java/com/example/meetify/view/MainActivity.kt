package com.example.meetify.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.meetify.R
import com.example.meetify.databinding.ActivityMainBinding
import com.example.meetify.model.MeetProvider
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        val db = FirebaseFirestore.getInstance()

        //Setear meets en la cache
        MeetProvider.getMeets()
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    findNavController(binding.navHostFragment.id).navigate(R.id.homeFragment)
                    true
                }
                R.id.mapFragment -> {

                    findNavController(binding.navHostFragment.id).navigate(R.id.searchFragment)
                    true
                }
                else -> false
            }
        }
    }

}