package com.example.gawe17.Helper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

object ValidationHelper {

    fun isEmtyBox(view: ViewGroup, context: Context): Boolean{
        return anyEmty(view, context)
    }

    private fun anyEmty(view: View, context: Context): Boolean{
        return when(view){
            is TextInputEditText -> {
                if (view.text.isNullOrBlank()) {
                    view.error = "Fill This Column!!"
                    Toast.makeText(context, "Please Fill All of Column!!", Toast.LENGTH_SHORT).show()
                    return true
                }
                false
            }
            is ViewGroup -> {
                for (i in 0 until view.childCount){
                    if(anyEmty(view.getChildAt(i), context))
                        return true
                }
                false
            }
            else -> false
        }
    }

}