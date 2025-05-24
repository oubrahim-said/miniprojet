package com.example.miniprojet

import android.content.Intent
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
import com.example.miniprojet.ui.auth.AuthActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var googleAuthManager: GoogleAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize auth manager
            googleAuthManager = GoogleAuthManager(this)

            // Verify authentication
            val account = googleAuthManager.getLastSignedInAccount()
            if (account == null) {
                navigateToAuth()
                return
            }

            // Set up the toolbar
            setupToolbar()

            // Set up navigation
            setupNavigation()

            // Update navigation header with user info
            updateNavigationHeader()

            // Set up back press handler
            setupBackPressHandler()

        } catch (e: Exception) {
            e.printStackTrace()
            // If there's any error during initialization, go back to auth
            navigateToAuth()
        }
    }

    private fun setupToolbar() {
        try {
            val toolbar: Toolbar = binding.toolbar
            setSupportActionBar(toolbar)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupNavigation() {
        try {
            // Set up the navigation drawer
            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView

            // Find the NavController
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

        } catch (e: Exception) {
            e.printStackTrace()
            // If navigation setup fails, we can still continue without it
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun updateNavigationHeader() {
        try {
            val account = googleAuthManager.getLastSignedInAccount()
            if (account != null && binding.navView.headerCount > 0) {
                val headerView = binding.navView.getHeaderView(0)
                val ivProfile = headerView?.findViewById<ImageView>(R.id.iv_profile)
                val tvUserName = headerView?.findViewById<TextView>(R.id.tv_user_name)
                val tvUserEmail = headerView?.findViewById<TextView>(R.id.tv_user_email)

                tvUserName?.text = account.displayName ?: "User"
                tvUserEmail?.text = account.email ?: ""

                // Load profile image with Glide
                ivProfile?.let { imageView ->
                    account.photoUrl?.let { photoUrl ->
                        try {
                            Glide.with(this)
                                .load(photoUrl)
                                .circleCrop()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                                .into(imageView)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // Set default image if Glide fails
                            imageView.setImageResource(R.mipmap.ic_launcher_round)
                        }
                    } ?: run {
                        // Set default image if no photo URL
                        imageView.setImageResource(R.mipmap.ic_launcher_round)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Navigation header update failed, but app can continue
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
                val intent = Intent(this, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        try {
            // Handle navigation view item clicks
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_dashboard)
                    }
                }
                R.id.nav_courses -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_courses)
                    }
                }
                R.id.nav_announcements -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_announcements)
                    }
                }
                R.id.nav_documents -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_documents)
                    }
                }
                R.id.nav_profile -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_profile)
                    }
                }
                R.id.nav_settings -> {
                    if (::navController.isInitialized) {
                        navController.navigate(R.id.nav_settings)
                    }
                }
            }

            // Close the drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } catch (e: Exception) {
            e.printStackTrace()
            // If navigation fails, just close the drawer
            try {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
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