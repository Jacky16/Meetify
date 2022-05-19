package com.example.meetify.viewmodel

import androidx.lifecycle.ViewModel
import com.example.meetify.model.UserModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    fun logIn(email: String, password: String, onLogin: () -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currUser = FirebaseAuth.getInstance().currentUser

                currUser?.let {

                    //Setear el id
                    UserModel.setUserId(it.uid)

                    //Setear el nickName
                    it.displayName?.let { nickName -> UserModel.setNickname(nickName) }

                    onLogin()

                }
            }.addOnFailureListener {
            }
    }

    fun signUp(nickName: String, email: String, password: String, onSignUp: () -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currUser = FirebaseAuth.getInstance().currentUser
                currUser?.let {
                    UserModel.setUserId(currUser.uid)
                    it.displayName?.let { nickName -> UserModel.setNickname(nickName) }
                }
                onSignUp()
            }.addOnFailureListener {

            }
    }

    fun isSginIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

}