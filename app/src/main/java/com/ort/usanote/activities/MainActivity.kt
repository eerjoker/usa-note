package com.ort.usanote.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.entities.Orden
import com.ort.usanote.entities.Product
import com.ort.usanote.entities.ProductItemRepository


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    var itemsCarrito : ProductItemRepository = ProductItemRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout_search)
        navigationView = findViewById(R.id.navigation_view_search)
        navController = Navigation.findNavController(this,R.id.nav_host_fragment_search)
        navigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.not_logged_drawer_menu, menu)
        val searchView_item = menu.findItem(R.id.action_search_offline)
        val searchView:androidx.appcompat.widget.SearchView
        searchView = searchView_item.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Ingrese el nombre del producto a buscar"
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }


            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                val bundle = bundleOf("searchQuery" to query,
                "categoria" to "busqueda")
                val action = NavDeepLinkBuilder(this@MainActivity)
                    .setGraph(R.navigation.main_navgraph)
                    .addDestination(R.id.productosFragment,bundle)
                    .createPendingIntent()
                action.send()
                return false
            }

        })
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}