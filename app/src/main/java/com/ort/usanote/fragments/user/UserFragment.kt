package com.ort.usanote.fragments.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
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
import com.ort.usanote.viewModels.user.UserViewModel

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
    lateinit var progressBar: ProgressBar

    lateinit var recDireccion: RecyclerView
    lateinit var direccionUserAdapter: DireccionUserAdapter
    lateinit var direcciones: ArrayList<Direccion>
    lateinit var idsDirecciones: MutableList<String>

    private val viewModelUser: UserViewModel by viewModels()

    companion object {
        fun newInstance() = UserFragment()
    }

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

        progressBar = v.findViewById(R.id.progressBar3)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        progressBar.setVisibility(View.VISIBLE)

        direcciones = ArrayList()
        idsDirecciones = mutableListOf()

        recDireccion.layoutManager = LinearLayoutManager(requireContext())
        recDireccion.setHasFixedSize(true)

        viewModelUser.getUser()
        viewModelUser.getDirecciones()

        viewModelUser.userDb.observe(viewLifecycleOwner, Observer {
            if (it != null){
                nombreText.setText("${it.nombre.trim()}")
                apellidoText.setText("${it.apellido.trim()}")
                emailText.setText("${it.email.trim()}")
                telefonoText.setText("${it.telefono.trim()}")
            }
        })

        viewModelUser.direccionesUser.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                progressBar.setVisibility(View.GONE)
                if(it.size >= 0){
                    direcciones = it as ArrayList<Direccion>
                    direccionUserAdapter = DireccionUserAdapter(it as ArrayList<Direccion>, requireContext()) { item ->
                        onItemClick(item)
                    }
                    recDireccion.adapter = direccionUserAdapter
                    direccionUserAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModelUser.idDireccionesUser.observe(viewLifecycleOwner, Observer {
            if (it != null){
                idsDirecciones = it
            }
        })


        btnNombre.setOnClickListener() {
            nombreLayout.isEnabled = true

            nombreText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModelUser.updateUser("nombre", nombreText.text.toString().trim())
                    nombreLayout.isEnabled = false
                }
            }
        }

        btnApellido.setOnClickListener() {
            apellidoLayout.isEnabled = true

            apellidoText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModelUser.updateUser("apellido", apellidoText.text.toString().trim())
                    apellidoLayout.isEnabled = false
                }
            }
        }

        btnEmail.setOnClickListener() {
            emailLayout.isEnabled = true
            emailText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModelUser.updateUser("email", emailText.text.toString().trim())
                    viewModelUser.updateEmail(emailText.text.toString().trim())
                    emailLayout.isEnabled = false
                }
            }
        }

        btnTelefono.setOnClickListener() {
            telefonoLayout.isEnabled = true

            telefonoText.setOnFocusChangeListener {_, hasFocus ->
                if (!hasFocus){
                    viewModelUser.updateUser("telefono", telefonoText.text.toString().trim())
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

        val action = UserFragmentDirections.actionUserFragmentToUpdateDireccionFragment(
                direccionAux,
                idsDirecciones
            )
        v.findNavController().navigate(action)

    }
}