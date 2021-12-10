package com.example.proyectofinal.UI.DetalleUI

import android.content.Context
import androidx.lifecycle.*
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.Data.FavoritosRepository
import com.example.proyectofinal.Data.Local.FavoritosDatabase
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LugarViewModel(context: Context): ViewModel() {

    val isLoading = MutableLiveData<Boolean>()
    val favoritosList : MutableList<LugarEntity> = mutableListOf()
    val agregarValido  = MutableLiveData<Boolean>()

    val lugarDao = FavoritosDatabase.getDatabase(context).lugarDao()
    val repository = FavoritosRepository(lugarDao)

    fun getFavoritos(){
       viewModelScope.launch{
           try{
               isLoading.value = true
               val result = withContext(Dispatchers.IO){
                   repository.readAllData(Comun.currentUser.nombre)
               }
               favoritosList.clear()
               favoritosList.addAll(result)
               isLoading.value = false
           }
           catch (exeption: Exception){
               isLoading.value =false
           }
       }
    }

    fun insertFavorito(lugarEntity: LugarEntity){
        favoritosList.forEach{ lugar ->
            if(lugar.id == lugarEntity.id){
                agregarValido.value = false
                return
            }
        }
        agregarValido.value = true
        lugarEntity._user = Comun.currentUser.nombre
        viewModelScope.launch(Dispatchers.IO){
            repository.addLugar(lugarEntity)
            getFavoritos()
        }
    }



}

class LugarViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LugarViewModel::class.java)) {
            return LugarViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}