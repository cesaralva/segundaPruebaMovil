package com.example.pi_movil_grupo01.service

import com.example.pi_movil_grupo01.entity.AuthRequest
import com.example.pi_movil_grupo01.entity.AuthResponse
import com.example.pi_movil_grupo01.entity.Proyecto
import com.example.pi_movil_grupo01.entity.ProyectoRequest
import com.example.pi_movil_grupo01.entity.RegisterRequest
import com.example.pi_movil_grupo01.entity.RegisterResponse
import com.example.pi_movil_grupo01.entity.Tarea
import com.example.pi_movil_grupo01.entity.TareaRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {

    @POST("auth/login")
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("api/proyectos/usuario/{userId}")
    fun findProjectsByUserId(@Path("userId") userId: Int): Call<List<Proyecto>>

    @GET("api/tareas/proyecto/{projectId}")
    fun findTasksByProjectId(@Path("projectId") projectId: Int): Call<List<Tarea>>

    @POST("api/tareas")
    fun registerTask(@Body task: TareaRequest): Call<Tarea>

    @POST("api/proyectos")
    fun registerProject(@Body project: ProyectoRequest): Call<Proyecto>
}