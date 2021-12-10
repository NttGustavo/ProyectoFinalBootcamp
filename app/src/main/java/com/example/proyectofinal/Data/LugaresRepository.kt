package com.example.proyectofinal.Data

import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Home.getLugaresCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LugaresRepository(private val firebase: Firebase){
    //Obtener Lugares Turisticos
    fun getLugares(getLugaresCallback: getLugaresCallback){
        var lugares = ArrayList<LugarEntity>()
        firebase.dbLugaresReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (lugarSnapshot in snapshot.children) {
                    val lugar = lugarSnapshot.getValue(LugarEntity::class.java)
                    lugar?.let { lugares.add(it) }
                }
                getLugaresCallback.onResponse(lugares)
            }
            override fun onCancelled(error: DatabaseError) {
                getLugaresCallback.onResponse(ArrayList<LugarEntity>())
            }
        })
    }

/*
    suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine     { continuation ->
                addListenerForSingleValueEvent(FValueEventListener(
                    onDataChange = { continuation.resume(it) },
                    onError = { continuation.resumeWithException(it.toException()) }
                ))
            }
        }
    }

    suspend fun getLugares(): List<LugarEntity>{
        val lugares = ArrayList<LugarEntity>()
        val lugares_firebase = firebase.dbLugaresReference.getSnapshotValue()
        for(it in lugares_firebase.children){
            val lugar = it.getValue(LugarEntity::class.java)
            if (lugar != null) {
                Log.d("asd",lugar.toString())
                lugares.add(lugar)
            }
        }
        return lugares
    }
    */
}
/*
class FValueEventListener(
    val onDataChange: (DataSnapshot) -> Unit,
    val onError: (DatabaseError) -> Unit) : ValueEventListener {
    override fun onDataChange(data: DataSnapshot) = onDataChange.invoke(data)
    override fun onCancelled(error: DatabaseError) = onError.invoke(error)
}
*/
