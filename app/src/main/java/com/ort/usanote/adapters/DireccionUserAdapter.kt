package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ort.usanote.R
import com.ort.usanote.entities.Direccion
import com.ort.usanote.fragments.UserFragment
import com.ort.usanote.viewModels.UpdateDireccionViewModel


class DireccionUserAdapter(
    var direccionList: ArrayList<Direccion>,
    var context: Context, var onClick: (Int) -> Unit,
    var updateDireccionViewModel: UpdateDireccionViewModel
) :
    RecyclerView.Adapter<DireccionUserAdapter.DireccionHolder>() {

    class DireccionHolder (v: View, parentView: ViewGroup) : RecyclerView.ViewHolder(v) {
        private var view: View
        private var parent : ViewGroup

        init {
            this.view = v
            this.parent = parentView
        }

        fun setDireccion(calle: String, altura: String, piso: String, depto: String) {
            val textoDireccion: String = "Calle: ${calle}, altura: ${altura}, piso: ${piso}, depto: ${depto}"
            val direccionUser: TextView = view.findViewById(R.id.direccionUser)

            direccionUser.text = textoDireccion
        }

        fun getUpdateBtn(): FloatingActionButton {
            return view.findViewById(R.id.btnUpdate)
        }

        fun getDeleteBtn(): FloatingActionButton {
            return view.findViewById(R.id.btnDelete)
        }

        fun getCardView(): CardView {
            return view.findViewById(R.id.cardDireccionUser)
        }

        fun deleteDireccion() {
            parent.removeView(getCardView())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DireccionHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.direccion_user_item,parent,false)
        return  (DireccionHolder(view, parent))
    }

    override fun onBindViewHolder(holder: DireccionHolder, position: Int) {

        var direccionAux: Direccion = direccionList.get(position)

        holder.setDireccion(direccionAux.calle, direccionAux.numero, direccionAux.piso, direccionAux.departamento)

        holder.getUpdateBtn().setOnClickListener(){
            onClick(position)
        }

        holder.getDeleteBtn().setOnClickListener(){
            updateDireccionViewModel.actualizarDireccion(direccionAux.alias, direccionAux.nombreCompleto, direccionAux.calle, direccionAux.localidad,
                direccionAux.numero, direccionAux.piso, direccionAux.departamento, direccionAux.provincia, direccionAux.codigoPostal)
        }

    }

    override fun getItemCount(): Int {
        return direccionList.size
    }
}