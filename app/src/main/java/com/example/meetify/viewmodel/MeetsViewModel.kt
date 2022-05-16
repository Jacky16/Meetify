package com.example.meetify.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.ArrayList

class MeetsViewModel : ViewModel() {
    val meets = MutableLiveData<ArrayList<MeetModel>>()

    var db = FirebaseFirestore.getInstance()

    public fun getMeets() {
        //Get all meets from firebase and save it to the local database
        db.collection("meets").get()
            .addOnSuccessListener { result ->
                val meetsList = ArrayList<MeetModel>()
                for (document in result) {

                    val dateTime = document.get("dateTime") as Timestamp
                    val geoPoint = document.getGeoPoint("position")
                    val position = LatLng(geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0)

                    //Init MeetModel with data from firebase
                    val meet = MeetModel(
                        document.id,
                        document.get("title").toString(),
                        dateTime.toDate(),
                        document.get("description").toString(),
                        position
                    )
                    meetsList.add(meet)
                }
                meets.value = meetsList
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting meets", exception)
            }
    }

    public fun addMeet(_meetToAdd: MeetModel) {
        val geoPoint = GeoPoint(_meetToAdd.position!!.latitude, _meetToAdd.position!!.longitude)
        val meetToAdd = hashMapOf(
            "title" to _meetToAdd.title,
            "dateTime" to _meetToAdd.dateTime,
            "description" to _meetToAdd.description,
            "position" to geoPoint
        )
        //Add meetToAdd in firestore database
        db.collection("meets").add(meetToAdd)
            .addOnSuccessListener { documentReference ->
                _meetToAdd.id = documentReference.id
                meets.value?.add(_meetToAdd)
                meets.notifyObserver()
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }



    fun MutableLiveData<*>.notifyObserver() {
        this.value = this.value
    }
}