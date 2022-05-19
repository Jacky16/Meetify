package com.example.meetify.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.meetify.R
import com.example.meetify.databinding.ActivityRegisterBinding
import com.example.meetify.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUp()

    }

    private fun signUp() {
        binding.btnRegister.setOnClickListener {
            val isNullOrEmptyFields = binding.editTextEmail.text.isNullOrEmpty() ||
                    binding.editTextPassword.text.isNullOrEmpty() ||
                    binding.editTextNickname.text.isNullOrEmpty()

            if (!isNullOrEmptyFields) {
                //Sign up in firebase
                viewModel.signUp(
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString(),
                    binding.editTextNickname.text.toString()
                ) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
