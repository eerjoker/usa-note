package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Orden

class OrdenAdapter(
    private val ordenesList: MutableList<Orden>,
    private val onOrdenClick : (String) -> Unit
) : RecyclerView.Adapter<OrdenAdapter.OrdenHolder>() {

    class OrdenHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun getCardView() : CardView {
            return view.findViewById(R.id.cardOrdenItem)
        }

        fun setNumeroOrden(numeroOrden: Int) {
            val txtNumeroOrden : TextView = view.findViewById(R.id.txtNumeroOrden)
            txtNumeroOrden.text = getNumeroOrdenString(numeroOrden)
        }

        fun setCantProductos(cantProductos: Int) {
            val txtCantProductos : TextView = view.findViewById(R.id.txtCantProductosValue)
            txtCantProductos.text = cantProductos.toString()
        }

        fun setTotal(total: Double) {
            val txtTotal : TextView = view.findViewById(R.id.txtTotalValue)
            txtTotal.text = "$" + total.toString()
        }

        fun setEntregadoA(entregadoA: String?) {
            val txtEntregadoA : TextView = view.findViewById(R.id.txtEntregadoAValue)
            txtEntregadoA.text = entregadoA
        }

        fun setDireccion(direccion: String) {
            val txtDireccion : TextView = view.findViewById(R.id.txtDireccionValue)
            txtDireccion.text = direccion
        }

        private fun getNumeroOrdenString(numeroOrden: Int) : String {
            var str = "Orden #"
            if (numeroOrden / 10 < 1) {
                str += "00"
            } else if (numeroOrden / 100 < 1) {
                str += "0"
            }
            return str + numeroOrden.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mis_compras_orden_item, parent, false)
        return (OrdenHolder(view))
    }

    override fun onBindViewHolder(holder: OrdenHolder, position: Int) {
        val ordenActual : Orden = ordenesList[position]
        holder.setNumeroOrden(ordenActual.numeroOrden)
        holder.setCantProductos(ordenActual.cantProductos)
        holder.setTotal(ordenActual.total)
        holder.setEntregadoA(ordenActual.entregadoA)
        holder.setDireccion(ordenActual.direccionEntrega)
        holder.getCardView().setOnClickListener {
            onOrdenClick(ordenActual.idOrden)
        }
    }

    override fun getItemCount(): Int {
        return ordenesList.size
    }
}