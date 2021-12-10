package com.example.proyectofinal.Data

import com.example.proyectofinal.Data.Local.DAO.FavoritosDAO
import com.example.proyectofinal.Data.Local.Entity.LugarEntity

class FavoritosRepository(private val favoritosDAO: FavoritosDAO) {

    suspend fun readAllData (user_name : String): List<LugarEntity>{
        return favoritosDAO.readAllLugares(user_name)
    }

    suspend fun addLugar(lugarEntity: LugarEntity){
        favoritosDAO.addLugar(lugarEntity)
    }

    suspend fun deleteLugar(lugarEntity: LugarEntity){
        favoritosDAO.deleteLugar(lugarEntity)
    }
}