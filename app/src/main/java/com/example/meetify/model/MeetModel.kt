package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class MeetModel(
    var id: String?,
    var title: String,
    var dateTime: Calendar,
    var description: String,
    var position: LatLng
) {

    constructor(
        title: String,
        dateTime: Calendar,
        description: String,
        position: LatLng,
    ) : this(null, title, dateTime, description, position)
}
