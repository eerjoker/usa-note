package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ort.usanote.R
import com.ort.usanote.viewModels.ComprasViewModel

class ComprasFragment : Fragment() {

    companion object {
        fun newInstance() = ComprasFragment()
    }

    private lateinit var viewModel: ComprasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.compras_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ComprasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}