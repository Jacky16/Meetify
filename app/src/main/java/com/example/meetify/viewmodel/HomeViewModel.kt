package com.example.meetify.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel
import com.example.meetify.model.MeetProvider

class HomeViewModel: ViewModel() {

    val quoteMeetList = MutableLiveData<List<MeetModel>>()
    fun getMeets(){
        //val meets = meetProvider.getMeets()
        //quoteMeetList.postValue(meets)
    }

}