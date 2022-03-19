package com.example.meetify.model

import android.content.Context
import com.example.meetify.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class DefaultClusterRenderer(
    context: Context?,
    map: GoogleMap?,
    clusterManager: ClusterManager<MyMeetCluster?>?
) : DefaultClusterRenderer<MyMeetCluster?>(context, map, clusterManager) {
    protected override fun onBeforeClusterItemRendered(item: MyMeetCluster, markerOptions: MarkerOptions) {
        // use this to make your change to the marker option
        // for the marker before it gets render on the map

    }
    protected override fun onClusterItemRendered(clusterItem: MyMeetCluster, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.isVisible = true
        marker.hideInfoWindow()
    }
}