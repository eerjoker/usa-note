package com.ort.usanote.fragments.contacto

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.ort.usanote.R
import com.ort.usanote.viewModels.contacto.ContactoViewModel

class ContactoFragment : Fragment() {

    lateinit var v: View
    lateinit var asunto: EditText
    lateinit var consulta: EditText
    lateinit var btnConsulta: Button
    lateinit var rootLayout: ConstraintLayout


    companion object {
        fun newInstance() = ContactoFragment()
    }

    private lateinit var viewModel: ContactoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.contacto_fragment, container, false)

        asunto = v.findViewById(R.id.asuntoContacto)
        consulta = v.findViewById(R.id.textContacto)
        btnConsulta = v.findViewById(R.id.btnContacto)
        rootLayout = v.findViewById(R.id.frameLayout8)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        btnConsulta.setOnClickListener(){

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:ezesalo@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, asunto.text.toString())
                putExtra(Intent.EXTRA_TEXT, consulta.text.toString())
            }

            startActivity(intent)
            v.findNavController().popBackStack()

        }
    }

}