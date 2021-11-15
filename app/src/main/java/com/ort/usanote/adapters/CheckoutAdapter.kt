package com.ort.usanote.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.DetalleOrden
import com.ort.usanote.entities.ProductItemRepository

class CheckoutAdapter (
    private var productItems : ProductItemRepository,
    private var context : Context
) : RecyclerView.Adapter<CheckoutAdapter.CheckoutHolder>() {

    class CheckoutHolder(v: View, parentView: ViewGroup) : RecyclerView.ViewHolder(v) {
        private var view : View
        private var parent : ViewGroup

        init {
            this.view = v
            this.parent = parentView
        }

        fun setTitle(title: String) {
            val txtTitle : TextView = view.findViewById(R.id.textViewProductTitle)
            txtTitle.text = title
        }

        @SuppressLint("SetTextI18n")
        fun setPrice(price: Double) {
            val txtPrice : TextView = view.findViewById(R.id.textViewProductPrice)
            txtPrice.text = "$$price"
        }

        fun getCardView() : CardView {
            return view.findViewById(R.id.recProductsCheckout)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_item_checkout, parent, false)
        return (CheckoutHolder(view, parent))
    }

    override fun onBindViewHolder(holder: CheckoutHolder, position: Int) {
        val detalleOrdenList : MutableList<DetalleOrden> = productItems.getProductItems()

        holder.setTitle(detalleOrdenList[position].producto.nombre)
        holder.setPrice(detalleOrdenList[position].producto.price)
    }

    override fun getItemCount(): Int {
        return productItems.getProductItems().size
    }
}