package com.ankitamoundekar.todoapp.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager

fun hideKeyBoard(activity: Activity)
{
    val inputMethodManager :InputMethodManager=activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusView : View? =activity.currentFocus
    currentFocusView?.let {
        inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
    }
}