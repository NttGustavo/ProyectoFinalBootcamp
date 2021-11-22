package com.example.proyectofinal.Firebase

import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.Repassword.RepasswordActivity
import com.example.proyectofinal.SignIn.SigninActivity
import com.example.proyectofinal.SignIn.User
import com.example.proyectofinal.SignUp.NewUser
import com.example.proyectofinal.Utils.Encrypt
import com.example.proyectofinal.Utils.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Firebase(var context: Activity){
    val secret_key = "662ede816988e58fb6d057d9d85605e0"
    private lateinit var dbReference : DatabaseReference
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    //inicializar Firebase
    fun initFirebase(){
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    //Inicializar BD de usuarios
    fun initUserDatabase(){
        dbReference = database.reference.child("User")
    }

    //Login usuario
    fun loginUser(user: User){
        val isEmpty = TextUtils.isEmptyUser(user.email, user.password)
        val isLong = TextUtils.isLong(user.password)
        val isEmail = TextUtils.containEmail(user.email)


        if(TextUtils.userisValidated(isEmpty, isLong, isEmail)){
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email_encriptado = Encrypt.encrypt(user.email,secret_key).toString()
                    if(snapshot.child(email_encriptado).exists()){
                        val usuario = snapshot.child(email_encriptado).getValue(NewUser::class.java)
                        if(user.password.equals(usuario?.password)){
                            usuario?.let { saverCurrentUser(it) }
                            (context as SigninActivity).gotoActividadPrincipal()
                        }
                        else{
                            Toast.makeText(context,"La contraseña es incorrecta",Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(context,"No se ha encontrado el usuario",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error en el Login",Toast.LENGTH_SHORT).show()
                }

            })
        }
        else{
            Toast.makeText(context,"Debe ingresar bien los datos", Toast.LENGTH_SHORT).show()
        }
    }

    //Registrar nuevo usuario
    fun  insertNewUser (newUser: NewUser, confirmarPassword: String) {
        val isEmpty = TextUtils.isEmptyNewUser(newUser.nombre, newUser.email, newUser.telefono, newUser.password, confirmarPassword, newUser.pregunta, newUser.respuesta)
        val isLong = TextUtils.isLong(newUser.password)
        val isEmail = TextUtils.containEmail(newUser.email)
        val isSame = TextUtils.isPasswordSame(newUser.password, confirmarPassword)
        val isPhone = TextUtils.isPhone(newUser.telefono)

        if(TextUtils.isNewUserValidated(isEmpty, isLong, isEmail,isSame, isPhone)){
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email_encriptado = Encrypt.encrypt(newUser.email,secret_key).toString()
                    if(!snapshot.child(email_encriptado).exists()){
                        val userBD = dbReference.child(Encrypt.encrypt(newUser.email,secret_key).toString())
                        userBD.setValue(newUser)
                        Toast.makeText(context,"Usuario correctamente registrado",Toast.LENGTH_SHORT).show()

                    }
                    else{
                        Toast.makeText(context,"el usuario ya existe",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    //Guardar nuevo usuario
    fun saverCurrentUser(usuario: NewUser){
        Comun.currentUser = usuario
        Toast.makeText(context,"Bienvenido ${Comun.currentUser.nombre}",Toast.LENGTH_SHORT).show()
    }

    //Verificar email
    fun verifyEmail(email: String){
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val email_encriptado = Encrypt.encrypt(email,secret_key).toString()
                if(snapshot.child(email_encriptado).exists()){
                    val usuario = snapshot.child(email_encriptado).getValue(NewUser::class.java)
                    (context as RepasswordActivity).verificacionPaso1()
                    usuario?.let { (context as RepasswordActivity).recuperarPregunta(it.pregunta) }
                }
                else{
                    Toast.makeText(context,"No se ha encontrado el usuario",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error al verificar usuario",Toast.LENGTH_SHORT).show()
            }

        })
    }

    //Verificar Respuesta
    fun verifyAnswer(email:String, respuesta: String){
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val email_encriptado = Encrypt.encrypt(email,secret_key).toString()
                val usuario = snapshot.child(email_encriptado).getValue(NewUser::class.java)
                if(respuesta.equals(usuario?.respuesta)){
                    (context as RepasswordActivity).verificacionPaso2()
                }
                else{
                    Toast.makeText(context,"La respuesta no coincide",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error en respeusta verificadora",Toast.LENGTH_SHORT).show()
            }

        })
    }

    //Cambiar Contraseña
    fun changePassword(email:String, newPassword: String){
        val email_encriptado = Encrypt.encrypt(email,secret_key).toString()
        dbReference.child(email_encriptado).child("password").setValue(newPassword)
        Toast.makeText(context,"Contraseña Cambiada",Toast.LENGTH_SHORT).show()
        (context as RepasswordActivity).regresarLogin()
    }

}
