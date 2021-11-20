package com.example.proyectofinal.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.proyectofinal.R
import com.example.proyectofinal.Repassword.RepasswordActivity
import com.example.proyectofinal.SignIn.SigninActivity
import com.example.proyectofinal.SignUp.SignupActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Handler().postDelayed({
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) //demora de 3 segundos, para pasar al login.
    }
}