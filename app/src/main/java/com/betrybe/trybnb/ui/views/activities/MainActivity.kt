package com.betrybe.trybnb.ui.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.betrybe.trybnb.R
import com.betrybe.trybnb.databinding.ActivityMainBinding
import com.betrybe.trybnb.ui.views.fragments.CreateReservationFragment
import com.betrybe.trybnb.ui.views.fragments.ProfileFragment
import com.betrybe.trybnb.ui.views.fragments.ReservationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private val reservationFragment = ReservationFragment()
    private val createReservationFragment = CreateReservationFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigation = binding.navigationBottomView

        changeFragmentMenu(profileFragment)
        bottomNavigation.selectedItemId = R.id.profile_menu_tem

        bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.reservation_menu_item -> {
                    changeFragmentMenu(reservationFragment)
                }
                R.id.create_reservation_menu_item -> {
                    changeFragmentMenu(createReservationFragment)
                }
                R.id.profile_menu_tem -> {
                    changeFragmentMenu(profileFragment)
                }
            }
            true
        }
    }

    private fun changeFragmentMenu(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}
