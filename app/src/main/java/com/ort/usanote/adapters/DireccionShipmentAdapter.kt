package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Direccion

class DireccionShipmentAdapter(
    var direcciones: MutableList<Direccion>,
    var selectedPosition: Int,
    var context: Context,
    var onItemClick : (Int) -> Unit
) : RecyclerView.Adapter<DireccionShipmentAdapter.DireccionShipmentHolder>() {

    class DireccionShipmentHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view : View = v

        fun getCardView(): CardView {
            val card : CardView = view.findViewById(R.id.card_shipment_direccion)
            return card
        }

        fun setAddress(direccion: String) {
            val txtDireccion : TextView = view.findViewById(R.id.txtDireccion)
            txtDireccion.text = direccion
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DireccionShipmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.direccion_shipment_item, parent, false)
        return (DireccionShipmentHolder(view))
    }

    override fun onBindViewHolder(holder: DireccionShipmentHolder, position: Int) {
        val cardView = holder.getCardView()

        holder.setAddress(direcciones[position].getFullAddress())
        cardView.setOnClickListener {
            onItemClick(position)
        }
        if (position == selectedPosition) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.verde_confirm))
        }
    }

    override fun getItemCount(): Int {
        return direcciones.size
    }
}