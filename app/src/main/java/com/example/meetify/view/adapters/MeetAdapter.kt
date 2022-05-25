package com.example.meetify.view.adapters

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.RecyclerView
import com.example.meetify.MeetActivity
import com.example.meetify.R
import com.example.meetify.databinding.ItemMeetBinding
import com.example.meetify.model.MeetModel
import com.example.meetify.viewmodel.MeetViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MeetAdapter(var meetList: List<MeetModel>) : RecyclerView.Adapter<MeetAdapter.MeetHolder>() {

    inner class MeetHolder(val view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {
        val binding = ItemMeetBinding.bind(view)
        var mapCurrent:GoogleMap? = null
        var mapView: MapView? = null
        private var meetVar: MeetModel? = null

        fun render(meet: MeetModel) {
           //Init mapview with fragment container
            mapView = binding.mapFragment
            mapView?.onCreate(null)
            mapView?.onResume()
            mapView?.getMapAsync(this)
            mapCurrent?.moveCamera(CameraUpdateFactory.newLatLngZoom(meet.position ?: LatLng(0.0,0.0), 15f))

            initInfo(meet)
            joinButton(meet)
            viewClick(meet)
        }

        private fun initInfo(meet: MeetModel) {
            binding.tvtitle.text = meet.title
            meetVar = meet
            val peopleInMeet = meet.peopleJoined?.size ?: 0
            binding.tvPeopleCount.text = peopleInMeet.toString()

            //Set datetime to tvHour
            val dateTimeString = DateUtils.formatDateTime(
                view.context,
                meet.dateTime?.time ?: 0L,
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE
            )
            binding.tvHour.text = dateTimeString
        }

        private fun viewClick(meet: MeetModel) {
            view.setOnClickListener {
                val intent = Intent(view.context, MeetActivity::class.java);
                intent.putExtra("idMeet", meet.id)
                view.context.startActivity(intent)
            }
        }

        private fun joinButton(meet: MeetModel) {
            binding.btnJoinMeet.setOnClickListener {
                val mvm = MeetViewModel()
                mvm.joinMeet(meet, view)

            }
        }

        override fun onMapReady(p0: GoogleMap) {
            MapsInitializer.initialize(view.context)
            mapCurrent = p0

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MeetHolder(layoutInflater.inflate(R.layout.item_meet, parent, false))
    }

    override fun onBindViewHolder(holder: MeetHolder, position: Int) {
        holder.render(meetList[position])
       //Move camera to meet position





    }

    override fun getItemCount(): Int {
        return meetList.size
    }

    fun updateMeets(it: ArrayList<MeetModel>) {
        meetList = it
        notifyDataSetChanged()
    }


}




