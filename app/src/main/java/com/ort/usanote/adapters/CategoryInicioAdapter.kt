package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.ort.usanote.R
import com.ort.usanote.entities.CategoriaInicio

class CategoryInicioAdapter(
    private var category: MutableList<CategoriaInicio>,
    private var context:Context,
    val onClick:(Int)->Unit
): RecyclerView.Adapter<CategoryInicioAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View, parentView: ViewGroup): RecyclerView.ViewHolder(itemView){
        var categoryTitle = itemView.findViewById<TextView>(R.id.txt_categoria)
        var card = itemView.findViewById<MaterialCardView>(R.id.card_view)
        fun setImage(context: Context, imageUrl: String) {
            var imgProductItem : ImageView = itemView.findViewById(R.id.imageView2)
            Glide
                .with(context)
                .load(imageUrl)
                .centerInside()
                .into(imgProductItem)
        }


    }
    override fun getItemCount(): Int {
        return category.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryInicioAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categoria_inicio_item,parent,false)
        return ViewHolder(view,parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryTitle.text = category[position].nombre
        holder.setImage(context,category[position].imageUrl)
        holder.card.setOnClickListener{
            onClick(position)
        }

    }


}