package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager

/**
 * Created by djacoronel on 10/28/17.
 */
class CustomEditText(internal var context: Context, attrs: AttributeSet) : android.support.v7.widget.AppCompatEditText(context, attrs) {
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            val mgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(this.windowToken, 0)
            // TODO: Hide your view as you do it in your activity
            (context as AddWorkActivity).finish()
        }
        return false
    }
}