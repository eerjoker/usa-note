package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Direccion

class DireccionShipmentAdapter(
    var direcciones: MutableList<Direccion>
) : RecyclerView.Adapter<DireccionShipmentAdapter.DireccionShipmentHolder>() {

    class DireccionShipmentHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view : View = v

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
        holder.setAddress(direcciones[position].getFullAddress())
    }

    override fun getItemCount(): Int {
        return direcciones.size
    }
}