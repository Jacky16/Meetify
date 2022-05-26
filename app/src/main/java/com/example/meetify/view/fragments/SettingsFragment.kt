package com.example.meetify.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.meetify.R
import com.example.meetify.databinding.FragmentSettingsBinding
import com.example.meetify.view.LoginActivity
import com.example.meetify.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNicknname()
        onLogoutClicked()
    }
    private fun initNicknname(){
        binding.tvNickname.text = "Hi " + FirebaseAuth.getInstance().currentUser?.displayName
    }
    private fun onLogoutClicked() {
        binding.btnLogout.setOnClickListener {
            authViewModel.logOut()
            this.activity?.startActivity(Intent(this.activity, LoginActivity::class.java))
            this.activity?.finish()
        }
    }

}