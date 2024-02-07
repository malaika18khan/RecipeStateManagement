package com.zetsol.recipestatemanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zetsol.recipestatemanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bottomNavigationView = binding.bottomNav

        bottomNavigationView.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.bottom_home -> {
                    replace_fragment(HomeFragment())
                    true
                }
                R.id.bottom_fav -> {
                    replace_fragment(FavoritesFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replace_fragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replace_fragment(HomeFragment())


    }

    private fun replace_fragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
