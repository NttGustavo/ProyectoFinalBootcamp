package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignIn

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Data.UsuariosRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.Utils.TextUtils

class SigninViewModel(var context: Context) : ViewModel(){

    val isValid = MutableLiveData<Valido>()
    val isLoading = MutableLiveData<Boolean>()
    val user = MutableLiveData<respuestaUser>()

    val firebase = Firebase(context)
    val userRepository = UsuariosRepository(firebase)

    //guardar Datos en local
    fun guardarDatos(user: User, isChecked: Boolean){
        when(isChecked){
            true -> {
                val sharedPreferences: SharedPreferences = context.getSharedPreferences("usuario_guardado", Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putString("email",user.email)
                editor.putString("password",user.password)
                editor.apply()
                editor.commit()
            }
        }
    }

    fun verficarUsuarioGuardado(){
        val sharedPref: SharedPreferences = context.getSharedPreferences("usuario_guardado", Context.MODE_PRIVATE)
        val email_guardado = sharedPref.getString("email","null")
        val password_guardado = sharedPref.getString("password","null")
        if(email_guardado != "null" && password_guardado != "null" ){
            if (password_guardado != null && email_guardado != null) {
                LoginUser(email_guardado,password_guardado)
            }
        }

    }

    ///Validacion de los campos email y password
    fun ValidarCampos(email: String, password: String):Boolean{
        val emptyUser = TextUtils.isEmptyUser(email, password)
        val isEmail = TextUtils.containEmail(email)
        if(emptyUser){
            isValid.value = Valido(false, "Campos incompletos")
            return false
        }
        else if(!isEmail){
            isValid.value = Valido(false, "Correo invalido")
            return false
        }
        isValid.value = Valido(true, "")
        return true
    }

    ///Llamar a firebase para el login
    fun LoginUser(email: String, password: String){
        isLoading.value = true
        userRepository.loginUser(object : SigninCallback{
            override fun onResponse(resultado: String, usuario: User?) {
                when(resultado){
                    "LOGIN OK" -> {
                        user.value = respuestaUser(true, usuario)
                        isLoading.value = false
                    }
                    "ERROR PASSWORD" -> {
                        user.value = respuestaUser(false, null)
                        isLoading.value = false
                    }
                    "NO USER" -> {
                        user.value = respuestaUser(false, null)
                        isLoading.value = false
                    }
                    "ERROR" -> {
                        user.value = respuestaUser(false, null)
                        isLoading.value = false
                    }
                }
            }
        },email,password)
    }

}

//funcion callback, para la respuesta del login
interface SigninCallback {
    fun onResponse(resultado: String, user: User ?= User())
}

//Clases observables
class Valido(var valido:Boolean, var mensaje:String)
class respuestaUser(var valido: Boolean, var user: User ?)

//Factory de VM
class SinginViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SigninViewModel::class.java)){
            return SigninViewModel(context) as T
        }
        throw IllegalArgumentException("Unkown VM class")
    }

}