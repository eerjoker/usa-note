package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ort.usanote.R
import com.ort.usanote.adapters.OrdenesAdapter
import com.ort.usanote.adapters.ProductItemsAdapter
import com.ort.usanote.entities.Orden
import com.ort.usanote.viewModels.MisComprasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MisComprasFragment : Fragment() {

    companion object {
        fun newInstance() = MisComprasFragment()
    }

    private val viewModel: MisComprasViewModel by viewModels()
    private lateinit var v : View
    private lateinit var recMisComprasOrdenes : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.mis_compras_fragment, container, false)
        recMisComprasOrdenes = v.findViewById(R.id.recMisComprasOrdenes)
        return v
    }

    override fun onStart() {
        super.onStart()
        recMisComprasOrdenes.setHasFixedSize(true)
        recMisComprasOrdenes.layoutManager = LinearLayoutManager(context)

        var ordenes = async { viewModel.getOrdenes("V6px2DSH8wOPaETZX8dtsSCQgOG3") }
        recMisComprasOrdenes.adapter = OrdenesAdapter(ordenes)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MisComprasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}