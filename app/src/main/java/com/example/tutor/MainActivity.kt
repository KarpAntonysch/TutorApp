package com.example.tutor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tutor.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            // использую NavigationUI with Bottom navigation
        val btmNavigationView=binding.bottomMenu
            //инициализация хоста, позволяющая искать нужный нам фрагмент
        val navHostFragment =
           supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            // инициализируем navController
        val navController=navHostFragment.navController
            // задаем конфигурацию меню
        val appBarConfiguration= AppBarConfiguration(setOf(
            //R.id.studentJournalFragment,
            R.id.mainFragment,
            R.id.statisticFragment,
            R.id.jornalPagerFragment
        ))
        // связываем конфигурацию меню   с navController
        setupActionBarWithNavController(navController,appBarConfiguration)
        // связываем bottom navigation view  с navController
        btmNavigationView.setupWithNavController(navController)
    }

}