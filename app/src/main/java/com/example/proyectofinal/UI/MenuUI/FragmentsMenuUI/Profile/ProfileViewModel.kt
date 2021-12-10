package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Firebase.FirebaseStorage
import com.example.proyectofinal.Models.User

class ProfileViewModel(context: Context) : ViewModel() {
    val changePhoto = MutableLiveData<Photo>()
    val isLoading = MutableLiveData<Boolean>()
    val isAvailablePhoto = MutableLiveData<Boolean>()
    val firebaseStorage = FirebaseStorage(context)
    val firebase = Firebase(context)
    fun uploadImage(imageUri: Uri?){
        if(imageUri != null){
            isAvailablePhoto.value = true
            isLoading.value = true
            firebaseStorage.addImage(object : getUrlCallback {
                override fun onResponse(response: String,url: String) {
                    when(response){
                        "OK" -> {
                            actualizarPerfil(url)
                        }
                        "ERROR" -> {
                            isLoading.value = false
                            changePhoto.value = Photo(false, "Error en la actualizacion de la foto")
                        }
                    }
                }
            }, Comun.currentUser.nombre, imageUri!!)
        }
        else{
            isAvailablePhoto.value = false
        }
    }

    private fun actualizarPerfil(url: String) {
        firebase.updatePhoto(object : getImageCallback{
            override fun onResponse(response: String) {
                when (response){
                    "OK" -> {
                         Comun.currentUser.imagen = url
                        changePhoto.value = Photo(true, "Foto actualizada correctamente")
                        isLoading.value = false
                    }
                    "ERROR" -> {
                        isLoading.value = false
                        changePhoto.value = Photo(false, "Error en la actualizacion de la foto")
                    }
                }
            }
        }, Comun.currentUser, url)
    }
}

//Clase validadora de foto
class Photo( var valido:Boolean, var respuesta:String)

//Callback para obtencion de imagen
interface getImageCallback {
    fun onResponse(response: String)
}

//Callback para obtner url de imagen
interface getUrlCallback {
    fun onResponse(response: String, url: String)
}

//Factory del VM
class ProfileViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

