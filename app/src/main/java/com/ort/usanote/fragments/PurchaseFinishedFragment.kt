package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ort.usanote.R
import com.ort.usanote.viewModels.PurchaseFinishedViewModel

class PurchaseFinishedFragment : Fragment() {

    companion object {
        fun newInstance() = PurchaseFinishedFragment()
    }

    private lateinit var viewModel: PurchaseFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.purchase_finished_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PurchaseFinishedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}