package com.ort.usanote.entities

import android.content.SharedPreferences

class Cart (
    private var productItemList : MutableList<ProductItem>,
    var onChange : (Double) -> Unit
) {

//    init {
//        productItemList.add(ProductItem(Product("Keyboard", "Keyboard description", 200.0, url), 2))
//        productItemList.add(ProductItem(Product("Mouse", "Mouse description", 90.0, url), 5))
//        productItemList.add(ProductItem(Product("Microchip", "Microchip description", 170.0, url), 1))
//        productItemList.add(ProductItem(Product("Screen", "Screen description", 900.0, url), 3))
//        productItemList.add(ProductItem(Product("Headphones", "Headphones description", 940.0, url), 2))
//    }

    fun getProductItems () : MutableList<ProductItem> {
        return productItemList
    }

    fun modifyProductItemQuantity(pos: Int, quantity: Int) {
        productItemList[pos].quantity = quantity
        onChange(calculateSubtotal())
    }

    fun addProductItem(productItem: ProductItem) {
        productItemList.add(productItem)
        onChange(calculateSubtotal())
    }

    fun deleteProductItem(pos: Int) {
        productItemList.removeAt(pos)
        onChange(calculateSubtotal())
    }

    fun calculateSubtotal() : Double {
        var subtotal = 0.0

        for (productItem in productItemList ) {
            subtotal += productItem.calculateSubtotal()
        }
        return subtotal
    }
}