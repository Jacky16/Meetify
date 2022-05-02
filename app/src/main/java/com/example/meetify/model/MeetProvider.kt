package com.example.meetify.model

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*


object MeetProvider {

    var db = FirebaseFirestore.getInstance()

    public fun getMeets():List<MeetModel> {
        //Get all meets from firebase and save it to the local database
        var meets = ArrayList<MeetModel>()
        db.collection("meets").get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val dateTime = document.get("dateTime") as Timestamp
                    val geoPoint = document.getGeoPoint("position")
                    val position = LatLng(geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0)

                    //Init MeetModel with data from firebase
                    val meet = MeetModel(
                        document.get("title").toString(),
                        dateTime.toDate(),
                        document.get("description").toString(),
                        position
                    )
                    meets.add(meet)
                    CacheMeets.meetList.add(meet)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting meets", exception)
            }

        return meets
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
                CacheMeets.meetList.add(_meetToAdd)

                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}