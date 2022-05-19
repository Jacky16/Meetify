package com.example.meetify.model

import com.google.firebase.auth.FirebaseAuth

object UserModel
{
    var id: String? = null
    var nickName: String? = null
    val listOfMeets: ArrayList<MeetModel> = ArrayList<MeetModel>()


    fun setUserId(id: String)
    {
        this.id = id
    }

    fun setNickname(_nickName: String)
    {
        this.nickName = _nickName
    }


    fun getUserId(): String {
        return id ?: "id not found"
    }

    fun getUserNickName(): String {
        return nickName ?: "Nickname Not found"
    }

    fun getUserListOfMeets(): ArrayList<MeetModel> {
        return listOfMeets
    }
}