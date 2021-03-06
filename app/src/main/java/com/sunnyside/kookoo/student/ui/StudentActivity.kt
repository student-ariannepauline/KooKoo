package com.sunnyside.kookoo.student.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sunnyside.kookoo.R
import com.sunnyside.kookoo.databinding.ActivityStudentBinding
import com.sunnyside.kookoo.student.data.UserToken
import com.sunnyside.kookoo.student.ui.fragments.timeline.ClassFragment

private lateinit var binding: ActivityStudentBinding
private lateinit var navController: NavController
private lateinit var appBarConfiguration: AppBarConfiguration
private var profileId = R.id.viewProfileFragment3

class StudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.student_nav_host) as NavHostFragment
        navController = navHostFragment.navController


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dashboardFragment2,
                R.id.viewProfileFragment3,
                R.id.settingsFragment3
            ), binding.studentDrawer
        )

        setupNavigationMenu(navController)
        setSupportActionBar(binding.myToolbar)
        setupActionBar(navController,
            appBarConfiguration)

        binding.myToolbar.setupWithNavController(navController, appBarConfiguration)
        setupDrawerContent(binding.navigationView)

        checkForProfile()

    }


    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboardFragment -> navController.navigate(R.id.dashboardFragment2)

                R.id.profileFragment -> navController.navigate(profileId)

                R.id.settingsFragment -> navController.navigate(R.id.settingsFragment3)

                R.id.logoutButton -> logout()
            }
            binding.studentDrawer.closeDrawers()
            true
        }

    }

    private fun checkForProfile() {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val docRef = db.collection("user_profile").whereEqualTo("uid", user?.uid)

        docRef.get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    profileId = R.id.viewProfileFragment3
                } else {
                    navController.navigate(R.id.setupProfileFragment3)
                    profileId = R.id.setupProfileFragment3
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return navController.navigateUp()
    }

    private fun logout() {
        Firebase.auth.signOut()
        navController.navigate(R.id.verificationActivity4)
    }

    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = binding.navigationView
        sideNavView.setupWithNavController(navController)
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }





}

