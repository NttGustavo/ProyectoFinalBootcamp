package com.example.proyectofinal.SignUp

import android.util.Log
import com.example.proyectofinal.SignIn.User
import kotlin.math.log

class NewUser(var nombre:String, email:String, var telefono: String, password:String, var pregunta:String, var respuesta: String): User(email, password){


}