package com.example.pi_movil_grupo01.viewholders

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pi_movil_grupo01.R
import com.example.pi_movil_grupo01.entity.Proyecto

class ProjectsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val tvProjectName: TextView = view.findViewById(R.id.tvProjectName)
    private val circleColor: ImageView = view.findViewById(R.id.circleColor)
    private val divider: View = view.findViewById(R.id.divider)
    private val viewContainer: CardView = view.findViewById(R.id.viewContainer)

    fun render(proyecto: Proyecto, onItemSelected: (Int) -> Unit){

        val color = if (proyecto.isSelected) {
            R.color.button_light
        } else {
            R.color.button_dark
        }

        viewContainer.setCardBackgroundColor(ContextCompat.getColor(viewContainer.context, color))

        itemView.setOnClickListener{onItemSelected(layoutPosition)}

        tvProjectName.text = proyecto.nombre.toString()

        val circleColorHex = proyecto.color
        val colorInt = Color.parseColor(circleColorHex)
        val drawable: Drawable = ContextCompat.getDrawable(itemView.context, R.drawable.ic_circle)!!
        drawable.setTint(colorInt)
        circleColor.setImageDrawable(drawable)
    }

}