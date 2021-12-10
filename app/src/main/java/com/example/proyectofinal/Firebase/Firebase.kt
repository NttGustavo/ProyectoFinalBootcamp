package com.example.proyectofinal.Firebase

import android.content.Context
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Profile.getImageCallback
import com.example.proyectofinal.Utils.Encrypt
import com.example.proyectofinal.Utils.TextUtils
import com.google.firebase.database.*

class Firebase(var context: Context){

    val secretKey = "662ede816988e58fb6d057d9d85605e0"
    var database : FirebaseDatabase
    var dbUserReference : DatabaseReference
    var dbLugaresReference : DatabaseReference
    var dbReseniasReference : DatabaseReference

    init {
        database            = FirebaseDatabase.getInstance()
        dbUserReference     = database.reference.child("User")
        dbLugaresReference  = database.reference.child("Lugares")
        dbReseniasReference = database.reference.child("Reseñas")
    }

    //Cambiar foto de perfil
    fun updatePhoto(getImageCallback: getImageCallback, user: User, url: String ){
        val emailEncriptado = TextUtils.correccionEncrypt(Encrypt.encrypt(user.email,secretKey).toString())
        dbUserReference.child(emailEncriptado).child("imagen").setValue(url)
            .addOnSuccessListener {
                getImageCallback.onResponse("OK")
            }
            .addOnFailureListener {
                getImageCallback.onResponse("ERROR")
            }

    }
}
