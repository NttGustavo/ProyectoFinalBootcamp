package com.example.proyectofinal.UI.DetalleUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.example.proyectofinal.UI.MapsUI.MapsActivity
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.databinding.ActivityLugarBinding
import com.google.android.material.snackbar.Snackbar

class LugarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLugarBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel : LugarViewModel by viewModels{
        LugarViewModelFactory(this)
    }

    private lateinit var lugarEntity: LugarEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLugarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = LoadingDialog(this)
        recuperatDatos()
        viewModel.getFavoritos()
        viewModel.agregarValido.observe(this, Observer(::existeFavorito))
        viewModel.isLoading.observe(this,Observer(::onLoadingChange))

        binding.btnGoToMap.setOnClickListener{
            gotoMaps(lugarEntity)
        }

        binding.btnSetWishlist.setOnClickListener{
            viewModel.insertFavorito(lugarEntity)
        }

    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun existeFavorito(agregarValido : Boolean){
        when(agregarValido){
            true ->{
                val snackbar = Snackbar.make(binding.root , "Agregado a favoritos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            false ->{
                val snackbar = Snackbar.make(binding.root , "Ya ha sio gregado a favoritos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    private fun recuperatDatos() {
        lugarEntity= intent.getParcelableExtra("lugarEntity")!!
        renderLugar(lugarEntity)
    }

    fun renderLugar(lugarEntity: LugarEntity){
        binding.lugarNombre.text = lugarEntity.nombre
        binding.lugarDescripcion.text = lugarEntity.descripcion
        binding.lugarDuracion.text = lugarEntity.duracion
        binding.lugarImagen.load(lugarEntity.imagen)
    }

    fun gotoMaps(lugarEntity: LugarEntity){
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("lugarEntity",lugarEntity)
        startActivity(intent)
    }
}