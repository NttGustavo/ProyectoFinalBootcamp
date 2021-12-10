package com.example.proyectofinal.UI.Dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.proyectofinal.R

class LoadingDialog(var context: Context) {
    private lateinit var dialog: AlertDialog
    fun startDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = LayoutInflater.from(context)
        builder.setView(inflater.inflate(R.layout.loading_screen, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}