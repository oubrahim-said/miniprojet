package com.example.miniprojet.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miniprojet.MainActivity
import com.example.miniprojet.R
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    private lateinit var googleAuthManager: GoogleAuthManager
    
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        binding.progressBar.visibility = View.VISIBLE
        val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(result.data)
        
        lifecycleScope.launch {
            val authResult = googleAuthManager.handleSignInResult(task)
            
            binding.progressBar.visibility = View.GONE
            
            if (authResult.isSuccess) {
                Toast.makeText(this@AuthActivity, getString(R.string.auth_success), Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            } else {
                Toast.makeText(this@AuthActivity, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        googleAuthManager = GoogleAuthManager(this)
        
        // Check if user is already signed in
        val account = googleAuthManager.getLastSignedInAccount()
        if (account != null) {
            navigateToMainActivity()
            return
        }
        
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }
    
    private fun signIn() {
        binding.progressBar.visibility = View.VISIBLE
        val signInIntent = googleAuthManager.getSignInIntent()
        signInLauncher.launch(signInIntent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        // Add proper flags to handle the activity stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        // Add a small delay before finishing
        Thread.sleep(30000)// 300ms delay
    }
}
