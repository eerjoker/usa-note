package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.ProductForListProducts

class ProductAdapter(
    private val productList:ArrayList<ProductForListProducts>,
    val onClick:(Int)->Unit):RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
            var card : CardView = itemView.findViewById(R.id.card_view)
            var itemImage: ImageView = itemView.findViewById(R.id.item_image)
            var itemTItle: TextView = itemView.findViewById(R.id.item_title)
            var itemDetail: TextView = itemView.findViewById(R.id.item_desc)
            var itemPrice: TextView = itemView.findViewById(R.id.item_price)
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card_in_list_products,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.itemTItle.text = productList[i].title
        holder.itemDetail.text = productList[i].description
        holder.itemPrice.text = productList[i].price
        holder.itemImage.setImageResource(productList[i].imagen)
        holder.card.setOnClickListener() {
            onClick(i)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

}