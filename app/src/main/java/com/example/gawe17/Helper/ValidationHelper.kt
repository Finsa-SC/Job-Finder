package com.example.gawe17.Helper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

object ValidationHelper {
    fun isNull(view: ViewGroup, context: Context): Boolean{
        return checkNullValue(view, context)
    }

    fun checkString(text: TextInputEditText, containt: String): Boolean{
        if (!text.text.toString().contains(containt)){
            return true
        }
        return false
    }

    private fun checkNullValue(view: View, context: Context): Boolean{
        return when(view){
            is TextInputEditText ->{
                if(view.text.isNullOrBlank()){
                    Toast.makeText(context, "Please Fill All of Input!!", Toast.LENGTH_SHORT).show()
                    return true
                }
                false
            }
            is ViewGroup -> {
                for(i in 0 until view.childCount){
                    if(checkNullValue(view.getChildAt(i), context)){
                        return true
                    }
                }
                false
            }
            else -> false
        }
    }
}
