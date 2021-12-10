package com.example.proyectofinal.Data

import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.UI.MainUI.FragmentsMainUI.Repassword.RepasswordCallback
import com.example.proyectofinal.UI.MainUI.FragmentsMainUI.Repassword.VerifyAnswerCallback
import com.example.proyectofinal.UI.MainUI.FragmentsMainUI.Repassword.VerifyPasswordCallback
import com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignIn.SigninCallback
import com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignUp.SignupCallback
import com.example.proyectofinal.Utils.Encrypt
import com.example.proyectofinal.Utils.TextUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UsuariosRepository(var firebase: Firebase) {

    val secretKey = "662ede816988e58fb6d057d9d85605e0"

    //Login usuario
    fun loginUser(signinCallback: SigninCallback, email: String, password: String){
        firebase.dbUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(email,secretKey).toString())
                if(snapshot.child(emailEncriptado).exists()){
                    val usuario = snapshot.child(emailEncriptado).getValue(User::class.java)
                    if(password == (usuario?.password)){
                        signinCallback.onResponse("LOGIN OK", usuario)
                    }
                    else{
                        signinCallback.onResponse("ERROR PASSWORD")
                    }
                }
                else{
                    signinCallback.onResponse("NO USER")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                signinCallback.onResponse("ERROR")

            }

        })
    }

    //Registrar nuevo usuario
    fun  insertNewUser (signupCallback: SignupCallback, newUser: User) {
        firebase.dbUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(newUser.email,secretKey).toString())
                if(!snapshot.child(emailEncriptado).exists()){
                    val userBD = firebase.dbUserReference.child(emailEncriptado)
                    userBD.setValue(newUser)
                    signupCallback.onResponse("REGISTRO OK")
                }
                else{
                    signupCallback.onResponse("USUARIO EXISTENTE")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                signupCallback.onResponse("ERROR REGISTRO")
            }
        })
    }

    //Verificar email
    fun verifyEmail(repasswordCallback: RepasswordCallback, email: String){
        firebase.dbUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(email,secretKey).toString())
                if(snapshot.child(emailEncriptado).exists()){
                    val usuario = snapshot.child(emailEncriptado).getValue(User::class.java)
                    repasswordCallback.onResponse(usuario!!.pregunta, "USUARIO OK")

                }
                else{
                    repasswordCallback.onResponse("No hay pregunta","NO USER")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                repasswordCallback.onResponse("No hay pregunta","ERROR")
            }

        })
    }

    //Verificar Respuesta
    fun verifyAnswer(verifyAnswerCallback: VerifyAnswerCallback, email:String, respuesta: String){
        firebase.dbUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(email,secretKey).toString())
                val usuario = snapshot.child(emailEncriptado).getValue(User::class.java)
                if(respuesta == (usuario?.respuesta)){
                    verifyAnswerCallback.onResponse("ANSWER OK")
                }
                else{
                    verifyAnswerCallback.onResponse("ANSWER ERROR")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                verifyAnswerCallback.onResponse("ERROR")
            }

        })
    }

    //Cambiar Contraseña
    fun changePassword(verifyPasswordCallback : VerifyPasswordCallback, email:String, newPassword: String){
        val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(email,secretKey).toString())
        firebase.dbUserReference.child(emailEncriptado).child("password").setValue(newPassword).addOnSuccessListener {
            verifyPasswordCallback.onResponse("OK")
        }.addOnFailureListener{
            verifyPasswordCallback.onResponse("ERROR")
        }
    }
}