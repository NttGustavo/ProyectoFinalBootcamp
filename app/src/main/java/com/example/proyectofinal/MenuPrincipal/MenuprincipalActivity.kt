package com.example.proyectofinal.MenuPrincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyectofinal.Mapas.MapsActivity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMenuprincipalBinding

class MenuprincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuprincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuprincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.wishlistFragment, R.id.descriptionsFragment, R.id.profileFragment)
        )
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun gotoMaps(latitud: String, longitud: String){
        Intent(this, MapsActivity::class.java).also {
            it.putExtra("LATITUD",latitud)
            it.putExtra("LONGITUD",longitud)
            startActivity(it)
        }
    }
}