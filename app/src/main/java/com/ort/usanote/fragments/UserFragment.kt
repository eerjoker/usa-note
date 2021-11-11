package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ort.usanote.R
import com.ort.usanote.adapters.DireccionUserAdapter
import com.ort.usanote.entities.Direccion
import com.ort.usanote.viewModels.UpdateDireccionViewModel
import com.ort.usanote.viewModels.UserViewModel

class UserFragment : Fragment() {


    lateinit var v: View

    lateinit var userTitle: TextView
    lateinit var nombreLayout: TextInputLayout
    lateinit var nombreText: TextInputEditText
    lateinit var btnNombre: FloatingActionButton

    lateinit var apellidoLayout: TextInputLayout
    lateinit var apellidoText: TextInputEditText
    lateinit var btnApellido: FloatingActionButton

    lateinit var emailLayout: TextInputLayout
    lateinit var emailText: TextInputEditText
    lateinit var btnEmail: FloatingActionButton

    lateinit var telefonoLayout: TextInputLayout
    lateinit var telefonoText: TextInputEditText
    lateinit var btnTelefono: FloatingActionButton

    lateinit var btnDireccion: Button
    lateinit var btnSecundarioDireccion: FloatingActionButton

    lateinit var recDireccion: RecyclerView
    lateinit var direccionUserAdapter: DireccionUserAdapter
    lateinit var direcciones: ArrayList<Direccion>
    lateinit var idsDirecciones: MutableList<String>

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

        btnDireccion = v.findViewById(R.id.agregaDireccionbtn)
        btnSecundarioDireccion = v.findViewById(R.id.agredarDireccionMas)
        recDireccion = v.findViewById(R.id.recyclerView2)

        nombreLayout = v.findViewById(R.id.nombreUserInputLayOutTxt)
        nombreText = v.findViewById(R.id.nombreUserTxt)
        btnNombre = v.findViewById(R.id.cambiarNombre)

        apellidoLayout = v.findViewById(R.id.apellidoUserInputLayOutTxt)
        apellidoText = v.findViewById(R.id.apellidoUserTxt)
        btnApellido = v.findViewById(R.id.cambiarApellido)

        emailLayout = v.findViewById(R.id.emailUserInputLayOutTxt)
        emailText = v.findViewById(R.id.emailUserTxt)
        btnEmail = v.findViewById(R.id.cambiarEmail)

        telefonoLayout = v.findViewById(R.id.telefonoUserInputLayOutTxt)
        telefonoText = v.findViewById(R.id.telefonoUserTxt)
        btnTelefono = v.findViewById(R.id.cambiarTelefono)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        direcciones = ArrayList()
        idsDirecciones = mutableListOf()

        recDireccion.layoutManager = LinearLayoutManager(requireContext())
        recDireccion.setHasFixedSize(true)

        viewModelUser.getUser()
        viewModelUser.getDirecciones()

        viewModel.userDb.observe(viewLifecycleOwner, Observer {
            if (it != null){
                nombreText.setText("${it.nombre}")
                apellidoText.setText("${it.apellido}")
                emailText.setText("${it.email}")
                telefonoText.setText("${it.telefono}")
            }
        })

        viewModel.direccionesUser.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                if(it.size > 0){

                    direcciones = it as ArrayList<Direccion>
                    direccionUserAdapter = DireccionUserAdapter(it as ArrayList<Direccion>, requireContext()) { item ->
                        onItemClick(item)
                    }
                    recDireccion.adapter = direccionUserAdapter
                }
            }
        })

        viewModel.idDireccionesUser.observe(viewLifecycleOwner, Observer {
            if (it != null){
                idsDirecciones = it
            }
        })


        btnNombre.setOnClickListener() {
            nombreLayout.isEnabled = true

            nombreText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModel.updateUser("nombre", nombreText.text.toString())
                    nombreLayout.isEnabled = false
                }
            }
        }

        btnApellido.setOnClickListener() {
            apellidoLayout.isEnabled = true

            apellidoText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModel.updateUser("apellido", apellidoText.text.toString())
                    apellidoLayout.isEnabled = false
                }
            }
        }

        btnEmail.setOnClickListener() {
            emailLayout.isEnabled = true

            emailText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModel.updateUser("email", emailText.text.toString())
                    emailLayout.isEnabled = false
                }
            }
        }

        btnTelefono.setOnClickListener() {
            telefonoLayout.isEnabled = true

            telefonoText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModel.updateUser("telefono", telefonoText.text.toString())
                    telefonoLayout.isEnabled = false
                }
            }
        }

        btnDireccion.setOnClickListener(){
            val action = UserFragmentDirections.actionUserFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

        btnSecundarioDireccion. setOnClickListener(){
            val action = UserFragmentDirections.actionUserFragmentToDireccionFragment()
            v.findNavController().navigate(action)
        }

    }

    // agregar id("kotlin-parcelize") al gradle
    // ajustar a 2.4.0-alpha10 el navigation
    fun onItemClick (pos: Int){

        var direccionAux = direcciones[pos]
        var idsDirecciones = idsDirecciones[pos]

        val action = UserFragmentDirections.actionUserFragmentToUpdateDireccionFragment(direccionAux, idsDirecciones)
        v.findNavController().navigate(action)

    }
}