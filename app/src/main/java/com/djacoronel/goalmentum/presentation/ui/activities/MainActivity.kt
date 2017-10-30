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


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        setFragment(item.itemId)
        return@OnNavigationItemSelectedListener true
    }

    private fun setFragment(itemId: Int) {
        var fragment = Fragment()
        when (itemId) {
            R.id.active_goals -> fragment = ActiveGoalsFragment().newInstance()
            R.id.analyze_goals -> fragment = AnalyzeGoalsFragment.newInstance("", "")
            R.id.achieved_goals -> fragment = AchievedGoalsFragment().newInstance()
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.setOnNavigationItemReselectedListener { }

        setFragment(R.id.active_goals)
    }

    override fun onResume() {
        super.onResume()
        setFragment(navigation.selectedItemId)
    }
}
