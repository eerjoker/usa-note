package com.ort.usanote.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.entities.ProductItemRepository


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    var itemsCarrito : ProductItemRepository = ProductItemRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_bar)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    /*override fun onOptionsItemSelected(item: MenuItem) = item.onNavDestinationSelected(navController)
            || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)
    */
    /*override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the toolbar if it is present.
        menuInflater.inflate(R.menu.toolbar, menu)
        /*val searchView_item = menu.findItem(R.id.action_search_offline)
        val searchView:SearchView
        searchView = searchView_item.actionView as SearchView
        searchView.queryHint = "Ingrese el nombre del producto a buscar"
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

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

        })*/
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = when(item.itemId) {

            //R.id.action_login_logout ->

            //R.id.action_carrito ->

            else -> ""
        }
        return super.onOptionsItemSelected(item)
    }
}