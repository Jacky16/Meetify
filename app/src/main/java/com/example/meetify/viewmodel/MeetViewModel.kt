package com.example.meetify.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetify.model.MeetModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.ArrayList

class MeetViewModel : ViewModel() {
    var meet: MutableLiveData<MeetModel> = MutableLiveData()
    var db = FirebaseFirestore.getInstance()
    public fun getMeetById(id: String) {
        //Get meet from firestore by id
        db.collection("meets").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dateTime = document.get("dateTime") as Timestamp
                    val geoPoint = document.getGeoPoint("position")
                    val position = LatLng(geoPoint?.latitude ?: 0.0, geoPoint?.longitude ?: 0.0)
                    val peopleInMeet = document.get("peopleInMeet") as ArrayList<String>?
                    val idOwner = document.get("idOwner") as String

                    //Init MeetModel with data from firebase
                    val _meet = MeetModel(
                        document.id,
                        document.get("title").toString(),
                        dateTime.toDate(),
                        document.get("description").toString(),
                        position,
                        idOwner,
                        peopleInMeet
                    )
                    meet.value = _meet
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting meet", exception)
            }

    }

    public fun joinMeet(_meet: MeetModel) {
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val documentReference = db.collection("meets").document(_meet.id!!)

        userID?.let {
            val usersList = arrayListOf<String>()
            usersList.add(userID)
            val hashMap = hashMapOf(
                "peopleInMeet" to usersList
            )
            documentReference.set(hashMap, SetOptions.merge())
                //Asignar el user la Meet donde acaba de unirse
                .addOnSuccessListener {
                    assingJoinedMeet(_meet)
                }

        }
    }

    public fun assingJoinedMeet(_meet: MeetModel) {
        val listJoinedMeets = ArrayList<String>()
        listJoinedMeets.add(_meet.id!!)
        val ownerMeets = hashMapOf(
            "OwnerMeets" to listJoinedMeets
        )
        db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .set(ownerMeets)
    }

    public fun checkOwnerMeet(meet: MeetModel):Boolean
    {
        val userID = FirebaseAuth.getInstance().currentUser?.uid!!
        return userID == meet.idOwner
    }


}
