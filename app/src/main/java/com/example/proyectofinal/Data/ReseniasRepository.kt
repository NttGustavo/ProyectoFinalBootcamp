package com.example.proyectofinal.Data

import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.Resenia
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Favoritos.ReseniaCallback
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Resenias.getReseniasCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ReseniasRepository(var firebase: Firebase) {

    //Obtener Reseñas
    fun getResenias(getReseniasCallback: getReseniasCallback){
        firebase.dbReseniasReference.addListenerForSingleValueEvent(object : ValueEventListener {
            var resenias = ArrayList<Resenia>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (reseniaSnapshot in snapshot.children) {
                    val resenia = reseniaSnapshot.getValue(Resenia::class.java)
                    resenia?.let { resenias.add(it) }
                }
                getReseniasCallback.onResponse(resenias)
            }

            override fun onCancelled(error: DatabaseError) {
                getReseniasCallback.onResponse(resenias)
            }

        })
    }

    //Agregar Reseña
    fun setResenia(reseniaCallback: ReseniaCallback, resenia: Resenia){
        firebase.dbReseniasReference.child(resenia.id.toString()).setValue(resenia).addOnSuccessListener {
            reseniaCallback.onResponse("OK")
        }.addOnFailureListener{
            reseniaCallback.onResponse("ERROR")
        }

    }
}