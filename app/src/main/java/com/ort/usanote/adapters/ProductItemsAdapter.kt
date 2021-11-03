package com.ort.usanote.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.usanote.R
import com.ort.usanote.entities.ProductItem
import android.view.View.OnTouchListener
import androidx.core.widget.addTextChangedListener
import com.ort.usanote.entities.Cart


class ProductItemsAdapter(
    private var cart : Cart,
    private var context : Context
) : RecyclerView.Adapter<ProductItemsAdapter.ProductItemHolder>() {
    private val DRAWABLE_LEFT = 0
    private val DRAWABLE_RIGHT = 2

    class ProductItemHolder(v: View, parentView: ViewGroup) : RecyclerView.ViewHolder(v) {
        private var view : View
        private var parent : ViewGroup

        init {
            this.view = v
            this.parent = parentView
        }

        fun setTitle(title: String) {
            val txtTitle : TextView = view.findViewById(R.id.txtTitleProductItem)
            txtTitle.text = title
        }

        fun setImage(context: Context, imageUrl: String) {
            val imgProductItem : ImageView = view.findViewById(R.id.imgProductItem)
            Glide
                .with(context)
                .load(imageUrl)
                .centerInside()
                .into(imgProductItem)
        }

        fun setQuantity(quantity: Int) {
            val txtQuantity : TextView = view.findViewById(R.id.txtProductItemQuantity)
            txtQuantity.text = quantity.toString()
        }

        fun setSubtotal(subtotal: Double) {
            val txtSubtotal : TextView = view.findViewById(R.id.txtProductItemSubtotal)
            txtSubtotal.text = "$" + subtotal.toString()
        }

        fun getCardView() : CardView {
            return view.findViewById(R.id.cardProductItem)
        }

        fun getDeleteButton() : ImageButton {
            return view.findViewById(R.id.imgBtnDeleteProductItem)
        }

        fun getQuantityEditText() : EditText {
            return view.findViewById(R.id.txtProductItemQuantity)
        }

        fun deleteProductItem() {
            parent.removeView(getCardView())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_product_item, parent, false)
        return (ProductItemHolder(view, parent))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ProductItemHolder, position: Int) {
        val productItemList : MutableList<ProductItem> = cart.getProductItems()

        holder.setTitle(productItemList[position].product.title)
        holder.setImage(context, productItemList[position].product.imageUrl)
        holder.setQuantity(productItemList[position].quantity)
        holder.setSubtotal(productItemList[position].calculateSubtotal())

        val productItemQuantity = holder.getQuantityEditText()
        productItemQuantity.addTextChangedListener() {
            productItemList[position].quantity = Integer.parseInt(productItemQuantity.text.toString())
            holder.setSubtotal(productItemList[position].calculateSubtotal())
        }
        productItemQuantity.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= productItemQuantity.getRight() - productItemQuantity.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    // your action here
                    productItemList[position].quantity += 1
                    //return@OnTouchListener true
                } else if(event.rawX <= productItemQuantity.getLeft() + productItemQuantity.getCompoundDrawables()
                        .get(DRAWABLE_LEFT).getBounds().width()) {
                    if (productItemList[position].quantity > 1) {
                        productItemList[position].quantity -= 1
                    }
                }
                holder.setQuantity(productItemList[position].quantity)
                holder.setSubtotal(productItemList[position].calculateSubtotal())
                cart.modifyProductItemQuantity(position, productItemList[position].quantity)
                return@OnTouchListener true
            }
            false
        })

        holder.getDeleteButton().setOnClickListener {
            holder.deleteProductItem()
            //notifyItemRemoved(position)
            cart.deleteProductItem(position)
        }
    }

    override fun getItemCount(): Int {
        return cart.getProductItems().size
    }
}