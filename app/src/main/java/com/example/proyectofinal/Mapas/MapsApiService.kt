package com.example.proyectofinal.Mapas

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MapsApiService {
    @GET("maps/api/directions/json")
    suspend fun getRoutes(
        @Query("mode") mode:String,
        @Query("transit_routing_preferance") preferance:String,
        @Query("origin") origin:String,
        @Query("destination") destination:String,
        @Query("sensor") sensor: String,
        @Query("key") key: String,
        ):Response<MapResponse>
}