package com.example.proyectofinal.Data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectofinal.Data.Local.DAO.FavoritosDAO
import com.example.proyectofinal.Data.Local.Entity.LugarEntity

@Database(entities = [LugarEntity::class], version = 2, exportSchema = false)
abstract class FavoritosDatabase: RoomDatabase(){

    abstract  fun lugarDao(): FavoritosDAO

    companion object{
        private var INSTANCE: FavoritosDatabase? = null

        fun getDatabase(context: Context): FavoritosDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context): FavoritosDatabase {
            return Room.databaseBuilder(context.applicationContext, FavoritosDatabase::class.java, "lugares_database") .fallbackToDestructiveMigration().build()
        }
    }
}