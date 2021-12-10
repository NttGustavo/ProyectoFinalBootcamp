package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Favoritos

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.Data.FavoritosRepository
import com.example.proyectofinal.Data.Local.FavoritosDatabase
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.Data.ReseniasRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.Resenia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritosViewModel(context: Context): ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val favoritosList : MutableList<LugarEntity> = mutableListOf()
    val favoritosListLD = MutableLiveData<List<LugarEntity>>()
    val isDeleted = MutableLiveData<Boolean>()
    val isReseniaUpload = MutableLiveData<Boolean>()
    val lugarDao = FavoritosDatabase.getDatabase(context).lugarDao()
    val repository = FavoritosRepository(lugarDao)
    val firebase = Firebase(context)
    val reseniasRepository = ReseniasRepository(firebase)

    fun loadFavoritos(){
        viewModelScope.launch{
            try{
                isLoading.value = true
                val result = withContext(Dispatchers.IO){
                    repository.readAllData(Comun.currentUser.nombre)
                }

                favoritosList.clear()
                favoritosList.addAll(result)
                Log.d("cantidad", favoritosList.size.toString())
                favoritosListLD.value = favoritosList
                isLoading.value = false
            }
            catch (exeption: Exception){
                isLoading.value =false
            }
        }
    }

    fun deleteLugar(lugarEntity: LugarEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLugar(lugarEntity)
            loadFavoritos()
        }
    }

    fun addResenia(resenia: Resenia){
        isLoading.value = true
        reseniasRepository.setResenia(object : ReseniaCallback {
            override fun onResponse(response: String) {
                when(response){
                    "OK"->{
                        isLoading.value = false
                        isReseniaUpload.value = true
                    }
                    "ERROR"->{
                        isLoading.value = false
                        isReseniaUpload.value = false
                    }
                }
            }


        },resenia)
    }

}

//Callback al siubir una reseña
interface ReseniaCallback {
    fun onResponse(response: String)
}

class FavoritosViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            return FavoritosViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}