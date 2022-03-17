package com.example.meetify.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel

class MeetViewModel:ViewModel() {
    val meetModel = MutableLiveData<MeetModel>()


}