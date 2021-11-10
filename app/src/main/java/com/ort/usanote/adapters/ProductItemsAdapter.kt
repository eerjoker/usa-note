package com.ort.usanote.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.ort.usanote.entities.Cart
import com.ort.usanote.entities.Product


class ProductItemsAdapter(
    private var cart : Cart,
    private var context : Context,
) : RecyclerView.Adapter<ProductItemsAdapter.ProductItemHolder>() {
    private val DRAWABLE_LEFT = 0
    private val DRAWABLE_RIGHT = 2
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()

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

        holder.setTitle(productItemList[position].product.nombre)
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
                if (isRightDrawable(productItemQuantity, event.rawX)) {
                    val productoRef = db.collection("productos").document(productItemList[position].product.idProducto)
                    productoRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val product = documentSnapshot.toObject<Product>()
                            if (product != null && product.stock > 0) {
                                cart.incrementProductQuantity(position, 1)
                                updateValues(holder, productItemList[position], 1)
                            } else {
                                val rootLayout = holder.getCardView()
                                Snackbar.make(rootLayout, R.string.no_stock, Snackbar.LENGTH_SHORT)
                                    .setBackgroundTint(getColor(context, R.color.alert_danger))
                                    .show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("Error", "get failed with ", exception)
                        }
                } else if(isLeftDrawable(productItemQuantity, event.rawX)) {
                    if (productItemList[position].quantity > 1) {
                        cart.incrementProductQuantity(position, -1)
                        updateValues(holder, productItemList[position], -1)
                    }
                }
                return@OnTouchListener true
            }
            false
        })

        holder.getDeleteButton().setOnClickListener {
            holder.deleteProductItem()
            cart.deleteProductItem(position)
        }
    }

    override fun getItemCount(): Int {
        return cart.getProductItems().size
    }

    fun updateValues(holder: ProductItemHolder, productItem : ProductItem, quantity: Int) {
        productItem.quantity += quantity
        productItem.product.stock -= quantity
        holder.setQuantity(productItem.quantity)
        holder.setSubtotal(productItem.calculateSubtotal())
    }

    fun isLeftDrawable(element : TextView, x : Float) : Boolean {
        return x <= element.getLeft() + element.getCompoundDrawables().get(DRAWABLE_LEFT).getBounds().width()
    }

    fun isRightDrawable(element : TextView, x : Float) : Boolean {
        return x >= element.getRight() - element.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()
    }
}