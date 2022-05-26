package com.example.meetify.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.meetify.R
import com.example.meetify.databinding.ActivityMainBinding
import com.example.meetify.model.UserModel
import com.example.meetify.viewmodel.MeetsViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding;
    val meetsViewModel: MeetsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        val db = FirebaseFirestore.getInstance()
        meetsViewModel.getMeets()

        //Do a toast with current userModel
        //Toast.makeText(this, "Welcome ${UserModel.getUserNickName()}", Toast.LENGTH_LONG).show()

        //Setear meets en la cache
        binding.bottomNavigation.setOnItemSelectedListener {
            onNavigationItemSelected(it)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //Select items
        when (item.itemId) {
            R.id.home -> {
                findNavController(binding.navHostFragment.id).navigate(R.id.homeFragment)
                return true
            }
            R.id.mapFragment -> {
                findNavController(binding.navHostFragment.id).navigate(R.id.searchFragment)
                return true
            }
            R.id.settingsFragment -> {
                findNavController(binding.navHostFragment.id).navigate(R.id.settingsFragment)
                return true
            }
            else -> return false
        }
    }
}