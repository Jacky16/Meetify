package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng

data class MeetModel( val id: Int,
                      val name: String,
                      val people: Int,
                      val hour: Int,
                      val description:String,
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
