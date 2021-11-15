package com.ort.usanote.fragments.estadistica

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ort.usanote.R
import com.ort.usanote.adapters.EstadisticasAdapter
import com.ort.usanote.entities.Estadistica
import com.ort.usanote.viewModels.estadistica.EstadisticasViewModel

class EstadisticasFragment : Fragment() {

    companion object {
        fun newInstance() = EstadisticasFragment()
    }
    private lateinit var rootView : View
    private lateinit var viewModel: EstadisticasViewModel
    private lateinit var recyclerView: RecyclerView
    private  var estadisticasList:MutableList<Estadistica> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.estadisticas_fragment, container, false)
        initEstadisticas()
        recyclerview(rootView)
        return rootView
    }
    fun initEstadisticas(){
        estadisticasList.clear()
        estadisticasList.add(Estadistica("Productos mas vistos", "#F58A00"))
        estadisticasList.add(Estadistica("Usuarios registrados", "#F58A00"))
        estadisticasList.add(Estadistica("Productos mas comprados", "#F58A00"))
    }
    private fun recyclerview(rootView:View){
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.adapter = EstadisticasAdapter(estadisticasList){
            if(estadisticasList[it].nombre == "Productos mas vistos"){
                val action =
                    EstadisticasFragmentDirections.actionEstadisticasFragmentToProductosMasVistosFragment()
                rootView.findNavController().navigate(action)
            }else if(estadisticasList[it].nombre == "Usuarios registrados"){
                val action =
                    EstadisticasFragmentDirections.actionEstadisticasFragmentToCantidadUsuariosRegistFragment()
                rootView.findNavController().navigate(action)
            }else if(estadisticasList[it].nombre == "Productos mas comprados"){
                val action =
                    EstadisticasFragmentDirections.actionEstadisticasFragmentToProductosMasCompradosFragment()
                rootView.findNavController().navigate(action)
            }


        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EstadisticasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}