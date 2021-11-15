package com.ort.usanote.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.entities.ProductItemRepository

class MainActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var itemsCarrito : ProductItemRepository = ProductItemRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        constraintLayout = findViewById(R.id.constraint_layout_activity)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_bar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        setBottomViewListener()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)

        val searchViewItem = menu.findItem(R.id.action_search_offline)
        val searchView : SearchView = searchViewItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_placeholder)
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.loginFragment -> {
                if (auth.currentUser != null) {
                    auth.signOut()
                }
                NavigationUI.onNavDestinationSelected(item, navController)
            }

            R.id.carritoFragment -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomViewListener() {
        // se inicializa vacio asi no entra en el otro listener si ya esta seleccionado el item
        bottomNavigationView.setOnItemReselectedListener {  }

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.userFragment -> {
                    if (auth.currentUser == null) {
                        Snackbar.make(constraintLayout, R.string.not_logged_in, Snackbar.LENGTH_SHORT).show()
                        return@setOnItemSelectedListener false
                    }
                }
            }
            NavigationUI.onNavDestinationSelected(it, navController)
            return@setOnItemSelectedListener true
        }
    }
}