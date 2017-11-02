package com.djacoronel.goalmentum.util

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import com.djacoronel.goalmentum.presentation.ui.activities.MilestoneActivity

/**
 * Created by djacoronel on 10/28/17.
 */
class CustomEditText(internal var context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatEditText(context, attrs) {
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            // TODO: Hide your view as you do it in your activity
            (context as MilestoneActivity).dismissInput()
        }
        return true
    }
}