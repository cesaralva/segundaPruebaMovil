package com.example.pi_movil_grupo01.entity

data class RegisterRequest(
    var nombre: String,
    var apellido: String,
    var email: String,
    var password: String,
    var username: String,
    var dni: String
)
