package com.ort.usanote.entities

import android.os.Parcel
import android.os.Parcelable

@Parcelize
class ProductItemRepository() : Parcelable {
    private var productItemList : MutableList<ProductItem> = mutableListOf()
    private val url = "https://edu-delitech2.odoo.com/web/image/product.template/1/image_1024?unique=fb5c381"

    constructor(parcel: Parcel) : this() {

    }

    init {
        //productItemList.add(ProductItem(Product("Keyboard", "Keyboard description", 200.0, 4,url), 2))
        //productItemList.add(ProductItem(Product("Mouse", "Mouse description", 90.0,4, url), 5))
        //productItemList.add(ProductItem(Product("Microchip", "Microchip description", 170.0,4, url), 1))
        //productItemList.add(ProductItem(Product("Screen", "Screen description", 900.0,4, url), 3))
        //productItemList.add(ProductItem(Product("Headphones", "Headphones description", 940.0,4, url), 2))
    }
    fun getThis():ProductItemRepository{
        return this
    }
    fun dropList(){
        productItemList.clear()
        print(productItemList)
    }
    fun addProductItem(product:Product,cant:Int){
        productItemList.add(ProductItem(product,cant))

    }
    fun size():Int{
        return productItemList.size
    }
    fun getProductItems () : MutableList<ProductItem> {
        return productItemList
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItemRepository> {
        override fun createFromParcel(parcel: Parcel): ProductItemRepository {
            return ProductItemRepository(parcel)
        }

        override fun newArray(size: Int): Array<ProductItemRepository?> {
            return arrayOfNulls(size)
        }
    }
}

annotation class Parcelize
