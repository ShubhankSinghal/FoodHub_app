package com.shubhank.foodhub_app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar;
import com.shubhank.foodhub_app.R
import com.shubhank.foodhub_app.fragment.HomeFragment
import com.shubhank.foodhub_app.fragment.MyProfileFragment
import com.shubhank.foodhub_app.fragment.FavoriteRestaurantsFragment
import com.shubhank.foodhub_app.fragment.OrderHistoryFragment
import com.shubhank.foodhub_app.fragment.FAQsFragment
import kotlinx.android.synthetic.main.drawer_header.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences

    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar()
        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.home -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favorite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavoriteRestaurantsFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Favorite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            MyProfileFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FAQsFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()
                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderHistoryFragment()
                        )
                        .commit()
                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {

                    val intent = Intent(this@MainActivity, LogoutActivity::class.java)
                    startActivity(intent)

                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is HomeFragment -> openDashboard()
            else -> super.onBackPressed()
        }
    }
}
