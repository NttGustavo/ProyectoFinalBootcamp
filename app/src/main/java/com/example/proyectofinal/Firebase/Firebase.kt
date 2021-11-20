package com.example.proyectofinal.Firebase

import android.app.Activity
import android.widget.Toast
import com.example.proyectofinal.SignUp.NewUser
import com.example.proyectofinal.Utils.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Firebase(var context: Activity){
    private lateinit var dbReference : DatabaseReference
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    //inicializar Firebase
    public fun initFirebase(){
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    //Inicializar BD de usuarios
    public fun initUserDatabase(){
        dbReference = database.reference.child("User")
    }

    //Insertar nuevo usuario
    public fun  insertNewUser (newUser: NewUser, confirmarPassword: String) {
        var isEmpty = TextUtils.isEmptyNewUser(newUser.nombre, newUser.email, newUser.telefono, newUser.password, confirmarPassword, newUser.pregunta, newUser.respuesta)
        var isLong = TextUtils.isLong(newUser.password)
        var isEmail = TextUtils.containEmail(newUser.email)
        var isSame = TextUtils.isPasswordSame(newUser.password, confirmarPassword)
        var isPhone = TextUtils.isPhone(newUser.telefono)
        if(TextUtils.isNewUserValidated(isEmpty, isLong, isEmail,isSame, isPhone)){
            var exists = false
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    children.forEach{
                        if(it.child("email").value.toString().equals(newUser.email)){
                            exists = true
                        }
                    }
                    if(!exists){

                        val userBD = dbReference.child(newUser.nombre)
                        userBD.setValue(newUser)
                        Toast.makeText(context,"Usuario correctamente registrado",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context,"el usuario existe",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}
