package com.example.gawe17.core.util

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.example.gawe17.R

object UIHelper {
    public fun showDialog(context: Context, message: String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_notification)
        dialog.show()
        dialog.findViewById<TextView>(R.id.dialogNotification_txtMessage).text = message
        dialog.findViewById<Button>(R.id.dialogNotification_btnUnderstand).setOnClickListener {
            dialog.dismiss()
        }
    }
}