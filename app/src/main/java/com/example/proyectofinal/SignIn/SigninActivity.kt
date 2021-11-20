package com.example.proyectofinal.SignIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.proyectofinal.MenuPrincipal.MenuprincipalActivity
import com.example.proyectofinal.SignUp.SignupActivity
import com.example.proyectofinal.Utils.TextUtils
import com.example.proyectofinal.databinding.ActivitySigninBinding

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signin)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignin.setOnClickListener{
            val email = binding.editTextEmailSignin.text.toString()
            val password = binding.editTextPasswordSignin.text.toString()
            val user = User(email,password)
            val isEmpty = TextUtils.isEmptyUser(user.email, user.password)
            val isLong = TextUtils.isLong(user.password)
            val isEmail = TextUtils.containEmail(user.email)
            if(TextUtils.userisValidated(isEmpty, isLong, isEmail)){
                //validar en Firebase
                gotoActividadPrincipal()
            }
            else{
                Toast.makeText(this,"Debe ingresar bien los datos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonRegister.setOnClickListener{
            gotoRegistro()
        }


    }

    private fun gotoRegistro() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun gotoActividadPrincipal() {
        val intent = Intent(this, MenuprincipalActivity::class.java)
        startActivity(intent)
    }
}