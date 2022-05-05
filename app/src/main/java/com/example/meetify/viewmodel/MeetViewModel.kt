package com.example.meetify.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class MeetViewModel : ViewModel() {
    var meet: MutableLiveData<MeetModel> = MutableLiveData()
    var db = FirebaseFirestore.getInstance()

    public fun getMeetById(id: String){
        //Get meet from firestore by id
        var meetPlace: MeetModel? = null

        db.collection("meets").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dateTime = document.get("dateTime") as Timestamp
                    val geoPoint = document.getGeoPoint("position")
                    val position = LatLng(geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0)

                    //Init MeetModel with data from firebase
                    meetPlace = MeetModel(
                        document.id,
                        document.get("title").toString(),
                        dateTime.toDate(),
                        document.get("description").toString(),
                        position
                    )
                    meetPlace?.let {
                        meet.value = it
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting meet", exception)
            }

    }
}
