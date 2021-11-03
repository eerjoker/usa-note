package com.ort.usanote.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ort.usanote.R
import com.ort.usanote.adapters.SliderAdapter
import com.ort.usanote.entities.SliderItem
import kotlin.math.abs

class InicioFragment : Fragment() {

    companion object {
        fun newInstance() = InicioFragment()
    }

    private lateinit var viewModel: InicioViewModel
    private lateinit var rootView : View
    private lateinit var sliderItemList: ArrayList<SliderItem>
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRun: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.inicio_fragment, container, false)
        sliderItems(rootView)
        itemSliderView()
        return rootView
    }
    private fun itemSliderView() {
        sliderItemList.add(SliderItem(R.drawable.combo))
        sliderItemList.add(SliderItem(R.drawable.gabo_violeta))
        sliderItemList.add(SliderItem(R.drawable.gaborgb))
        sliderItemList.add(SliderItem(R.drawable.mouse))
        sliderItemList.add(SliderItem(R.drawable.setup))
    }

    private fun sliderItems(rootView:View) {
        sliderItemList = ArrayList()
        var viewPagerImgSlider = rootView.findViewById<ViewPager2>(R.id.viewPagerImgSlider)
        sliderAdapter = SliderAdapter(viewPagerImgSlider,sliderItemList){
            onItemClick(it)
        }
        viewPagerImgSlider.adapter = sliderAdapter
        viewPagerImgSlider.clipToPadding = false
        viewPagerImgSlider.clipChildren = false
        viewPagerImgSlider.offscreenPageLimit = 3
        viewPagerImgSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val comPosPageTarn = CompositePageTransformer()
        comPosPageTarn.addTransformer(MarginPageTransformer(40))
        comPosPageTarn.addTransformer { page, position ->
            val r :Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPagerImgSlider.setPageTransformer(comPosPageTarn)
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
        var imagen = sliderItemList[pos].imagen
        val action = InicioFragmentDirections.actionInicioFragmentToOpenFromSliderFragment(imagen)
        rootView.findNavController().navigate(action)
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

}