package com.example.meetify.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import kotlin.collections.ArrayList

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
                    val peopleInMeet = document.get("peopleInMeet") as ArrayList<String>?
                    val idOwner = document.get("idOwner") as String

                    //Init MeetModel with data from firebase
                    val meet = MeetModel(
                        document.id,
                        document.get("title").toString(),
                        dateTime.toDate(),
                        document.get("description").toString(),
                        position,
                        idOwner,
                        peopleInMeet
                    )
                    meetsList.add(meet)

                }
                meets.value = meetsList
                meets.notifyObserver()
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
            "position" to geoPoint,
            "idOwner" to _meetToAdd.idOwner,
        )
        //Add meetToAdd in firestore database
        db.collection("meets").add(meetToAdd)
            .addOnSuccessListener { documentReference ->
                _meetToAdd.id = documentReference.id
                meets.value?.add(_meetToAdd)
                meets.notifyObserver()
                assingOwnerMeet(_meetToAdd)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    public fun deleteMeet(_meetToDelete: MeetModel) {
        db.collection("meets").document(_meetToDelete.id!!).delete()
            .addOnSuccessListener {
                meets.value?.remove(_meetToDelete)
                meets.notifyObserver()

                deleteAssignedMeet(_meetToDelete)
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error deleting document", e)
            }
    }

    private fun assingOwnerMeet(meet: MeetModel) {
        val listOwnerMeets = ArrayList<String>()
        listOwnerMeets.add(meet.id!!)

        //Update array of ownerMeets in firestore database
        db.collection("users").document(meet.idOwner!!).set(
            mutableMapOf(
                "ownerMeets" to listOwnerMeets
            ),SetOptions.merge()
        )
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
            }

        //Asignar tambien que se ha unido a la meet
        val mvm = MeetViewModel()
        mvm.assingJoinedMeet(meet)
    }


    private fun deleteAssignedMeet(meet: MeetModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("users").document(userId!!)
            .update("ownerMeets", FieldValue.arrayRemove(meet.id!!))
    }

    fun MutableLiveData<*>.notifyObserver() {
        this.value = this.value
    }
}