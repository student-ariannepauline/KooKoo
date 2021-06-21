package com.sunnyside.kookoo.student.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityStudentBinding
import com.sunnyside.kookoo.verification.ui.activity.VerificationActivity

private lateinit var binding: ActivityStudentBinding
private lateinit var navController: NavController


class StudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.student_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment2,
                R.id.viewProfileFragment3,
                R.id.settingsFragment3
            ), binding.studentDrawer
        )

        binding.myToolbar.setupWithNavController(navController, appBarConfiguration)

        setupDrawerContent(binding.navigationView)


    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboardFragment -> navController.navigate(R.id.dashboardFragment2)

                R.id.profileFragment -> navController.navigate(R.id.viewProfileFragment3)

                R.id.settingsFragment -> navController.navigate(R.id.settingsFragment3)

                R.id.logoutButton -> logout()
            }
            binding.studentDrawer.closeDrawers()
            true
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        navController.navigate(R.id.action_dashboardFragment2_to_verificationActivity4)
    }


}