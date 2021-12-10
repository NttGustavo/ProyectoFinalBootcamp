package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignUp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Data.UsuariosRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.Utils.TextUtils

class SignupViewModel(context: Context): ViewModel() {
    val isValid = MutableLiveData<ValidoNewUser>()
    val isLoading = MutableLiveData<Boolean>()
    val isResgister = MutableLiveData<respuestaRegistro>()

    val firebase = Firebase(context)
    val userRepository = UsuariosRepository(firebase)

    fun ValidarCampos(nombre: String, email: String, telefono: String, password: String, confirmarPassword: String,pregunta: String, respuesta: String): Boolean{
        val isEmpty = TextUtils.isEmptyNewUser(nombre, email, telefono, password, confirmarPassword,pregunta, respuesta)
        val isLong = TextUtils.isLong(password)
        val isEmail = TextUtils.containEmail(email)
        val isSame = TextUtils.isPasswordSame(password, confirmarPassword)
        val isPhone = TextUtils.isPhone(telefono)
        if(isEmpty){
            isValid.value = ValidoNewUser(false,"Campos incompletos")
            return false
        }
        else if(!isLong){
            isValid.value = ValidoNewUser(false,"Contraseña debe tener al menos 6 caracteres")
            return false
        }
        else if(!isEmail){
            isValid.value = ValidoNewUser(false,"Correo invalido")
            return false
        }
        else if(!isSame){
            isValid.value = ValidoNewUser(false,"Las contraseñas no coinciden")
            return false
        }
        else if(!isPhone){
            isValid.value = ValidoNewUser(false,"Debe colocar un telefono")
            return false
        }
        isValid.value = ValidoNewUser(true,"")
        return true
    }

    ///Llamar a firebase para Registro
    fun RegisterNewUser(newUser: User){
        isLoading.value = true
        userRepository.insertNewUser(object : SignupCallback{
            override fun onResponse(resultado: String) {
                when(resultado){
                    "REGISTRO OK" -> {
                        isResgister.value = respuestaRegistro(true, "Usuario correctamente Registrado")
                        isLoading.value = false
                    }
                    "USUARIO EXISTENTE" -> {
                        isResgister.value = respuestaRegistro(false,"USUARIO EXISTENTE")
                        isLoading.value = false
                    }
                    "ERROR REGISTRO" -> {
                        isResgister.value = respuestaRegistro(false, "ERROR REGISTRO")
                        isLoading.value = false
                    }
                }
            }

        },newUser)
    }

}


//funcion callback, para la repsuesta del registro
interface SignupCallback {
    fun onResponse(resultado: String)
}


///Clases observables
class ValidoNewUser(var valido:Boolean, var mensaje:String)
class respuestaRegistro(var valido: Boolean, var resultado: String)

//Factory de VM
class SignupViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SignupViewModel::class.java)){
            return SignupViewModel(context) as T
        }
        throw IllegalArgumentException("Unkown VM class")
    }
}