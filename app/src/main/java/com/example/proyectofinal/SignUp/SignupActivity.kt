package com.example.proyectofinal.SignUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var firebase = Firebase(this)
        firebase.initFirebase()
        firebase.initUserDatabase()

        binding.buttonSignup.setOnClickListener{
            val nombre = binding.editTextNombreCompletoSignup.text.toString()
            val email = binding.editTextEmailSignup.text.toString()
            val telefono = binding.editTextPhoneSignup.text.toString()
            val password = binding.editTextPasswordSignup.text.toString()
            val confirmar_password = binding.editTextConfirmPasswordSignup.text.toString()
            val pregunta = binding.editTextQuestionVerifier.text.toString()
            val respuesta = binding.editTextAnswerVerifier.text.toString()
            val newUser = NewUser(nombre, email, telefono, password, pregunta, respuesta)
            firebase.insertNewUser(newUser, confirmar_password)
        }
    }

}

