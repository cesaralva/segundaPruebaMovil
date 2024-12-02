package com.example.pi_movil_grupo01.entity


data class TareaRequest(
    var idTarea: Int?,
    var titulo: String,
    var descripcion: String,
    var prioridad: String,
    var fechaVencimiento: String,
    var proyecto : TareaRequestProject,
    var usuario: TareaRequestUser,
    var cumplida: Boolean
)

data class TareaRequestProject(
    var idProyecto: Int?
)

data class TareaRequestUser(
    var id: Int
)
