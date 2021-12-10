package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.Repassword

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinal.Data.UsuariosRepository
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Utils.TextUtils

class RepasswordViewModel(context: Context): ViewModel() {
    val isUserExist = MutableLiveData<emailPregunta>()
    val isAnswerCorrect = MutableLiveData<Boolean>()
    val isNewpasswordCorrect = MutableLiveData<Boolean>()

    val isEmailValid = MutableLiveData<Boolean>()
    val isAnswerValid = MutableLiveData<Boolean>()
    val isPasswordValid = MutableLiveData<passwordValid>()

    val isLoading = MutableLiveData<Boolean>()

    val firebase = Firebase(context)
    val userRepository = UsuariosRepository(firebase)

    //Validar campo de pregunta
    fun ValidarCampoEmail(email: String):Boolean{
        if(email.isEmpty()){
            isEmailValid.value = false
            return false
        }
        isEmailValid.value = true
        return true
    }

    //Validar email en firebase
    fun userExists(email: String){
        isLoading.value = true
        userRepository.verifyEmail(object : RepasswordCallback{
            override fun onResponse(pregunta: String, resultado: String) {
                when(resultado){
                    "ERROR" -> {
                        isUserExist.value = emailPregunta(false, resultado)
                        isLoading.value = false
                    }
                    "NO USER" -> {
                        isUserExist.value = emailPregunta(false, resultado)
                        isLoading.value = false
                    }
                    "USUARIO OK" -> {
                        isUserExist.value = emailPregunta(true, pregunta)
                        isLoading.value = false
                    }
                }
            }

        },email)
    }

    //Validar campo respuesta
    fun ValidarCampoRespuesta(respuesta: String): Boolean{
        if(respuesta.isEmpty()){
            isAnswerValid.value = false
            return false
        }
        isAnswerValid.value = true
        return true
    }

    //Validar Resouesta en firebase
    fun answerExists(email: String,answer: String){
        isLoading.value = true
        userRepository.verifyAnswer(object :  VerifyAnswerCallback{
            override fun onResponse(resultado: String) {
                when(resultado){
                    "ANSWER OK" -> {
                        isAnswerCorrect.value = true
                        isLoading.value = false
                    }
                    "ANSWER ERROR" -> {
                        isAnswerCorrect.value = false
                        isLoading.value = false
                    }
                    "ERROR" -> {
                        isAnswerCorrect.value = false
                        isLoading.value = false
                    }
                }
            }
        }, email,answer)
    }

    //Validar campo Password
    fun ValidarCampoPassword(nuevoPassword: String): Boolean{
        val isLong = TextUtils.isLong(nuevoPassword)
        if(nuevoPassword.isEmpty()){
            isPasswordValid.value = passwordValid(false, "Completar campo")
            return false
        }
        else if(!isLong){
            isPasswordValid.value = passwordValid(false, "La contraseña debe tener al menos 6 caracteres")
            return false
        }
        isPasswordValid.value = passwordValid(true, "OK")
        return true
    }

    //Cmabiar de password en firebase
    fun changePassword(email: String, newPassword: String){
        isLoading.value = true
        userRepository.changePassword( object : VerifyPasswordCallback{
            override fun onResponse(resultado: String) {
                when(resultado){
                    "OK" -> {
                        isNewpasswordCorrect.value = true
                        isLoading.value = false
                    }
                    "ERROR" -> {
                        isNewpasswordCorrect.value = false
                        isLoading.value = false
                    }
                }
            }
        }, email, newPassword)

    }


}

//funcion callback verificacion de pregunta
interface RepasswordCallback {
    fun onResponse(pregunta:String,resultado: String)
}

//funcion callback verificacion de respuesta validadora
interface VerifyAnswerCallback{
    fun onResponse(resultado: String)
}

//funcion callback verificacion de respuesta validadora
interface VerifyPasswordCallback{
    fun onResponse(resultado: String)
}

//Clases observables

class emailPregunta(var valido: Boolean, var pregunta: String)
class passwordValid(var valido: Boolean, var mensaje: String)

//Factory VM
class RepasswordViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepasswordViewModel::class.java)){
            return RepasswordViewModel(context) as T
        }
        throw IllegalArgumentException("Unkown VM class")
    }
}