package com.example.pi_movil_grupo01.entity

data class RegisterResponse(
    val token: String,
    val dniUnique: Boolean,
    val emailUnique: Boolean,
    val usernameUnique: Boolean,
    val suggestion: String
)
