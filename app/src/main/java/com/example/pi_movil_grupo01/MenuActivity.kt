package com.example.pi_movil_grupo01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.content.SharedPreferences
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.Insets
import com.example.pi_movil_grupo01.util.PreferenceHelper

class MenuActivity : AppCompatActivity() {

    private lateinit var btnDashboard: Button
    private lateinit var tvBienvenida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        if (!preferences.getBoolean("session", false)) {
            goToLogin()
            return
        }

        // Recuperar el nombre de usuario y personalizar el botón
        val username = preferences.getString("username", "Usuario")

        tvBienvenida = findViewById(R.id.tvBienvenida)
        btnDashboard = findViewById(R.id.btnDashboard)
        tvBienvenida.text = "Bienvenid@, $username"

        val btnLogout: Button = findViewById(R.id.btn_logout)

        btnLogout.setOnClickListener {
            logout()
        }

        btnDashboard.setOnClickListener{
            navigateToDashboard()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigateToDashboard(){
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }


    private fun logout() {
        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        preferences.edit()
            .remove("auth_token")
            .remove("session")
            .apply()

        goToLogin()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}