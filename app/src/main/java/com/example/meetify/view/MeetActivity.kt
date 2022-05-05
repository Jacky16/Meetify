package com.example.meetify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.databinding.ActivityMeetBinding
import com.example.meetify.model.MeetModel
import com.example.meetify.viewmodel.MeetViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MeetActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMeetBinding
    lateinit var map: GoogleMap
    var currentMeet: MeetModel? = null
    lateinit var recyclerView: RecyclerView
    val meetModelView: MeetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtras()

        initRecyclerView()

        initInfoMeet()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        //Button back
        binding.btnBack.setOnClickListener {
            finish()
        }


    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        //val adapter = PersonAdapter(meet.persons)
        //recyclerView.adapter = adapter
    }

    private fun getExtras() {
        val extras = intent.extras
        val id = extras?.getString("idMeet") ?: return finish()
        meetModelView.getMeetById(id)
        meetModelView.meet.observe(this) {
            currentMeet = it
            addMarkerToMeet()
            moveCameraToMeet()
            initInfoMeet()
        }
    }

    private fun initInfoMeet() {
        binding.tvTitleMeet.text = currentMeet?.title.toString()
        //Set the hour of the meet with formatter am/pm
        val dateTimeString = DateUtils.formatDateTime(this, currentMeet?.dateTime?.time ?:0L, DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
        binding.tvHourMeet.text = dateTimeString

        binding.tvDescMeet.text = currentMeet?.description.toString()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.isIndoorEnabled = false

        addMarkerToMeet()
        moveCameraToMeet()

        binding.btnLocation.setOnClickListener {
            moveCameraToMeet()
        }
    }

    private fun moveCameraToMeet() {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                currentMeet?.position ?: LatLng(0.0, 0.0), 18f
            )
        )
    }

    private fun addMarkerToMeet() {
        map.clear()
        map.addMarker(
            MarkerOptions()
                .position(
                    currentMeet?.position ?: LatLng(0.0, 0.0)
                )
        )
    }
}