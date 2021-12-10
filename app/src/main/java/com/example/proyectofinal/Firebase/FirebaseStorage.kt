package com.example.proyectofinal.Firebase

import android.content.Context
import android.net.Uri
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Profile.getUrlCallback
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseStorage(context: Context) {


    private var storage: FirebaseStorage
    private var storageRef: StorageReference

    init {
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference().child("perfiles")
    }

    fun addImage(getUrlCallback: getUrlCallback, nombre: String, imagen: Uri){

        storageRef.child(nombre).putFile(imagen).addOnCompleteListener { task ->

            storageRef.child(nombre).downloadUrl.addOnSuccessListener {
                val url = it.toString()
                getUrlCallback.onResponse("OK",url)
            }.addOnFailureListener{
                getUrlCallback.onResponse("ERROR","")
            }
        }
    }

}