package com.example.pi_movil_grupo01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.graphics.Insets
import com.example.pi_movil_grupo01.entity.AuthResponse
import com.example.pi_movil_grupo01.entity.RegisterRequest
import com.example.pi_movil_grupo01.entity.RegisterResponse
import com.example.pi_movil_grupo01.service.AuthService
import com.example.pi_movil_grupo01.util.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etUsername: EditText
    private lateinit var etDni: EditText
    private lateinit var tvEmailValidator: AppCompatTextView
    private lateinit var tvDniValidator: AppCompatTextView
    private lateinit var tvUsernameValidator: AppCompatTextView
    private lateinit var btnRegister: Button
    private lateinit var tcGoLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etUsername = findViewById(R.id.etUsername)
        etDni = findViewById(R.id.etDni)
        tvEmailValidator = findViewById(R.id.tvEmailValidator)
        tvDniValidator = findViewById(R.id.tvDniValidator)
        tvUsernameValidator = findViewById(R.id.tvUsernameValidator)
        btnRegister = findViewById(R.id.btnRegister)
        tcGoLogin = findViewById(R.id.tv_go_to_login)

        etPassword.addTextChangedListener(passwordWatcher)
        etConfirmPassword.addTextChangedListener(confirmPasswordWatcher)
        etEmail.addTextChangedListener(emailWatcher)

        btnRegister.setOnClickListener {
            register()
        }

        tcGoLogin.setOnClickListener {
            goToLogin()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validatePassword()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private val confirmPasswordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
                etConfirmPassword.error = "Las contraseñas no coinciden"
            } else {
                etConfirmPassword.error = null
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private val emailWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                etEmail.error = "Correo electrónico no válido"
            } else {
                etEmail.error = null
            }
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun validatePassword() {
        val password = etPassword.text.toString()

        // Validaciones específicas
        val hasNumber = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasUppercase = password.any { it.isUpperCase() }

        when {
            password.length < 8 -> {
                etPassword.error = "La contraseña debe tener al menos 8 caracteres"
            }
            !hasNumber -> {
                etPassword.error = "La contraseña debe tener al menos un número"
            }
            !hasSpecialChar -> {
                etPassword.error = "La contraseña debe tener al menos un carácter especial"
            }
            !hasLowercase -> {
                etPassword.error = "La contraseña debe tener al menos una letra minúscula"
            }
            !hasUppercase -> {
                etPassword.error = "La contraseña debe tener al menos una letra mayúscula"
            }
            else -> {
                etPassword.error = null
            }
        }
    }

    private fun register() {
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()
        val username = etUsername.text.toString()
        val dni = etDni.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
            || username.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(nombre, apellido, email, password, username, dni)

        val service = ApiClient.getClient(this).create(AuthService::class.java)
        service.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val registerResponse = response.body()
                if (response.isSuccessful && registerResponse != null) {
                    if (registerResponse.emailUnique && registerResponse.usernameUnique && registerResponse.dniUnique){
                        tvDniValidator.text = ""
                        tvUsernameValidator.text = ""
                        tvEmailValidator.text = ""
                        Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        goToLogin()
                    } else {
                        if(!registerResponse.usernameUnique){
                            tvUsernameValidator.text = "Usuario existente. Sugerencia: ${registerResponse.suggestion}"
                        } else{
                            tvUsernameValidator.text = ""
                        }
                        if(!registerResponse.dniUnique){
                            tvDniValidator.text = "El DNI ya se encuentra registrado"
                        } else{
                            tvDniValidator.text = ""
                        }
                        if(!registerResponse.emailUnique){
                            tvEmailValidator.text = "El Email ya se encuentra registrado"
                        } else{
                            tvEmailValidator.text = ""
                        }
                        Toast.makeText(this@RegisterActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Error en servidor", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
