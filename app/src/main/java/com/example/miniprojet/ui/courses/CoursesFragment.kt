package com.example.miniprojet.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.miniprojet.R
import com.example.miniprojet.databinding.FragmentCoursesBinding
import com.google.android.material.tabs.TabLayoutMediator

class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Set up ViewPager with tabs
        setupViewPager()
    }
    
    private fun setupViewPager() {
        // Create adapter for the ViewPager
        val pagerAdapter = CoursesPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        
        // Connect TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_courses_current)
                1 -> getString(R.string.tab_courses_completed)
                2 -> getString(R.string.tab_courses_all)
                else -> null
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    // ViewPager adapter for course tabs
    private inner class CoursesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        
        override fun getItemCount(): Int = 3
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CourseListFragment.newInstance(CourseListFragment.TYPE_CURRENT)
                1 -> CourseListFragment.newInstance(CourseListFragment.TYPE_COMPLETED)
                2 -> CourseListFragment.newInstance(CourseListFragment.TYPE_ALL)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}
