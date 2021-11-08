package com.ort.usanote.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ort.usanote.R
import com.ort.usanote.adapters.CategoryInicioAdapter
import com.ort.usanote.adapters.ProductAdapter
import com.ort.usanote.adapters.SliderAdapter
import com.ort.usanote.entities.CategoriaInicio
import com.ort.usanote.entities.SliderItem
import com.ort.usanote.viewModels.InicioViewModel
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
    private lateinit var recyclerView: RecyclerView
    private var categoryList: MutableList<CategoriaInicio> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.inicio_fragment, container, false)
        sliderItems(rootView)
        itemSliderView()
        itemsCategory()
        recyclerView(rootView,requireContext())
        return rootView
    }

    private fun itemsCategory() {
        categoryList.clear()
        categoryList.add(CategoriaInicio("Periferico","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7Ak2-27I-akU0k4IRiZ-m6JCv21ByS1-kk1Wo0Q3SQLAHlVs6lEqjTjaoBTAeo49eXtQ&usqp=CAU"))
        categoryList.add(CategoriaInicio("GPU","data:image/png;base64,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"))
        categoryList.add(CategoriaInicio("Procesador","https://www.creativefabrica.com/wp-content/uploads/2020/06/15/cpu-black-and-white-line-icon-Graphics-4364378-1-1-580x387.jpg"))
        categoryList.add(CategoriaInicio("Ram ","https://www.ato.com.my/image/ato/image/data/Icon/Ram.png"))


    }

    private fun itemSliderView() {
        sliderItemList.add(SliderItem(R.drawable.combo))
        sliderItemList.add(SliderItem(R.drawable.gabo_violeta))
        sliderItemList.add(SliderItem(R.drawable.gaborgb))
        sliderItemList.add(SliderItem(R.drawable.mouse))
        sliderItemList.add(SliderItem(R.drawable.setup))
    }
    private fun recyclerView(rootView: View, context: Context){
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(rootView.context)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerView.adapter =CategoryInicioAdapter(categoryList,context){
            onCategoryClick(it)
        }


    }

    private fun onCategoryClick(it:Int){
        val category = categoryList[it].nombre
        categoryList.clear()
        val action = InicioFragmentDirections.actionInicioFragmentToProductosFragment().setCategoria(category)
        rootView.findNavController().navigate(action)

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
//        val action = InicioFragmentDirections.actionInicioFragmentToOpenFromSliderFragment(imagen)
//        rootView.findNavController().navigate(action)
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