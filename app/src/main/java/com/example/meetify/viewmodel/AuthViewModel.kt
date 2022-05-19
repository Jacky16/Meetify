package com.example.meetify.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel:ViewModel() {

    fun logIn(email:String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }



}