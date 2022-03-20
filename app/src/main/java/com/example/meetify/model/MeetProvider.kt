package com.example.meetify.model

import com.google.android.gms.maps.model.LatLng

class MeetProvider {

    companion object {
        public fun getMeets(): List<MeetModel> {
            return meets
        }
        public fun addMeet(_meetToAdd:MeetModel){
            meets.add(_meetToAdd)
        }
        private val meets = mutableListOf<MeetModel>(
            MeetModel(
                1,
                "Plaza Cataluña",
                20,
                15,
                2,
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit.",
                LatLng(41.387027, 2.170071)
            ),
            MeetModel(
                2,
                "Arc de Triomf",
                10,
                14,
                2,
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit.",
                LatLng(41.390997, 2.180766)
            ),
            MeetModel(
                3,
                "La Maquinista",
                50,
                9,
                2,
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit.",
                LatLng(41.442391, 2.197628)
            ),
            MeetModel(
                4,
                "Bunquers del Carmel",
                100,
                10,
                2,
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit.",
                LatLng(41.419298, 2.161717)
            ),
            MeetModel(
                5,
                "Virrey Amat",
                40,
                20,
                2,
                "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit.",
                LatLng(41.429997, 2.174821)
            )
        )

    }
}