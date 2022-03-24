package com.example.meetify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.R
import com.example.meetify.databinding.ActivityMeetBinding
import com.example.meetify.model.MeetModel
import com.example.meetify.model.MeetProvider
import com.example.tabs.Person.PersonAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MeetActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMeetBinding
    lateinit var meet: MeetModel
    lateinit var recyclerView: RecyclerView

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

        val adapter = PersonAdapter(meet.persons)
        recyclerView.adapter = adapter
    }

    private fun getExtras() {
        val extras = intent.extras

        val id = extras?.getInt("idMeet")
        meet = MeetProvider.getMeets().find {
            it.id == id
        }!!
    }

    private fun initInfoMeet() {
        binding.tvTitleMeet.text = meet.name.toString()
        binding.tvHourMeet.text = meet.hour.toString() + ":00"
        binding.tvDescMeet.text = meet.description.toString()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.isIndoorEnabled = false

        googleMap.addMarker(
            MarkerOptions()
                .position(meet.position)
                .title(meet.name)
        )

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    meet.position.latitude,
                    meet.position.longitude
                ), 18f
            )
        )
        binding.btnLocation.setOnClickListener {
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(meet.position, 18f),
                1000,
                null
            )
        }
    }
}