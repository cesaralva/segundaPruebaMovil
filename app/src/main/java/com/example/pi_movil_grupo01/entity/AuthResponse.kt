package com.example.pi_movil_grupo01.entity

data class AuthResponse(
    var token: String,
    var id: Int,
    var username: String,
    var email: String
)
