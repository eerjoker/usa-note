package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Orden

class OrdenesAdapter(
    private var ordenesList: MutableList<Orden>
) : RecyclerView.Adapter<OrdenesAdapter.OrdenesHolder>() {

    class OrdenesHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setNumeroOrden(numeroOrden: Int) {
            val txtNumeroOrden : TextView = view.findViewById(R.id.txtNumeroOrden)
            txtNumeroOrden.text = numeroOrden.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return (OrdenesHolder(view))
    }

    override fun onBindViewHolder(holder: OrdenesHolder, position: Int) {
        holder.setNumeroOrden(ordenesList[position].numeroOrden)
    }

    override fun getItemCount(): Int {
        return ordenesList.size
    }
}