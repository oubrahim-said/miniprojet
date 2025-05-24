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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.ActivityMainBinding
import com.example.miniprojet.ui.announcements.AnnouncementsFragment
import com.example.miniprojet.ui.auth.AuthActivity
import com.example.miniprojet.ui.courses.CoursesFragment
import com.example.miniprojet.ui.dashboard.DashboardFragment
import com.example.miniprojet.ui.documents.DocumentsFragment
import com.example.miniprojet.ui.profile.ProfileFragment
import com.example.miniprojet.ui.settings.SettingsFragment
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

            // Configure the navigation drawer for manual navigation
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_dashboard, R.id.nav_courses, R.id.nav_announcements,
                    R.id.nav_documents, R.id.nav_profile, R.id.nav_settings
                ), drawerLayout
            )

            // Set up custom navigation listener (not using Navigation Component)
            navView.setNavigationItemSelectedListener(this)

            // Set the initial fragment to Dashboard
            changefragment(DashboardFragment())

            // Set the checked item to dashboard
            navView.setCheckedItem(R.id.nav_dashboard)

            println("Navigation setup completed successfully")

        } catch (e: Exception) {
            e.printStackTrace()
            println("Navigation setup failed: ${e.message}")
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
            println("Navigation item selected: ${item.title} (ID: ${item.itemId})")

            // Handle navigation view item clicks
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    println("Navigating to Dashboard")
                    changefragment(DashboardFragment())
                    binding.navView.setCheckedItem(R.id.nav_dashboard)
                }
                R.id.nav_courses -> {
                    println("Navigating to Courses")
                    changefragment(CoursesFragment())
                    binding.navView.setCheckedItem(R.id.nav_courses)
                }
                R.id.nav_announcements -> {
                    println("Navigating to Announcements")
                    changefragment(AnnouncementsFragment())
                    binding.navView.setCheckedItem(R.id.nav_announcements)
                }
                R.id.nav_documents -> {
                    println("Navigating to Documents")
                    changefragment(DocumentsFragment())
                    binding.navView.setCheckedItem(R.id.nav_documents)
                }
                R.id.nav_profile -> {
                    println("Navigating to Profile")
                    changefragment(ProfileFragment())
                    binding.navView.setCheckedItem(R.id.nav_profile)
                }
                R.id.nav_settings -> {
                    println("Navigating to Settings")
                    changefragment(SettingsFragment())
                    binding.navView.setCheckedItem(R.id.nav_settings)
                }
                else -> {
                    println("Unknown navigation item: ${item.itemId}")
                    return false
                }
            }

            // Close the drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error in onNavigationItemSelected: ${e.message}")
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
        // Handle up navigation manually since we're not using Navigation Component
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        return super.onSupportNavigateUp()
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

    private fun changefragment(fragment: Fragment) {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, fragment)
            // Only add to back stack if it's not the dashboard (home) fragment
            if (fragment !is DashboardFragment) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
            println("Fragment changed successfully to: ${fragment::class.java.simpleName}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error changing fragment: ${e.message}")
        }
    }
}