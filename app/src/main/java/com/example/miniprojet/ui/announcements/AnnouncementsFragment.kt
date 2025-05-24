package com.example.miniprojet.ui.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.miniprojet.R
import com.example.miniprojet.data.model.AnnouncementType
import com.example.miniprojet.databinding.FragmentAnnouncementsBinding
import com.google.android.material.tabs.TabLayoutMediator

class AnnouncementsFragment : Fragment() {

    private var _binding: FragmentAnnouncementsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up ViewPager with tabs
        setupViewPager()
    }
    
    private fun setupViewPager() {
        // Create adapter for the ViewPager
        val pagerAdapter = AnnouncementsPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        
        // Connect TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_announcements_urgent)
                1 -> getString(R.string.tab_announcements_events)
                2 -> getString(R.string.tab_announcements_misc)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // ViewPager adapter for announcement tabs
    private inner class AnnouncementsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        
        override fun getItemCount(): Int = 3
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AnnouncementListFragment.newInstance(AnnouncementType.URGENT)
                1 -> AnnouncementListFragment.newInstance(AnnouncementType.EVENT)
                2 -> AnnouncementListFragment.newInstance(AnnouncementType.MISC)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}
