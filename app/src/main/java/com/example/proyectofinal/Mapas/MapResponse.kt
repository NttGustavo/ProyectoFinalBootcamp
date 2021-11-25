package com.example.proyectofinal.Mapas

import com.example.proyectofinal.Models.Route
import com.google.gson.annotations.SerializedName

data class MapResponse (
        @SerializedName("status") var status: String,
        @SerializedName("routes") var routes: List<Route>){
}