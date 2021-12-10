package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Data.LugaresRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Data.Local.Entity.LugarEntity

class HomeViewModel(var context: Context) : ViewModel(){
    val isLoading = MutableLiveData<Boolean>()
    val lugaresList = MutableLiveData<List<LugarEntity>>()
    val firebase = Firebase(context)
    val lugaresRepository = LugaresRepository(firebase)

    //cargar Lugares
    fun loadLugares(){
        isLoading.value = true
        lugaresRepository.getLugares(object: getLugaresCallback{
            override fun onResponse(list_lugares: MutableList<LugarEntity>) {
                when(list_lugares.isEmpty()){
                    true -> {
                        isLoading.value = false
                        lugaresList.value = list_lugares
                    }
                    false -> {

                        isLoading.value = false
                        lugaresList.value = list_lugares
                    }
                }
            }

        })
    }
}

//Callback, para obtencion de lugares
interface getLugaresCallback {
    fun onResponse(list_lugares: MutableList<LugarEntity>)
}

//Factory del VM
class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}