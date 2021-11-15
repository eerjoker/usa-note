package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.view.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.ort.usanote.R
import com.ort.usanote.adapters.CategoryInicioAdapter
import com.ort.usanote.adapters.SliderAdapter
import com.ort.usanote.viewModels.InicioViewModel


class InicioFragment : Fragment() {

    companion object {
        fun newInstance() = InicioFragment()
    }

    private lateinit var viewModel: InicioViewModel
    private lateinit var rootView : View
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRun: Runnable
    private lateinit var recyclerView: RecyclerView
    private val viewModelInicio:InicioViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.inicio_fragment, container, false)

        sliderItems(rootView)
        recyclerView(rootView,requireContext())
        return rootView
    }


    private fun recyclerView(rootView: View, context: Context){
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter =CategoryInicioAdapter(viewModelInicio.getCategoryList(),context){
            onCategoryClick(it)
        }
    }

    private fun onCategoryClick(it:Int){
        viewModelInicio.onCategoryClick(rootView,it)

    }
    private fun sliderItems(rootView:View) {
        var viewPagerImgSlider = rootView.findViewById<ViewPager2>(R.id.viewPagerImgSlider)
        sliderAdapter = SliderAdapter(viewPagerImgSlider,viewModelInicio.getSliderItems()){
            onItemClick(it)
        }
        viewPagerImgSlider.adapter = sliderAdapter
        viewPagerImgSlider.clipToPadding = false
        viewPagerImgSlider.clipChildren = false
        viewPagerImgSlider.offscreenPageLimit = 3
        viewPagerImgSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        viewPagerImgSlider.setPageTransformer(viewModelInicio.getCompositePageTransformer())
        sliderHandler = Handler()
        sliderRun = Runnable {
            viewPagerImgSlider.currentItem = viewPagerImgSlider.currentItem + 1
        }
        viewPagerImgSlider.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRun)
                    sliderHandler.postDelayed(sliderRun, 2000)
                }
            })

    }

    private fun onItemClick(pos: Int){
        viewModelInicio.NavigateToAction(pos,rootView)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRun)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRun, 2000)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InicioViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
    }
}