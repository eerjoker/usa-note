package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ort.usanote.R
import com.ort.usanote.viewModels.OpenFromSliderViewModel

class OpenFromSliderFragment : Fragment() {
    lateinit var v: View
    companion object {
        fun newInstance() = OpenFromSliderFragment()
    }

    private lateinit var viewModel: OpenFromSliderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.open_from_slider_fragment, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OpenFromSliderViewModel::class.java)
    }
    override fun onStart() {
        super.onStart()
        var img = OpenFromSliderFragmentArgs.fromBundle(requireArguments()).image
        var display = v.findViewById<ImageView>(R.id.display)
        display.setImageResource(img)

    }

}