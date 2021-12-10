package com.example.proyectofinal.UI.MenuUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMenuprincipalBinding

class MenuprincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuprincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Saliendo de la app")
            setMessage("Desea salir de la app?")

            setPositiveButton("Yes") { _, _ ->
                finishAffinity()
                super.onBackPressed()
            }

            setNegativeButton("No"){alertDialog, _ ->
                alertDialog.dismiss()
            }

            setCancelable(true)
        }.create().show()
    }





}