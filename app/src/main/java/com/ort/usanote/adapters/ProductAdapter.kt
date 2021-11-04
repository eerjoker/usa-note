package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.usanote.R
import com.ort.usanote.entities.Product

class ProductAdapter(
    private var productList : MutableList<Product>,
    private val context: Context,
    val onClick:(Int)->Unit):RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


        inner class ViewHolder(itemView: View, parentView:ViewGroup):RecyclerView.ViewHolder(itemView){

            private var view : View
            private var parent : ViewGroup

            init {
                this.view = itemView
                this.parent = parentView
            }
            var card : CardView = itemView.findViewById(R.id.card_view)
            var itemTItle: TextView = itemView.findViewById(R.id.item_title)
            var itemDetail: TextView = itemView.findViewById(R.id.item_desc)
            var itemPrice: TextView = itemView.findViewById(R.id.item_price)
            var item_stock: TextView = itemView.findViewById(R.id.item_stock)
            fun setImage(context: Context, imageUrl: String) {
                var imgProductItem : ImageView = view.findViewById(R.id.item_image)
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
        val stockText : String
        val color : Int
        if(productList[i].stock <= 0){
            stockText = "No hay stock"
            color = ContextCompat.getColor(context,R.color.rojo_stock)
        }else if(productList[i].stock > 0 && productList[i].stock < 4){
            stockText = "Poco stock"
            color = ContextCompat.getColor(context,R.color.naranja_stock)
        }else{
            stockText = "Hay stock"
            color = ContextCompat.getColor(context,R.color.verde_stock)
        }

        holder.item_stock.text = stockText
        holder.item_stock.setTextColor(color)
        holder.itemTItle.text = productList[i].nombre
        holder.itemDetail.text = productList[i].description
        holder.itemPrice.text = productList[i].price.toString()

        holder.setImage(context,productList[i].imageUrl)
        holder.card.setOnClickListener() {
            onClick(i)
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

}