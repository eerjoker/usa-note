package com.ort.usanote.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.activities.MainActivity
import com.ort.usanote.adapters.OrdenAdapter
import com.ort.usanote.entities.Orden
import com.ort.usanote.viewModels.MisComprasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
        recMisComprasOrdenes = v.findViewById(R.id.cardOrdenItem)
        return v
    }

    override fun onStart() {
        super.onStart()
        recMisComprasOrdenes.setHasFixedSize(true)
        recMisComprasOrdenes.layoutManager = LinearLayoutManager(context)

        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)
        scope.launch {
            viewModel.getOrdenes("V6px2DSH8wOPaETZX8dtsSCQgOG3") { ordenes ->
                recMisComprasOrdenes.adapter = OrdenAdapter(ordenes) { ordenId ->
                    onOrdenClick(ordenId)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(MisComprasViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onOrdenClick(ordenId: String) {
        val action = MisComprasFragmentDirections.actionMisComprasFragmentToMisComprasDetalleOrdenFragment(ordenId)
        v.findNavController().navigate(action)
    }
}