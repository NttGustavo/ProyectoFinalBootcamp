package com.example.proyectofinal.Data

import android.content.Context
import com.example.proyectofinal.Data.Remote.MapsApiService
import com.example.proyectofinal.Models.Route

class RutasRepository(var context: Context,private val mapsApiService: MapsApiService){
    suspend fun getRoutes(modo:String, transit: String, origin: String, destination: String, sensor: String, key: String) : List<Route>{
        val response = mapsApiService.getRoutes(modo, transit, origin, destination, sensor, key)

        if(response.isSuccessful) {
            val routesResponse = response.body()
            return routesResponse?.routes ?: emptyList()
        }
        else{
            throw Exception("Something went wrong")
        }
    }
}