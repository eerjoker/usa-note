package com.ort.usanote.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ort.usanote.R
import com.ort.usanote.entities.SliderItem

class SliderAdapter (
    private val viewPager: ViewPager2,
    private val imgList: ArrayList<SliderItem>,
    val onClick : (Int) -> Unit
): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>()
{
    inner class SliderViewHolder(var v: View): RecyclerView.ViewHolder(v){
        val img = v.findViewById<ImageView>(R.id.imageSlider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.slider_item,parent,false)
        return SliderViewHolder(v)
    }

    override fun getItemCount(): Int = imgList.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val listImg = imgList[position]
        holder.img.setImageResource(listImg.imagen)
        if (position == imgList.size - 2){
            viewPager.post(run)
        }
        holder.img.setOnClickListener {
            onClick(position)
        }
    }

    private val run = Runnable {
        imgList.addAll(imgList)
        notifyDataSetChanged()
    }

}