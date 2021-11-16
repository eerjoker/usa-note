package com.ort.usanote.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.ort.usanote.R
import com.ort.usanote.entities.ProductItemRepository
import com.ort.usanote.viewModels.auth.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    lateinit var bottomNavigationView : BottomNavigationView
    lateinit var toolbar : Toolbar
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var itemsCarrito : ProductItemRepository = ProductItemRepository()
    var actionForRedirection : NavDirections? = null
    var alertDangerMessage = ""
    val viewModelLogin: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        constraintLayout = findViewById(R.id.constraint_layout_activity)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_bar)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        configureBottomView()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModelLogin.checkIsAdmin()
    }

    private fun setObservers() {
        viewModelLogin.esAdmin.observe(this, Observer {
            if(it) {
                // bottombar - item estadisticas
                bottomNavigationView.menu.add(Menu.NONE, R.id.estadisticasFragment, Menu.NONE
                    , getString(R.string.estadisticas))
                bottomNavigationView.menu.findItem(R.id.estadisticasFragment)
                    .setIcon(R.drawable.baseline_equalizer_white_24dp)
            }
            if (!it) {
                // bottombar - item estadisticas
                bottomNavigationView.menu.removeItem(R.id.estadisticasFragment)
            }

            // toolbar - item carrito
            val itemCarrito = toolbar.menu.findItem(R.id.carritoFragment)
            if (itemCarrito != null) {
                itemCarrito.isEnabled = !it
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        if (auth.currentUser != null) {
            val loginItem = menu.findItem(R.id.loginFragment)
            loginItem.icon = resources.getDrawable(R.drawable.baseline_logout_white_24dp, theme)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {

            R.id.loginFragment -> {
                if (auth.currentUser != null) {
                    viewModelLogin.logOut()
                    item.icon = resources.getDrawable(R.drawable.baseline_login_white_24dp, theme)
                }
                NavigationUI.onNavDestinationSelected(item, navController)
            }

            R.id.carritoFragment -> {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
            R.id.productosFragment ->{
                val searchViewItem = item
                val searchView : SearchView = searchViewItem.actionView as SearchView
                searchView.setBackgroundColor(getColor(R.color.white))
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

    private fun configureBottomView() {
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

    fun redirectDone() {
        actionForRedirection = null
        alertDangerMessage = ""
    }
}