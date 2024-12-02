package com.example.pi_movil_grupo01.entity

data class ProyectoRequest(
    var idProyecto: Int?,
    var nombre: String,
    var color: String,
    var usuario: ProyectoRequestUsuario
)

data class ProyectoRequestUsuario(
    var id: Int
)
