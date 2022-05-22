package com.example.meetify.view

import android.content.Intent
import android.os.Bundle
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
        //FirebaseAuth.getInstance().signOut()

        if(viewModel.isSinIn()){
            loadMainActivity()
        }



    }
    private fun logIn(){
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.logIn(email, password){
                loadMainActivity()
            }
        }
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
}