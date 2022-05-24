package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

data class MeetModel(
    var id: String? = null,
    var title: String? = null,
    var dateTime: Date? = null,
    var description: String? = null,
    var position: LatLng? = null,
    var idOwner: String,
    val peopleJoined:ArrayList<String>? = ArrayList(),
) {

    constructor(
        title: String,
        dateTime: Date,
        description: String,
        position: LatLng?,
        idOwner: String,

    ) : this(null, title, dateTime, description, position,idOwner)
}
