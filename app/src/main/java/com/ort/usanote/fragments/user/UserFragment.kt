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
    lateinit var btnConfirmarNombre: FloatingActionButton

    lateinit var apellidoLayout: TextInputLayout
    lateinit var apellidoText: TextInputEditText
    lateinit var btnApellido: FloatingActionButton
    lateinit var btnConfirmarApellido: FloatingActionButton

    lateinit var emailLayout: TextInputLayout
    lateinit var emailText: TextInputEditText
    lateinit var btnEmail: FloatingActionButton
    lateinit var btnConfirmarEmail: FloatingActionButton

    lateinit var telefonoLayout: TextInputLayout
    lateinit var telefonoText: TextInputEditText
    lateinit var btnTelefono: FloatingActionButton
    lateinit var btnConfirmarTelefono: FloatingActionButton

    lateinit var btnDireccion: Button
    lateinit var btnSecundarioDireccion: FloatingActionButton
    lateinit var progressBar: ProgressBar

    lateinit var recDireccion: RecyclerView
    lateinit var direccionUserAdapter: DireccionUserAdapter
    lateinit var direcciones: ArrayList<Direccion>
    lateinit var idsDirecciones: MutableList<String>

    private lateinit var btnToMisCompras : Button

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
        btnConfirmarNombre = v.findViewById(R.id.confirmarNombre)

        apellidoLayout = v.findViewById(R.id.apellidoUserInputLayOutTxt)
        apellidoText = v.findViewById(R.id.apellidoUserTxt)
        btnApellido = v.findViewById(R.id.cambiarApellido)
        btnConfirmarApellido = v.findViewById(R.id.confirmarApellido)

        emailLayout = v.findViewById(R.id.emailUserInputLayOutTxt)
        emailText = v.findViewById(R.id.emailUserTxt)
        btnEmail = v.findViewById(R.id.cambiarEmail)
        btnConfirmarEmail = v.findViewById(R.id.confirmarEmail)

        telefonoLayout = v.findViewById(R.id.telefonoUserInputLayOutTxt)
        telefonoText = v.findViewById(R.id.telefonoUserTxt)
        btnTelefono = v.findViewById(R.id.cambiarTelefono)
        btnConfirmarTelefono = v.findViewById(R.id.confirmarTelefono)

        progressBar = v.findViewById(R.id.progressBar3)

        btnToMisCompras = v.findViewById(R.id.btnToMisCompras)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

        btnConfirmarNombre.hide()
        btnConfirmarApellido.hide()
        btnConfirmarEmail.hide()
        btnConfirmarTelefono.hide()

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
            btnNombre.hide()
            btnConfirmarNombre.show()

            nombreLayout.isEnabled = true

            btnConfirmarNombre.setOnClickListener(){

                var msgError = viewModelUser.validateNombreApellido(nombreText.text.toString().trim())
                if (msgError != null){
                    nombreLayout.error = msgError
                }else{
                    btnConfirmarNombre.hide()
                    btnNombre.show()
                    viewModelUser.updateUser("nombre", nombreText.text.toString().trim())
                    nombreLayout.isEnabled = false
                }
            }
        }

        btnApellido.setOnClickListener() {
            btnApellido.hide()
            btnConfirmarApellido.show()

            apellidoLayout.isEnabled = true

            btnConfirmarApellido.setOnClickListener(){

                var msgError = viewModelUser.validateNombreApellido(apellidoText.text.toString().trim())
                if (msgError != null){
                    apellidoLayout.error = msgError
                }else{
                    btnConfirmarApellido.hide()
                    btnApellido.show()
                    viewModelUser.updateUser("apellido", apellidoText.text.toString().trim())
                    apellidoLayout.isEnabled = false
                }
            }
        }

        btnEmail.setOnClickListener() {
            btnEmail.hide()
            btnConfirmarEmail.show()

            emailLayout.isEnabled = true

            btnConfirmarEmail.setOnClickListener(){

                var msgError = viewModelUser.validateEmail(emailText.text.toString().trim())
                if (msgError != null){
                    emailLayout.error = msgError
                }else{
                    btnConfirmarEmail.hide()
                    btnEmail.show()
                    viewModelUser.updateUser("email", emailText.text.toString().trim())
                    viewModelUser.updateEmail(emailText.text.toString().trim())
                    emailLayout.isEnabled = false
                }
            }
        }

        btnTelefono.setOnClickListener() {
            btnTelefono.hide()
            btnConfirmarTelefono.show()

            telefonoLayout.isEnabled = true

            btnConfirmarTelefono.setOnClickListener(){

                var msgError = viewModelUser.validateTelefono(telefonoText.text.toString().trim())
                if (msgError != null){
                    telefonoLayout.error = msgError
                }else{
                    btnConfirmarTelefono.hide()
                    btnTelefono.show()
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

        btnToMisCompras.setOnClickListener {
            val action = UserFragmentDirections.actionUserFragmentToMisComprasFragment()
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