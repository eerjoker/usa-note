package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ort.usanote.R
import com.ort.usanote.adapters.DireccionUserAdapter
import com.ort.usanote.entities.Direccion
import com.ort.usanote.viewModels.UserViewModel

class UserFragment : Fragment() {

    lateinit var v: View
    lateinit var userTitle: TextView
    lateinit var nombreCompleto: TextView
    lateinit var emailUser: TextView
    lateinit var telefonoUser: TextView
    lateinit var btnDireccion: Button
    lateinit var btnSecundarioDireccion: FloatingActionButton
    lateinit var recDireccion: RecyclerView
    lateinit var direccionUserAdapter: DireccionUserAdapter
    private val viewModelUser: UserViewModel by viewModels()

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.user_fragment, container, false)

        userTitle = v.findViewById(R.id.userTitle)
        nombreCompleto = v.findViewById(R.id.nombreUser)
        emailUser = v.findViewById(R.id.emailUser)
        telefonoUser = v.findViewById(R.id.telefonoUser)
        btnDireccion = v.findViewById(R.id.agregaDireccionbtn)
        btnSecundarioDireccion = v.findViewById(R.id.agredarDireccionMas)
        recDireccion = v.findViewById(R.id.recyclerView2)



        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        viewModelUser.setUser()
        viewModelUser.getDirecciones()

        viewModel.userDb.observe(viewLifecycleOwner, Observer {
            if (it != null){
                nombreCompleto.text = "${it.nombre} ${it.apellido}"
                emailUser.text = it.email
                telefonoUser.text = it.telefono
            }
        })

        viewModel.direccionesUser.observe(viewLifecycleOwner, Observer{

            if (it != null) {
                if(it.size > 0){
                    recDireccion.layoutManager = LinearLayoutManager(requireContext())
                    recDireccion.setHasFixedSize(true)
                    direccionUserAdapter = DireccionUserAdapter(it as ArrayList<Direccion>, requireContext())
                    recDireccion.adapter = direccionUserAdapter
                }
            }
        })

        btnDireccion.setOnClickListener(){
            val action = UserFragmentDirections.actionUserFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

        btnSecundarioDireccion. setOnClickListener(){
            val action = UserFragmentDirections.actionUserFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

    }
}