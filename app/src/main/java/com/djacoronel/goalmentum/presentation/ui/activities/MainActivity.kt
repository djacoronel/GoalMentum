package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.presentation.ui.fragments.AchievedGoalsFragment
import com.djacoronel.goalmentum.presentation.ui.fragments.ActiveGoalsFragment
import com.djacoronel.goalmentum.presentation.ui.fragments.AnalyzeGoalsFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var currentFragment = Fragment()
        when (item.itemId) {
            R.id.active_goals -> currentFragment = ActiveGoalsFragment().newInstance()
            R.id.analyze_goals -> currentFragment = AnalyzeGoalsFragment.newInstance("","")
            R.id.achieved_goals -> currentFragment = AchievedGoalsFragment().newInstance()
        }
        setFragment(currentFragment)
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val currentFragment = ActiveGoalsFragment().newInstance()
        setFragment(currentFragment)
    }

    private fun setFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }
}
