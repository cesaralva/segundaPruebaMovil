package com.example.pi_movil_grupo01.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pi_movil_grupo01.R
import com.example.pi_movil_grupo01.entity.Proyecto

class ProjectsAdapter(var projectsList: List<Proyecto>, private val onItemSelected:(Int) -> Unit):
    RecyclerView.Adapter<ProjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_projects, parent, false)
        return ProjectsViewHolder(view)
    }

    override fun getItemCount(): Int = projectsList.size

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.render(projectsList[position], onItemSelected)
    }
}