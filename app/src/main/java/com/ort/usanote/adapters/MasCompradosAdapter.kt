package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.usanote.R
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.DetalleOrden

class MasCompradosAdapter(
    val productList: MutableList<DetalleOrden>,
    val context: Context,
    val compras: Int
): RecyclerView.Adapter<MasCompradosAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View, parentView: ViewGroup): RecyclerView.ViewHolder(itemView){

        private var view : View
        private var parent : ViewGroup
        init {
            this.view = itemView
            this.parent = parentView
        }
        var itemTItle: TextView = itemView.findViewById(R.id.item_title)
        var itemDetail: TextView = itemView.findViewById(R.id.item_desc)
        var itemPrice: TextView = itemView.findViewById(R.id.item_price)
        var item_stock: TextView = itemView.findViewById(R.id.item_stock)
        fun setImage(context: Context, imageUrl: String) {
            var imgProductItem : ImageView = itemView.findViewById(R.id.item_image)
            Glide
                .with(context)
                .load(imageUrl)
                .centerInside()
                .into(imgProductItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_in_list_products,parent,false)
        return ViewHolder(view,parent)
    }
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.item_stock.text = ""
        holder.itemTItle.text = productList[i].product.nombre
        holder.itemDetail.text = productList[i].product.description
        holder.itemPrice.text = "Comprados: " + productList[i].quantity
        holder.setImage(context,productList[i].product.imageUrl)
    }
    override fun getItemCount(): Int {
        return productList.size
    }

}