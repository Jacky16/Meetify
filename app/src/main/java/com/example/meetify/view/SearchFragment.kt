package com.example.meetify.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.meetify.R
import com.example.meetify.databinding.SearchFragmentBinding
import com.example.meetify.model.DefaultClusterRenderer
import com.example.meetify.model.MeetProvider
import com.example.meetify.model.MyMeetCluster
import com.example.meetify.viewmodel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager

class SearchFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: SearchViewModel
    lateinit var binding: SearchFragmentBinding

    //Google maps variables
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null
    private lateinit var clusterManager: ClusterManager<MyMeetCluster?>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMapFragment()
        onClickButtonLocation()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onMapReady(_map: GoogleMap) {
        googleMap = _map
        setUpMap()
        enableLocation()
        setUpClusterer()
        //addItems()
    }

    //region Main functions
    private fun onClickButtonLocation() {
        binding.btnCurrentLocation.setOnClickListener {
            moveToCurrentLocation()
        }
    }

    fun moveToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            currentLocation?.let {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it, 18f),
                    1000,
                    null
                )
            }
        }
    }

    //endregion

    //region Inits functions

    @SuppressLint("PotentialBehaviorOverride")
    private fun setUpClusterer() {
        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, googleMap)

        //Modificanco el renderer de los clusters por uno custom
        val clusterRenderer = DefaultClusterRenderer(context,googleMap, clusterManager)
        clusterManager.setRenderer(clusterRenderer)

        // Point the map's listeners at the listeners implemented by the cluster
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)

        // Add cluster items (markers) to the cluster manager.
        addItems()
    }

    fun addItems() {
        MeetProvider.getMeets().forEach { meetToAdd ->
            clusterManager.addItem( MyMeetCluster(meetToAdd))
        }

    }

    private fun setUpMap() {
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = false
        googleMap.uiSettings.isTiltGesturesEnabled = false
        googleMap.isBuildingsEnabled = false
        googleMap.isIndoorEnabled = false
    }

    private fun createMapFragment() {

        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        //Init current location and move to camera to her
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                currentLocation?.let {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
                }
            }
        }
    }


    //endregion FU functions_functions

}