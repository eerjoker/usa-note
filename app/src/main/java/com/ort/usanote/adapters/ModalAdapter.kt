package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R

class ModalAdapter(
    private var stock: MutableList<Int>,
    val onClick:(Int)->Unit
):RecyclerView.Adapter<ModalAdapter.viewHolder>()  {
    inner class viewHolder(itemView: View, parentView:ViewGroup):RecyclerView.ViewHolder(itemView) {
        var stock = itemView.findViewById<TextView>(R.id.item_stock)
        var card = itemView.findViewById<CardView>(R.id.card_view)
        var sonMasDe6 = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModalAdapter.viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stock_dialog_item,parent,false)
        return viewHolder(view,parent)
    }

    override fun onBindViewHolder(holder: ModalAdapter.viewHolder, position: Int) {
        var text = " unidades"
        if(position == 0){
            text = " unidad"
        }
        if(position < 6){
            holder.stock.text  = (position+1).toString() + text
            holder.card.setOnClickListener{
                onClick(position+1)

            }
        }else{
            if(position == 6) {
                holder.stock.text = "Más de 6 unidades"
                holder.card.setOnClickListener{
                    onClick(position+1)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return stock.size
    }

}