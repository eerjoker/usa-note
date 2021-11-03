package com.ort.usanote.entities

class ProductsRepository () {
    private var productList : MutableList<Product> = mutableListOf()
    private val url = "https://edu-delitech2.odoo.com/web/image/product.template/1/image_1024?unique=fb5c381"

    init {
        productList.add(Product("Keyboard", "Keyboard description", 200.0,9,url))
        productList.add(Product("Mouse", "Mouse description", 90.0,10, url))
        productList.add(Product("Microchip", "Microchip description", 170.0,4, url))
        productList.add(Product("Screen", "Screen description", 900.0,3, url))
        productList.add(Product("Headphones", "Headphones description", 940.0,4, url))
        productList.add(Product("Tower", "Tower description", 900.0,3, url))
        productList.add(Product("Ram", "Ram description", 940.0,15, url))
        productList.add(Product("Mats", "Mats description", 900.0,3, url))
        productList.add(Product("GPU", "GPU description", 940.0,4, url))


    }



    fun getProductItems () : MutableList<Product> {
        return productList
    }
}