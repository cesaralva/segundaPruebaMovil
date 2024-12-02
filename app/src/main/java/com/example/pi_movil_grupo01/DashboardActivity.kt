package com.example.pi_movil_grupo01

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pi_movil_grupo01.entity.Proyecto
import com.example.pi_movil_grupo01.entity.ProyectoRequest
import com.example.pi_movil_grupo01.entity.ProyectoRequestUsuario
import com.example.pi_movil_grupo01.entity.Tarea
import com.example.pi_movil_grupo01.entity.TareaRequest
import com.example.pi_movil_grupo01.entity.TareaRequestProject
import com.example.pi_movil_grupo01.entity.TareaRequestUser
import com.example.pi_movil_grupo01.service.AuthService
import com.example.pi_movil_grupo01.util.ApiClient
import com.example.pi_movil_grupo01.util.PreferenceHelper
import com.example.pi_movil_grupo01.viewholders.ProjectsAdapter
import com.example.pi_movil_grupo01.viewholders.TasksAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DashboardActivity : ComponentActivity() {

    private var projectsList: MutableList<Proyecto> = mutableListOf()
    private var tasksList: MutableList<Tarea> = mutableListOf()

    private lateinit var preferences: SharedPreferences
    private lateinit var tvUsername: TextView
    private lateinit var tvProjectsSectionTitle: TextView
    private lateinit var tvTasksSectionTitle: TextView
    private lateinit var rvProjects: RecyclerView
    private lateinit var rvTasks: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton
    private lateinit var btnAddProject: Button

    private lateinit var projectsAdapter: ProjectsAdapter
    private lateinit var tasksAdapter: TasksAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val preferences: SharedPreferences = PreferenceHelper.defaultPrefs(this)
        if (!preferences.getBoolean("session", false)) {
            goToLogin()
            return
        }

        val userId = preferences.getInt("user_id", -1) // Obtén el userId almacenado
        if (userId != -1) {
            initComponents()
            initUI()
            initListeners()
            fetchProjects(userId)
        } else {
            Log.e("DashboardActivity", "User ID inválido")
        }
    }



// TODO MÉTODODS Adicionales




// TODO Primer Metodo --------------------------------------------------------------------------------
    private fun initComponents() {
        preferences = PreferenceHelper.defaultPrefs(this)
        rvProjects = findViewById(R.id.rvProjects)
        rvTasks = findViewById(R.id.rvTasks)
        fabAddTask = findViewById(R.id.fabAddTask)
        btnAddProject = findViewById(R.id.btnAddProject)
        tvUsername = findViewById(R.id.tvUsername)
        tvProjectsSectionTitle = findViewById(R.id.tvProjectsSectionTitle)
        tvTasksSectionTitle = findViewById(R.id.tvTasksSectionTitle)
    }











// TODO SEGUNDO Metodo --------------------------------------------------------------------------------

    private fun initUI() {
        val username = preferences.getString("username", "Usuario")

        tvUsername.text = username
        tvProjectsSectionTitle.text = "Proyectos de $username"

        projectsAdapter = ProjectsAdapter(projectsList){position -> updateProjects(position)}
        rvProjects.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvProjects.adapter = projectsAdapter

        tasksAdapter = TasksAdapter(tasksList){position -> onItemSelected(position)}
        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = tasksAdapter
    }







    //Metodo de apoyo


    private fun onItemSelected(position:Int){
        tasksList[position].cumplida = !tasksList[position].cumplida
        updateTasks()
    }


    private fun updateTasks(){
        val selectedProject = projectsList.firstOrNull{it.isSelected}

        if(selectedProject != null){
            fetchTasksForProject(selectedProject.idProyecto)
        } else {
            tasksList.clear()
            tasksAdapter.notifyDataSetChanged()
        }
    }


    private fun updateProjects(position: Int){

        projectsList.forEachIndexed { index, proyecto ->
            proyecto.isSelected = index == position
        }

        val selectedProject = projectsList.firstOrNull { it.isSelected }

        Log.i("Sergio", selectedProject.toString())

        if (selectedProject != null) {
            fetchTasksForProject(selectedProject.idProyecto)

            tvTasksSectionTitle.text = "Tareas en ${selectedProject.nombre}"
        }
        projectsAdapter.notifyDataSetChanged()
    }



    private fun fetchTasksForProject(projectId: Int) {
        val service = ApiClient.getClient(this).create(AuthService::class.java)
        val call = service.findTasksByProjectId(projectId)

        call.enqueue(object : Callback<List<Tarea>> {
            override fun onResponse(call: Call<List<Tarea>>, response: Response<List<Tarea>>) {
                if (response.isSuccessful) {
                    val taskResponseList = response.body()
                    if(response.code() == 204){
                        tasksList.clear()
                        tasksAdapter.notifyDataSetChanged()
                    }
                    if (taskResponseList != null) {
                        tasksList.clear()
                        tasksList.addAll(taskResponseList)
                        tasksAdapter.notifyDataSetChanged()
                        Log.i("Sergio", tasksList.toString())
                    }
                } else {
                    Log.e("Sergio", "Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Tarea>>, t: Throwable) {
                Log.e("Sergio", "Error: ${t.message}")
            }
        })
    }














    // TODO TERCER Metodo --------------------------------------------------------------------------------
    private fun initListeners() {
        fabAddTask.setOnClickListener {
            showNewTaskDialog() }
        btnAddProject.setOnClickListener{
            showNewProjectDialog() }
    }



    //Metodo de apoyo

    private fun showNewProjectDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_project)

        val btnAddProject: Button = dialog.findViewById(R.id.btnAddProject)
        val etProjectName: EditText = dialog.findViewById(R.id.etProjectName)

        val redSeekBar = dialog.findViewById<SeekBar>(R.id.seekbar_red)
        val greenSeekBar = dialog.findViewById<SeekBar>(R.id.seekbar_green)
        val blueSeekBar = dialog.findViewById<SeekBar>(R.id.seekbar_blue)
        val colorPreview = dialog.findViewById<View>(R.id.color_preview)
        val redValueText = dialog.findViewById<TextView>(R.id.value_red)
        val greenValueText = dialog.findViewById<TextView>(R.id.value_green)
        val blueValueText = dialog.findViewById<TextView>(R.id.value_blue)

        val initialRed = 0
        val initialGreen = 0
        val initialBlue = 0

        redSeekBar.progress = initialRed
        greenSeekBar.progress = initialGreen
        blueSeekBar.progress = initialBlue

        redValueText.text = initialRed.toString()
        greenValueText.text = initialGreen.toString()
        blueValueText.text = initialBlue.toString()

        val initialColor = Color.rgb(initialRed, initialGreen, initialBlue)
        colorPreview.setBackgroundColor(initialColor)

        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Obtener los valores actuales de los SeekBars
                val red = redSeekBar.progress
                val green = greenSeekBar.progress
                val blue = blueSeekBar.progress

                // Actualizar los TextView con los valores actuales
                redValueText.text = red.toString()
                greenValueText.text = green.toString()
                blueValueText.text = blue.toString()

                // Generar el color dinámicamente
                val color = Color.rgb(red, green, blue)

                // Actualizar la vista previa
                colorPreview.setBackgroundColor(color)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)


        btnAddProject.setOnClickListener{

            val red = redSeekBar.progress
            val green = greenSeekBar.progress
            val blue = blueSeekBar.progress

            val colorHex = String.format("#%02X%02X%02X", red, green, blue)


            val currentProjectName = etProjectName.text.toString()
            val currentProjectColor = colorHex

            val currentProjectUser = ProyectoRequestUsuario(preferences.getInt("user_id", -1))

            if(currentProjectName.isEmpty() || currentProjectColor.isEmpty()){
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val projectRequest = ProyectoRequest(null, currentProjectName, currentProjectColor, currentProjectUser)

            val projectRequestName = projectRequest.nombre

            val isNewProjectNameDuplicate: Boolean = projectsList.any{project -> project.nombre.equals(projectRequestName, ignoreCase = true)}

            if(isNewProjectNameDuplicate){
                Toast.makeText(this, "Nombre de proyecto duplicado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                registerProject(projectRequest)
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun registerProject(projectRequest: ProyectoRequest){
        val service = ApiClient.getClient(this).create(AuthService::class.java)
        val call = service.registerProject(projectRequest)

        call.enqueue(object : Callback<Proyecto> {
            override fun onResponse(call: Call<Proyecto>, response: Response<Proyecto>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DashboardActivity, "Proyecto registrado con éxito.", Toast.LENGTH_SHORT).show()

                    // Actualiza la lista de proyectos después de la creación

                    val userId = preferences.getInt("user_id", -1) // Obtén el userId almacenado
                    if (userId != -1) {
                        fetchProjects(userId) // Refresca los proyectos
                    }

                } else {
                    Toast.makeText(this@DashboardActivity, "Error al registrar el proyecto.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Proyecto>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }







    private fun showNewTaskDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_task)

        val btnAddTask: Button = dialog.findViewById(R.id.btnAddTask)
        val etTaskTitle: EditText = dialog.findViewById(R.id.etTaskTitle)
        val etTaskDescription: EditText = dialog.findViewById(R.id.etTaskDescription)

        val cvTaskDueDate: CalendarView = dialog.findViewById(R.id.cvTaskDueDate)
        val rgPriorities: RadioGroup = dialog.findViewById(R.id.rgPriorities)

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var currentTaskDueDate: String = formatter.format(Calendar.getInstance().time)

        cvTaskDueDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Convierte la fecha seleccionada a String en formato "yyyy-MM-dd"
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            currentTaskDueDate = formatter.format(selectedDate.time)
        }

        btnAddTask.setOnClickListener {
            val currentTaskTitle = etTaskTitle.text.toString()
            val currentTaskDescription = etTaskDescription.text.toString()

            if (currentTaskTitle.isEmpty() || currentTaskDescription.isEmpty() || currentTaskDueDate.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = rgPriorities.checkedRadioButtonId
            val selectedRadioButton: RadioButton = rgPriorities.findViewById(selectedId)
            val currentPriority: String = when (selectedRadioButton.text) {
                getString(R.string.task_high_priority) -> "Alta"
                getString(R.string.task_mid_priority) -> "Media"
                getString(R.string.task_low_priority) -> "Baja"
                else -> "Error"
            }

            // Obtén el proyecto seleccionado
            val currentProject = projectsList.firstOrNull { it.isSelected }
            val currentProjectId = currentProject?.idProyecto ?: 0  // Si no hay proyecto seleccionado, asume 0 (o puedes manejar otro caso)

            val currentUser = TareaRequestUser(preferences.getInt("user_id", -1))

            val taskRequest = TareaRequest(
                null,
                currentTaskTitle,
                currentTaskDescription,
                currentPriority,
                currentTaskDueDate,
                TareaRequestProject(currentProjectId),
                currentUser,
                false
            )

            val taskRequestName = taskRequest.titulo

            val isNewTaskNameDuplicate: Boolean = tasksList.any{task -> task.titulo.equals(taskRequestName, ignoreCase = true)}

            if(isNewTaskNameDuplicate){
                Toast.makeText(this, "Nombre de tarea duplicado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                registerTask(taskRequest)
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun registerTask(taskRequest: TareaRequest) {
        val service = ApiClient.getClient(this).create(AuthService::class.java)
        val call = service.registerTask(taskRequest)

        call.enqueue(object : Callback<Tarea> {
            override fun onResponse(call: Call<Tarea>, response: Response<Tarea>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DashboardActivity, "Tarea registrada con éxito.", Toast.LENGTH_SHORT).show()
                    val newTask = response.body()

                    newTask?.let {
                        tasksList.add(it)
                        tasksAdapter.notifyItemInserted(tasksList.size - 1)

                        val selectedProject = projectsList.firstOrNull { it.isSelected }
                        selectedProject?.let {
                            fetchTasksForProject(selectedProject.idProyecto)
                        }
                    }
                } else {
                    Toast.makeText(this@DashboardActivity, "Error al registrar la tarea.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tarea>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




















// TODO CUARTO Metodo --------------------------------------------------------------------------------
    private fun fetchProjects(userId: Int) {
        val service = ApiClient.getClient(this).create(AuthService::class.java)
        val call = service.findProjectsByUserId(userId)

        call.enqueue(object : Callback<List<Proyecto>> {
            override fun onResponse(call: Call<List<Proyecto>>, response: Response<List<Proyecto>>) {
                if (response.isSuccessful) {
                    val projectResponseList = response.body()
                    if (projectResponseList != null) {
                        projectsList.clear()
                        projectsList.addAll(projectResponseList)

                        // Seleccionar el primer proyecto por defecto
                        if (projectsList.isNotEmpty()) {
                            projectsList[0].isSelected = true

                            val projectName = projectsList[0].nombre
                            tvTasksSectionTitle.text = "Tareas en $projectName"

                            fetchTasksForProject(projectsList[0].idProyecto)
                        }
                        projectsAdapter.notifyDataSetChanged()

                        Log.i("Sergio", projectsList[0].toString())
                    }
                } else {
                    Log.e("DashboardActivity", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Proyecto>>, t: Throwable) {
                Log.e("DashboardActivity", "Error: ${t.message}")
            }
        })
    }











    //TODO ----------------------------------------------------------------------------------------------




    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }





























}
