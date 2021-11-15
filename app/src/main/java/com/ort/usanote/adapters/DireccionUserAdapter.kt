package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.entities.Direccion


class DireccionUserAdapter(
    var direccionList: ArrayList<Direccion>,
    var context: Context, var onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<DireccionUserAdapter.DireccionHolder>() {

    class DireccionHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setDireccion(calle: String, altura: String, piso: String, depto: String) {
            val textoDireccion: String = "Calle: ${calle}, altura: ${altura}, piso: ${piso}, depto: ${depto}"
            val direccionUser: TextView = view.findViewById(R.id.direccionUser)

            direccionUser.text = textoDireccion
        }

        fun getCardView(): CardView {
            return view.findViewById(R.id.cardDireccionUser)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DireccionHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.direccion_user_item,parent,false)
        return (DireccionHolder(view))
    }

    override fun onBindViewHolder(holder: DireccionHolder, position: Int) {

        var direccionAux: Direccion = direccionList.get(position)

        holder.setDireccion(direccionAux.calle, direccionAux.numero, direccionAux.piso, direccionAux.departamento)

        holder.getCardView().setOnClickListener(){
            onClick(position)
        }

    }

    override fun getItemCount(): Int {
        return direccionList.size
    }
}