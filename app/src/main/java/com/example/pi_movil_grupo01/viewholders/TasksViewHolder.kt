package com.example.pi_movil_grupo01.viewholders

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pi_movil_grupo01.R
import com.example.pi_movil_grupo01.entity.Tarea

class TasksViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvTask: TextView = view.findViewById(R.id.tvTask)
    private val cbTask: CheckBox = view.findViewById(R.id.cbTask)
    private val viewContainer: CardView = view.findViewById(R.id.viewContainer)

    fun render(task: Tarea){
        cbTask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvTask.paintFlags = tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTask.paintFlags = tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        tvTask.text = task.titulo
        /*
        cbTask.isChecked = task.isSelected

         */

        val backgroundColor = when(task.prioridad){
            "Alta" -> R.color.p1_background
            "Media" -> R.color.p2_background
            "Baja" -> R.color.p3_background
            else -> R.color.white
        }
        viewContainer.setCardBackgroundColor(ContextCompat.getColor(viewContainer.context, backgroundColor))

        var textColor = when(task.prioridad){
            "Alta" -> R.color.white
            "Media" -> R.color.white
            "Baja" -> R.color.black
            else -> R.color.black
        }

        tvTask.setTextColor(ContextCompat.getColor(tvTask.context, textColor))


    }
}