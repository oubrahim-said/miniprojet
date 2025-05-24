package com.example.miniprojet.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.miniprojet.auth.GoogleAuthManager
import com.example.miniprojet.databinding.FragmentSettingsBinding
import com.example.miniprojet.ui.auth.AuthActivity
// BuildConfig import removed

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleAuthManager: GoogleAuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize auth manager
        googleAuthManager = GoogleAuthManager(requireContext())

        // Set up app version with hardcoded values
        binding.tvAppVersion.text = "Version 1.0 (1)"

        // Set up theme selection
        setupThemeSelection()

        // Set up privacy policy and terms of service
        setupLinks()

        // Set up sign out button
        binding.btnSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun setupThemeSelection() {
        // Get current theme
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()

        // Set the appropriate radio button
        when (currentNightMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding.rbThemeSystem.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> binding.rbThemeLight.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding.rbThemeDark.isChecked = true
        }

        // Set up radio button listeners
        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                binding.rbThemeSystem.id -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                binding.rbThemeLight.id -> AppCompatDelegate.MODE_NIGHT_NO
                binding.rbThemeDark.id -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }

            // Apply the theme
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun setupLinks() {
        // Privacy policy
        binding.tvPrivacyPolicy.setOnClickListener {
            openUrl("https://example.com/privacy-policy")
        }

        // Terms of service
        binding.tvTermsOfService.setOnClickListener {
            openUrl("https://example.com/terms-of-service")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun signOut() {
        googleAuthManager.signOut()

        // Navigate to auth activity
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
