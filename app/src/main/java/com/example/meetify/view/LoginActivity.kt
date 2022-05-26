package com.example.meetify.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.meetify.databinding.ActivityLoginBinding
import com.example.meetify.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createAccount()
        logIn()
        //Sing out of firebase
        FirebaseAuth.getInstance().signOut()

        if(viewModel.isSinIn()){
            loadMainActivity()
        }



    }
    private fun logIn(){

        binding.btnLogin.setOnClickListener {
            if(inputsCheckers()){
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                viewModel.logIn(email, password){
                    loadMainActivity()
                }
            }
        }
    }

    private fun inputsCheckers(): Boolean {
        //Check password
        val isPasswordValid = binding.editTextPassword.text?.length!! >= 6
        if (isPasswordValid) {
            binding.editTextPassword.error = null
        } else {
            binding.editTextPassword.error = "Password must be at least 6 characters"
            return false
        }
        //Check email
        if (isEmailValid(binding.editTextEmail.text.toString())) {
            binding.editTextEmail.error = null
        } else {
            binding.editTextEmail.error = "Invalid email"
            return false
        }
        return true
    }

    private fun createAccount(){
        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}