package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng

data class MeetModel(val id: Int,
                     var name: String,
                     val people: Int,
                     var hour: Int,
                     var date:Int,
                     var description:String,
                     val position: LatLng,
                     val persons:MutableList<PersonModel> = mutableListOf(
                          PersonModel("Guillermo","Barrasa"),
                          PersonModel("Pedro","Fernandez"),
                          PersonModel("Dieko","Meña"),
                          PersonModel("Guillermo","Barrasa"),
                          PersonModel("Pedro","Fernandez"),
                          PersonModel("Guillermo","Barrasa"),
                          PersonModel("Pedro","Fernandez"),
                          PersonModel("Guillermo","Barrasa"),
                          PersonModel("Pedro","Fernandez"),
                          PersonModel("Guillermo","Barrasa"),
                          PersonModel("Pedro","Fernandez"),
                      ))
