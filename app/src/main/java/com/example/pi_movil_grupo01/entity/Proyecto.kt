package com.example.pi_movil_grupo01.entity

import java.util.Date

data class Proyecto(
    var idProyecto: Int,
    var nombre: String,
    var color: String,
    var usuario: Usuario,
    var isSelected: Boolean = false
)
