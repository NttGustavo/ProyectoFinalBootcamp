package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Resenias

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Data.ReseniasRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.Resenia
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Home.HomeViewModel

class ReseniasViewModel(var context: Context) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val reseniasList = MutableLiveData<List<Resenia>>()
    val firebase = Firebase(context)
    val reseniasRepository = ReseniasRepository(firebase)

    //cargar Resenias
    fun loadResenias(){
        isLoading.value = true
        reseniasRepository.getResenias(object : getReseniasCallback{
            override fun onResponse(list_resenias: MutableList<Resenia>) {
                when(list_resenias.isEmpty()){
                    true -> {
                        isLoading.value = false
                        reseniasList.value = list_resenias
                    }
                    false -> {

                        isLoading.value = false
                        reseniasList.value = list_resenias
                    }
                }
            }
        })
    }

    //agregar Resenia
    fun addResenia(){

    }
}

//Callback al obtener Reseña
interface getReseniasCallback {
    fun onResponse(list_resenias: MutableList<Resenia>)

}

//Factory del VM
class ReseniasViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReseniasViewModel::class.java)) {
            return ReseniasViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
