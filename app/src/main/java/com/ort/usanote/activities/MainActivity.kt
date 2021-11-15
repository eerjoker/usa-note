package com.ort.usanote.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
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
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    lateinit var bottomNavigationView : BottomNavigationView
    lateinit var toolbar : Toolbar
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

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.loginFragment -> {
                if (auth.currentUser != null) {
                    auth.signOut()
                    item.icon = resources.getDrawable(R.drawable.baseline_login_white_24dp, theme)
                    updateContextOnLogout()
                }
                NavigationUI.onNavDestinationSelected(item, navController)
            }

            R.id.carritoFragment -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
            R.id.productosFragment ->{
                val searchViewItem = item
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
                            NavHostFragment.findNavController(navHostFragment).navigate(R.id.productosFragment,bundle)


                        return false
                    }
                })


            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomViewListener() {
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

    private fun updateContextOnLogout() {
        // bottombar - remueve item de estadisticas si lo hay
        bottomNavigationView.menu.removeItem(R.id.estadisticasFragment)
    }
}