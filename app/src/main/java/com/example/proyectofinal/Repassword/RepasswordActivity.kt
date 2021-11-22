package com.example.proyectofinal.Repassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.databinding.ActivityRepasswordBinding

class RepasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepasswordBinding
    lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebase = Firebase(this)
        firebase.initFirebase()
        firebase.initUserDatabase()

        //verificar email
        binding.buttonSendEmailForgotPassword.setOnClickListener{
            email = binding.editTextEmailForgotPassword.text.toString()
            if(!email.isEmpty()){
                firebase.verifyEmail(email)
            }

        }

        //verificar pregunta
        binding.buttonSendVerification.setOnClickListener{
            val respuesta = binding.editTextRespuestaConfirmadora.text.toString()
            if(!respuesta.isEmpty()){
                firebase.verifyAnswer(email,respuesta)
            }
        }

        //cambiar password
        binding.buttonChangePassword.setOnClickListener {
            val newPassword = binding.editTextNewPassword.text.toString()
            if(newPassword.isEmpty() || newPassword.length < 6){
                Toast.makeText(this,"Ingrese datos correctos",Toast.LENGTH_SHORT).show()
            }
            else{
                firebase.changePassword(email, newPassword)
            }
        }
    }

    fun verificacionPaso1(){
        binding.linearForgotPasswordEmail.visibility = View.GONE
        binding.linearForgotPasswordPregunta.visibility = View.VISIBLE
    }

    fun verificacionPaso2(){
        binding.linearForgotPasswordPregunta.visibility = View.GONE
        binding.linearForgotChangePasword.visibility = View.VISIBLE
    }

    fun recuperarPregunta(pregunta: String){
        binding.txtQuestionVerifier.text = pregunta
    }

    fun regresarLogin(){
        this.finish()
    }
}