package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng
import java.sql.Timestamp
import java.util.*

data class MeetModel(
    var id: String? = null,
    var title: String? = null,
    var dateTime: Date? = null,
    var description: String? = null,
    var position: LatLng? = null,
) {

    constructor(
        title: String,
        dateTime: Date,
        description: String,
        position: LatLng?
    ) : this(null, title, dateTime, description, position)
}
