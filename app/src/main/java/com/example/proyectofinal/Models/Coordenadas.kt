package com.example.proyectofinal.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordenadas(var latitud: String? = "", var longitud: String? = "") : Parcelable