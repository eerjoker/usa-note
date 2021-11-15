package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ort.usanote.R

class CategoryAdapter (
    private var category: Array<String>,
    val onClick:(Int)->Unit
        ):RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View, parentView:ViewGroup):RecyclerView.ViewHolder(itemView){
         var category = itemView.findViewById<MaterialButton>(R.id.boton_category)


    }
    override fun getItemCount(): Int {
        return category.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.button_categoria_item,parent,false)
        return ViewHolder(view,parent)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, pos: Int) {
        holder.category.text = category[pos]
        holder.category.setOnClickListener{
            onClick(pos)
        }
    }



}