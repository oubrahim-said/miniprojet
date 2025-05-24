package com.example.miniprojet

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var googleAuthManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize auth manager
        googleAuthManager = GoogleAuthManager(this)

        // Set up the toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Set up the navigation drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)

        // Configure the navigation drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_courses, R.id.nav_announcements,
                R.id.nav_documents, R.id.nav_profile, R.id.nav_settings
            ), drawerLayout
        )

        // Set up the ActionBar with the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        // Update navigation header with user info
        updateNavigationHeader()

        // Set up back press handler
        setupBackPressHandler()
    }

    private fun updateNavigationHeader() {
        val account = googleAuthManager.getLastSignedInAccount()
        if (account != null) {
            val headerView = binding.navView.getHeaderView(0)
            val ivProfile = headerView.findViewById<ImageView>(R.id.iv_profile)
            val tvUserName = headerView.findViewById<TextView>(R.id.tv_user_name)
            val tvUserEmail = headerView.findViewById<TextView>(R.id.tv_user_email)

            tvUserName.text = account.displayName
            tvUserEmail.text = account.email

            // Load profile image with Glide
            account.photoUrl?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(ivProfile)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                googleAuthManager.signOut()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks
        when (item.itemId) {
            R.id.nav_dashboard -> navController.navigate(R.id.nav_dashboard)
            R.id.nav_courses -> navController.navigate(R.id.nav_courses)
            R.id.nav_announcements -> navController.navigate(R.id.nav_announcements)
            R.id.nav_documents -> navController.navigate(R.id.nav_documents)
            R.id.nav_profile -> navController.navigate(R.id.nav_profile)
            R.id.nav_settings -> navController.navigate(R.id.nav_settings)
        }

        // Close the drawer
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Using the new OnBackPressedCallback approach instead of deprecated onBackPressed()
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}