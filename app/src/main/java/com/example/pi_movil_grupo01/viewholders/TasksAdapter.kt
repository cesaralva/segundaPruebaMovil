package com.example.pi_movil_grupo01.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pi_movil_grupo01.R
import com.example.pi_movil_grupo01.entity.Proyecto
import com.example.pi_movil_grupo01.entity.Tarea

class TasksAdapter(var tasksList: List<Tarea>, private val onTaskSelected: (Int) -> Unit):
    RecyclerView.Adapter<TasksViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent,false)
        return TasksViewHolder(view)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.render(tasksList[position])
        holder.itemView.setOnClickListener{ (onTaskSelected(position)) }
    }
}