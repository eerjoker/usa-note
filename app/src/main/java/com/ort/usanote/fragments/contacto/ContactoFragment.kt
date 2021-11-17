package com.ort.usanote.fragments.contacto

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.ort.usanote.R
import com.ort.usanote.viewModels.contacto.ContactoViewModel
import org.w3c.dom.Text

class ContactoFragment : Fragment() {

    lateinit var v: View
    lateinit var btnConsulta: Button
    lateinit var btnWP: Button
    lateinit var rootLayout: ConstraintLayout
    lateinit var txtWP: TextView
    lateinit var txtEmail: TextView

    companion object {
        fun newInstance() = ContactoFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.contacto_fragment, container, false)

        btnConsulta = v.findViewById(R.id.btnContacto)
        btnWP = v.findViewById(R.id.btnWP)
        rootLayout = v.findViewById(R.id.frameLayout8)
        txtWP = v.findViewById(R.id.txtWP)
        txtEmail = v.findViewById(R.id.txtEmail)

        return v
    }

    override fun onStart() {
        super.onStart()
        val emailContacto = "info@usanote.com.ar"
        val celularContacto = "+54 9 1155702911"
        val textoWP = "Hola! Me gustaría contactarme con ustedes para revisar un tema"

        txtWP.text = celularContacto
        txtEmail.text = emailContacto

        btnConsulta.setOnClickListener(){

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:" + emailContacto)
            }
            startActivity(intent)
            v.findNavController().popBackStack()

        }

        btnWP.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + celularContacto + "&text=" + textoWP))
            startActivity(intent)
            v.findNavController().popBackStack()
        }
    }

}