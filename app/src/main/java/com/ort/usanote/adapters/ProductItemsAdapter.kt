package com.ort.usanote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.usanote.R
import com.ort.usanote.entities.ProductItem

class ProductItemsAdapter(
    var productItemList : MutableList<ProductItem>,
    var context : Context,
    var onClick : (Int) -> Unit
) : RecyclerView.Adapter<ProductItemsAdapter.ProductItemHolder>() {

    class ProductItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view : View

        init {
            this.view = v
        }

        fun setTitle(title: String) {
            var txtTitle : TextView = view.findViewById(R.id.txtTitleProductItem)
            txtTitle.text = title
        }

        fun setImage(context: Context, imageUrl: String) {
            var imgProductItem : ImageView = view.findViewById(R.id.imgProductItem)
            Glide
                .with(context)
                .load(imageUrl)
                .centerInside()
                .into(imgProductItem)
        }

        fun setQuantity(quantity: Int) {
            var txtQuantity : TextView = view.findViewById(R.id.txtProductItemQuantity)
            txtQuantity.text = quantity.toString()
        }

        fun setSubtotal(subtotal: Double) {
            var txtSubtotal : TextView = view.findViewById(R.id.txtProductItemSubtotal)
            txtSubtotal.text = R.string.dollar_sign.toString() + subtotal.toString()
        }

        fun getCardView() : CardView {
            return view.findViewById(R.id.cardProductItem)
        }

        fun getDeleteButton() : ImageButton {
            return view.findViewById(R.id.imgBtnDeleteProductItem)
        }

        fun deleteProductItem() {
            getCardView().removeAllViews()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return (ProductItemHolder(view))
    }

    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        holder.setTitle(productItemList[position].product.title)
        holder.setImage(context, productItemList[position].product.imageUrl)
        holder.setQuantity(productItemList[position].quantity)
        holder.setSubtotal(productItemList[position].calculateSubtotal())
        holder.getDeleteButton().setOnClickListener {
            holder.deleteProductItem()
        }
        holder.getCardView().setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return productItemList.size
    }
}