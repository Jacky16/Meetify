package com.example.meetify.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.meetify.R
import com.example.meetify.databinding.SearchFragmentBinding
import com.example.meetify.model.clusters.DefaultClusterRenderer
import com.example.meetify.model.MeetProvider
import com.example.meetify.model.clusters.MyMeetCluster
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.maps.android.clustering.ClusterManager

class SearchFragment : Fragment(), OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<MyMeetCluster?> {

    private lateinit var viewModel: SearchViewModel
    lateinit var binding: SearchFragmentBinding

    //Google maps variables
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null
    private lateinit var clusterManager: ClusterManager<MyMeetCluster?>
    var placeHolderMarker: Marker? = null


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
        collapseBottomSheetInfoMeet()
        bottomSheetCreateMeetManager()

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

        googleMap.setOnMapClickListener {
            collapseBottomSheetInfoMeet()
        }
        googleMap.setOnMapLongClickListener {
            expandeBottomSheetCreateMeet()
            placeHolderMarker = googleMap.addMarker(MarkerOptions().position(it))
        }

    }


    //region Main functions

    override fun onClusterItemClick(item: MyMeetCluster?): Boolean {
        BottomSheetBehavior.from(binding.bsInfoMeetInclude.bsMeet).apply {
            peekHeight = 550
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        setInfoOnBottomSheetMeet(item)
        return true
    }

    private fun onClickButtonLocation() {
        binding.btnCurrentLocation.setOnClickListener {
            moveToCurrentLocation()
        }
    }

    private fun setInfoOnBottomSheetMeet(item: MyMeetCluster?) {
        item?.getMeet()?.let {
            val bind = binding.bsInfoMeetInclude
            bind.tvTitleMeet.text = it.name
            bind.tvDescMeet.text = it.description
            bind.tvHour.text = it.hour.toString() + ":00"
            bind.tvPeopleCount.text = it.persons.size.toString()

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

    private fun collapseBottomSheetInfoMeet() {
        BottomSheetBehavior.from(binding.bsInfoMeetInclude.bsMeet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_HIDDEN
            this.isHideable = true

        }
    }

    private fun closeKeyBoard() {
        val view = this.requireActivity().currentFocus
        if (view != null) {
            val imm = getSystemService(
                requireContext(),
                InputMethodManager::class.java
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun bottomSheetCreateMeetManager() {
        val b = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    2 -> {
                        closeKeyBoard()
                        placeHolderMarker?.remove()
                        Toast.makeText(context,bottomSheet.findViewById<TextInputEditText>(R.id.titl_name_meet).text, Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        }
        BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_HIDDEN
            this.isHideable = true
        }.addBottomSheetCallback(b)
    }

    private fun expandeBottomSheetCreateMeet() {
        BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
            this.setPeekHeight(1000)

        }
    }
    //endregion

    //region Inits functions

    private fun setUpClusterer() {
        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, googleMap)

        //Modificanco el renderer de los clusters por uno custom
        val clusterRenderer = DefaultClusterRenderer(context, googleMap, clusterManager)
        clusterManager.setRenderer(clusterRenderer)

        // Point the map's listeners at the listeners implemented by the cluster
        googleMap.setOnCameraIdleListener(clusterManager)

        clusterManager.setOnClusterItemClickListener(this)

        // Add cluster items (markers) to the cluster manager.
        addMarkersMeets()
    }

    fun addMarkersMeets() {
        MeetProvider.getMeets().forEach { meetToAdd ->
            clusterManager.addItem(MyMeetCluster(meetToAdd))
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