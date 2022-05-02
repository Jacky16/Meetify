package com.example.meetify.model.clusters

import com.example.meetify.model.MeetModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MyMeetCluster(
    meet: MeetModel
) : ClusterItem {
    private val myMeet: MeetModel

    public fun getMeet(): MeetModel {
        return myMeet
    }

    init {
        this.myMeet = meet
    }

    override fun getPosition(): LatLng {
        return myMeet.position!!
    }

    override fun getTitle(): String? {
        return myMeet.title
    }

    override fun getSnippet(): String? {
        return null
    }
}