package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.DetalleOrden

class DetalleOrdenAdapter (
    private val detalleOrdenList: MutableList<DetalleOrden>,
) : RecyclerView.Adapter<DetalleOrdenAdapter.DetalleOrdenHolder>() {

    class DetalleOrdenHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setTitle(title: String) {
            val txtTitle : TextView = view.findViewById(R.id.txtTituloValue)
            txtTitle.text = title
        }

        fun setPrecio(precio: Double) {
            val txtPrecio : TextView = view.findViewById(R.id.txtPrecioValue)
            txtPrecio.text = "$" + precio.toString()
        }

        fun setCantidad(cantidad: Int) {
            val txtCantidad : TextView = view.findViewById(R.id.txtCantidadValue)
            txtCantidad.text = cantidad.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetalleOrdenHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mis_compras_detalle_orden_item, parent, false)
        return (DetalleOrdenHolder(view))
    }

    override fun onBindViewHolder(holder: DetalleOrdenHolder, position: Int) {
        val detalleOrdenActual = detalleOrdenList[position]
        holder.setTitle(detalleOrdenActual.product.nombre)
        holder.setPrecio(detalleOrdenActual.calculateSubtotal())
        holder.setCantidad(detalleOrdenActual.quantity)
    }

    override fun getItemCount(): Int {
        return detalleOrdenList.size
    }

}