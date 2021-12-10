package com.example.proyectofinal.Data.Local.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.Data.Local.Entity.LugarEntity

@Dao
interface FavoritosDAO {

    @Query("SELECT * FROM lugares_table WHERE _user = :user_name ORDER BY _id ASC")
    suspend fun readAllLugares(user_name : String): List<LugarEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLugar(lugarEntity: LugarEntity)

    @Delete
    suspend fun deleteLugar(lugarEntity: LugarEntity)

}