package com.example.proyectofinal.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.MenuPrincipal.MenuprincipalActivity
import com.example.proyectofinal.Repassword.RepasswordActivity
import com.example.proyectofinal.SignUp.SignupActivity
import com.example.proyectofinal.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebase = Firebase(this)
        firebase.initFirebase()
        firebase.initUserDatabase()

        binding.buttonSignin.setOnClickListener{
            val email = binding.editTextEmailSignin.text.toString()
            val password = binding.editTextPasswordSignin.text.toString()
            val user = User(email,password)

            firebase.loginUser(user)

        }

        binding.buttonRegister.setOnClickListener{
            gotoRegistro()
        }

        binding.tvForgotPassword.setOnClickListener {
            gotoForgotPassword()
        }


    }

    private fun gotoRegistro() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun gotoForgotPassword(){
        val intent = Intent(this, RepasswordActivity::class.java)
        startActivity(intent)
    }

    fun gotoActividadPrincipal() {
        val intent = Intent(this, MenuprincipalActivity::class.java)
        startActivity(intent)
    }
}