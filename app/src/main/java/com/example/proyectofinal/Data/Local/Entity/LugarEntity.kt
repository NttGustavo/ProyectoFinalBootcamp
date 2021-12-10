package com.example.proyectofinal.Data.Local.Entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyectofinal.Models.Coordenadas
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "lugares_table")
data class LugarEntity(
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0,
    var _user: String? = "",
    var nombre: String? = "",
    var duracion: String? = "",
    var departamento: String? = "",
    var id: String? = "",
    var imagen: String? = "",
    @Embedded
    var coordenadas: Coordenadas = Coordenadas("",""),
    var descripcion: String? = ""): Parcelable