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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.ort.usanote.R
import com.ort.usanote.viewModels.contacto.ContactoViewModel

class ContactoFragment : Fragment() {

    lateinit var v: View
    lateinit var btnConsulta: Button
    lateinit var btnWP: Button
    lateinit var rootLayout: ConstraintLayout

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

        return v
    }

    override fun onStart() {
        super.onStart()

        btnConsulta.setOnClickListener(){

            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:info@usanote.com.ar")
            }
            startActivity(intent)
            v.findNavController().popBackStack()

        }

        btnWP.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=5491157346424"))
            startActivity(intent)
            v.findNavController().popBackStack()
        }
    }

}