package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng

data class MeetModel(
    val id: Int = 0,
    var name: String = "",
    val people: Int =0,
    var hour: Int = 0,
    var date: Int = 0,
    var description: String = "",
    var position: LatLng = LatLng(0.0,0.0),
    val persons: MutableList<PersonModel> = mutableListOf(
        PersonModel("Guillermo", "Barrasa"),
        PersonModel("Pedro", "Fernandez"),
        PersonModel("Dieko", "Meña"),
        PersonModel("Guillermo", "Barrasa"),
        PersonModel("Pedro", "Fernandez"),
        PersonModel("Guillermo", "Barrasa"),
        PersonModel("Pedro", "Fernandez"),
        PersonModel("Guillermo", "Barrasa"),
        PersonModel("Pedro", "Fernandez"),
        PersonModel("Guillermo", "Barrasa"),
        PersonModel("Pedro", "Fernandez"),
    )

){



}
