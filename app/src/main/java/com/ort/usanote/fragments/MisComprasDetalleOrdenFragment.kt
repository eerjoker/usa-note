package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.DetalleOrdenAdapter
import com.ort.usanote.adapters.ProductItemsAdapter
import com.ort.usanote.viewModels.MisComprasDetalleOrdenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MisComprasDetalleOrdenFragment : Fragment() {

    companion object {
        fun newInstance() = MisComprasDetalleOrdenFragment()
    }

    private val viewModel: MisComprasDetalleOrdenViewModel by viewModels()
    private lateinit var v : View
    private lateinit var recMisComprasDetalleOrden: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.mis_compras_detalle_orden_fragment, container, false)
        recMisComprasDetalleOrden = v.findViewById(R.id.recMisComprasDetalleOrden)
        return v
    }

    override fun onStart() {
        super.onStart()

        var idOrden = MisComprasDetalleOrdenFragmentArgs.fromBundle(requireArguments()).idOrden

        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)
        scope.launch {
            var detalleOrdenList = viewModel.getDetallesOrden(idOrden) { detallesOrden ->
                recMisComprasDetalleOrden.setHasFixedSize(true)
                recMisComprasDetalleOrden.layoutManager = LinearLayoutManager(context)
                recMisComprasDetalleOrden.adapter = DetalleOrdenAdapter(detallesOrden)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MisComprasDetalleOrdenViewModel::class.java)
        // TODO: Use the ViewModel
    }
}