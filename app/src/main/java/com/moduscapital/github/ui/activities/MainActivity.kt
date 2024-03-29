package com.moduscapital.github.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.moduscapital.github.R
import com.moduscapital.github.extensions.loadSavedUser
import com.moduscapital.github.ui.fragments.userdetails.UserDetailsFragmentArgs
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, null)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)


        loadSavedUserIfExist()


    }

    private fun loadSavedUserIfExist() {
        val user = loadSavedUser()
        if (user != "") {
            val bundle = UserDetailsFragmentArgs.Builder().setUserName(user).build()
            navController.navigate(R.id.userDetailsFragment, bundle.toBundle())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
