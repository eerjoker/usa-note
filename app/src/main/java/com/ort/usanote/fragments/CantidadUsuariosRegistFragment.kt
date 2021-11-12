package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.R
import com.ort.usanote.entities.Product
import com.ort.usanote.viewModels.CantidadUsuariosRegistViewModel

class CantidadUsuariosRegistFragment : Fragment() {

    companion object {
        fun newInstance() = CantidadUsuariosRegistFragment()
    }

    private lateinit var viewModel: CantidadUsuariosRegistViewModel
    private lateinit var v:View
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.cantidad_usuarios_regist_fragment, container, false)
        db = FirebaseFirestore.getInstance()
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CantidadUsuariosRegistViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()
        var cant_personas = v.findViewById<TextView>(R.id.cant_users)
        db.collection("usuarios").get()
            .addOnSuccessListener {
                cant_personas.text = it.size().toString()
            }
            .addOnFailureListener {
                Log.d("UsuariosTotales", "Error en traer la cantidad")
            }

            }
        }
