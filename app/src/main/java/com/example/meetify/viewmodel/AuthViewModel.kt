package com.example.meetify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.meetify.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {

    fun logIn(email: String, password: String, onLogin: () -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currUser = FirebaseAuth.getInstance().currentUser

                currUser?.let {
                    saveUserDataOnFirestore()
                    onLogin()
                }
            }.addOnFailureListener {

            }
    }

    fun signUp(nickName: String, email: String, password: String, onSignUp: () -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currUser = FirebaseAuth.getInstance().currentUser
                //Change name of currentUser of firebase
                val profileUpdates = userProfileChangeRequest {
                    displayName = nickName
                }

                currUser?.let { user ->
                    currUser.updateProfile(profileUpdates).addOnSuccessListener {
                        saveUserDataOnFirestore()
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        onSignUp()
                    }
                }
            }
    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun isSinIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    private fun saveUserDataOnFirestore() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            val userData = hashMapOf(
                "nickname" to it.displayName,
                "userId" to it.uid,
                "email" to it.email,
                "photoUrl" to it.photoUrl.toString(),
                "ownerMeets" to arrayListOf<String>(),
            )
            FirebaseFirestore.getInstance().collection("users").document(it.uid).set(userData)
        }

    }

}