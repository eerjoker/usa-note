package com.ort.usanote.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Estadistica


class EstadisticasAdapter(private var estadisticaList : MutableList<Estadistica>,
                          val onClick:(Int)->Unit): RecyclerView.Adapter<EstadisticasAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View, parentView:ViewGroup):RecyclerView.ViewHolder(itemView) {
        var itemTItle: TextView = itemView.findViewById(R.id.item_title)
        var card : CardView = itemView.findViewById(R.id.card_view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstadisticasAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.estdisticas_item,parent,false)
        return ViewHolder(view,parent)
    }

    override fun getItemCount(): Int {

        return estadisticaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTItle.text = estadisticaList[position].nombre
        holder.card.setCardBackgroundColor(Color.parseColor(estadisticaList[position].color))
        holder.card.setOnClickListener() {
            onClick(position)
        }

    }

}