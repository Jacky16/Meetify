package com.example.meetify.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        backButton()
    }

    private fun signUp() {
        binding.btnRegister.setOnClickListener {

            //Check password
            val isPasswordValid = binding.editTextPassword.text?.length!! > 6
            if (isPasswordValid) {
                binding.editTextPassword.error = null
            } else {
                binding.editTextPassword.error = "Password must be at least 6 characters"
            }
            //Check email
            if(isEmailValid(binding.editTextEmail.text.toString())) {
                binding.editTextEmail.error = null
            }else{
                binding.editTextEmail.error = "Invalid email"
            }
            //Check null or empty
            val isNullOrEmptyFields = binding.editTextEmail.text.isNullOrEmpty() ||
                    binding.editTextPassword.text.isNullOrEmpty() ||
                    binding.editTextNickname.text.isNullOrEmpty()


            if (!isNullOrEmptyFields) {
                if (isPasswordValid)
                    viewModel.signUp(
                        binding.editTextNickname.text.toString(),
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString()
                    ) {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
            }

        }
    }

    private fun backButton() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
