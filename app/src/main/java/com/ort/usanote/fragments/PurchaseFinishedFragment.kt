package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.ort.usanote.R
import com.ort.usanote.activities.SearchActivity
import com.ort.usanote.viewModels.PurchaseFinishedViewModel

class PurchaseFinishedFragment : Fragment() {
    lateinit var btnBackToHome : Button
    lateinit var v: View

    companion object {
        fun newInstance() = PurchaseFinishedFragment()
    }

    private lateinit var viewModel: PurchaseFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.purchase_finished_fragment, container, false)
        btnBackToHome = v.findViewById(R.id.buttonBackHome)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseFinishedViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        btnBackToHome.setOnClickListener {
            val itemsCarrito = (activity as SearchActivity).itemsCarrito
            print(itemsCarrito)
            itemsCarrito.dropList()
            print(itemsCarrito)
            val action = PurchaseFinishedFragmentDirections.actionPurchaseFinishedFragmentToInicioFragment()
            v.findNavController().navigate(action)

        }
    }

}