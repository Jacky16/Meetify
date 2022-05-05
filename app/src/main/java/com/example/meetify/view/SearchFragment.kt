package com.example.meetify.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.meetify.R
import com.example.meetify.databinding.SearchFragmentBinding
import com.example.meetify.model.CacheMeets
import com.example.meetify.model.MeetModel
import com.example.meetify.model.MeetProvider
import com.example.meetify.model.PersonModel
import com.example.meetify.model.clusters.DefaultClusterRenderer
import com.example.meetify.model.clusters.MyMeetCluster
import com.example.meetify.viewmodel.MeetViewModel
import com.example.meetify.viewmodel.MeetsViewModel
import com.example.meetify.viewmodel.SearchViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment(), OnMapReadyCallback,
    ClusterManager.OnClusterItemClickListener<MyMeetCluster?> {

    private lateinit var viewModel: SearchViewModel
    lateinit var binding: SearchFragmentBinding
    val meetsViewModel: MeetsViewModel by viewModels()
    //Google maps variables
    var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null
    private var clusterManager: ClusterManager<MyMeetCluster?>? = null
    lateinit var locationToAdd:LatLng
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
        initBottomSheetCreateMeet()

        binding.bsCreateMeetInclude.titlDate.setFocusable(false)
        binding.bsCreateMeetInclude.titlHour.setFocusable(false)

        initBottomSheetCreateMeet()
        //Create Meet Manager
        bottomSheetCreateMeetManager()
        meetsViewModel.getMeets()
        meetsViewModel.meets.observe(viewLifecycleOwner){
            addMarkersMeets()
        }
    }


    override fun onMapReady(_map: GoogleMap) {
        googleMap = _map
        setUpMap()
        enableLocation()
        setUpClusterer()

        googleMap?.setOnMapClickListener {
            collapseBottomSheetInfoMeet()
        }
        googleMap?.setOnMapLongClickListener {
            expandeBottomSheetCreateMeet()
            locationToAdd = it
        }
    }

    //region BottomSheet Create Meet functions
    private fun bottomSheetCreateMeetManager() {
        //Get current date with Date data
        val dateTime:Date = Calendar.getInstance().time

        //Button Done
        binding.bsCreateMeetInclude.btnCreateMeet.setOnClickListener {
            BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }

            // Get Name
            val titleMeet = binding.bsCreateMeetInclude.titlNameMeet.text.toString()

            //Get Description
            val descriptionMeet = binding.bsCreateMeetInclude.titlDescriptionMeet.text.toString()

            dateTime?.let { _dateTime ->
                addMeetToListAndMap(MeetModel(titleMeet,_dateTime,descriptionMeet,locationToAdd))
            }


        }
        //Cancel Button
        binding.bsCreateMeetInclude.btnCancelCreateMeet.setOnClickListener {
            BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        //Do a date picker with datetime
        binding.bsCreateMeetInclude.titlDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    val date = calendar.time
                    binding.bsCreateMeetInclude.titlDate.setText(date.toString())
                },
                dateTime.year,
                dateTime.month,
                dateTime.date
            )
            datePicker.show()
        }

        //Do a Hour picker
        binding.bsCreateMeetInclude.titlHour.setOnClickListener {
            val timePicker = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val time = calendar.time
                    val timeFormat = SimpleDateFormat("HH:mm")
                    val timeString = timeFormat.format(time)
                    binding.bsCreateMeetInclude.titlHour.setText(timeString)
                },
                dateTime.hours,
                dateTime.minutes,
                true
            )
            timePicker.show()
        }
    }

    private fun addMeetToListAndMap(meet: MeetModel) {
        //Add to map
        clusterManager?.addItem(MyMeetCluster(meet))

        //Add to list of meets
        MeetProvider.addMeet(meet)

        //Refresh Map to view the meet
        clusterManager?.cluster()
    }

    private fun expandeBottomSheetCreateMeet() {
        BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
            this.setPeekHeight(1000)
        }
    }

    private fun initBottomSheetCreateMeet() {
        val b = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        closeKeyBoard()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        }
        BottomSheetBehavior.from(binding.bsCreateMeetInclude.bsCreateMeet).apply {
            this.state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 0
            this.isHideable = true
            this.skipCollapsed = true

        }.addBottomSheetCallback(b)
    }

    //endregion

    //region BottomSheet Info Meet functions
    private fun collapseBottomSheetInfoMeet() {
        BottomSheetBehavior.from(binding.bsInfoMeetInclude.bsMeet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_HIDDEN
            this.isHideable = true

        }
    }

    private fun setInfoOnBottomSheetMeet(item: MyMeetCluster?) {
        item?.getMeet()?.let {
            val stringHour = "${it.dateTime?.hours.toString()}:${it.dateTime?.minutes.toString()}"
            val bind = binding.bsInfoMeetInclude
            bind.tvTitleMeet.text = it.title
            bind.tvDescMeet.text = it.description
            bind.tvHour.text = stringHour
            //bind.tvPeopleCount.text = it.persons.size.toString()

        }
    }

    //endregion

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

    private fun moveToCurrentLocation() {
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
                googleMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(it, 18f),
                    1000,
                    null
                )
            }
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
    //endregion

    //region Inits functions

    private fun setUpClusterer() {
        // Position the map.
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        clusterManager = ClusterManager(context, googleMap)

        //Modificanco el renderer de los clusters por uno custom
        val clusterRenderer = DefaultClusterRenderer(context, googleMap, clusterManager)
        clusterManager?.setRenderer(clusterRenderer)

        // Point the map's listeners at the listeners implemented by the cluster
        googleMap?.setOnCameraIdleListener(clusterManager)

        clusterManager?.setOnClusterItemClickListener(this)

        // Add cluster items (markers) to the cluster manager.
        addMarkersMeets()
    }

    private fun addMarkersMeets() {
        clusterManager?.clearItems()
        meetsViewModel.meets.value?.forEach {
            val meet = it
            val marker = MyMeetCluster(meet)
            clusterManager?.addItem(marker)
        }
    }

    private fun setUpMap() {
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        googleMap?.uiSettings?.isRotateGesturesEnabled = false
        googleMap?.uiSettings?.isTiltGesturesEnabled = false
        googleMap?.isBuildingsEnabled = false
        googleMap?.isIndoorEnabled = false
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
        googleMap?.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                currentLocation?.let {
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
                }
            }
        }
    }

    //endregion FU functions_functions

}