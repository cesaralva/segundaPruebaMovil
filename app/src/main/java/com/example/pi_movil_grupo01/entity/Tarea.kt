package com.example.pi_movil_grupo01.entity

import java.util.Date

data class Tarea(
    var idTarea: Int,
    var titulo: String,
    var descripcion: String,
    var prioridad: String,
    var fechaVencimiento: String,
    var proyecto : Proyecto,
    var usuario: Usuario,
    var cumplida: Boolean
)
