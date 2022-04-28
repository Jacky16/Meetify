package com.example.meetify.model

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MeetProvider {
    var db = FirebaseFirestore.getInstance()
    public fun getMeets(): List<MeetModel> {

        val meetsList:MutableList<MeetModel> = mutableListOf()
        db.collection("meets")
            .get()
            .addOnSuccessListener { result ->
                result.toObjects(MeetModel::class.java).forEach() {
                    meetsList.add(it)
                }
                for (document in result) {
                    val dateTime = document.data["datetime"] as Timestamp
                    val position = document.data["position"] as LatLng
                    val description = document.data["description"] as String
                    val title= document.data["title"] as String

                    val meetModel = MeetModel(title, Calendar.getInstance(),description,position)
                    Log.d(TAG,meetModel.toString())

                }
            }
        return meetsList
    }

    public fun addMeet(_meetToAdd: MeetModel) {

        val meetToAdd = hashMapOf(
            "title" to _meetToAdd.title,
            "datetime" to _meetToAdd.dateTime.time,
            "description" to _meetToAdd.description,
            "position" to _meetToAdd.position
        )
        //Add meetToAdd in firestore database
        db.collection("meets").add(meetToAdd)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}

