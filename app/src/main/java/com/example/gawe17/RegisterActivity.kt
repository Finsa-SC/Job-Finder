package com.example.gawe17

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.Helper.ApiHelper
import com.example.gawe17.Helper.ValidationHelper
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var txtFullname: TextInputEditText
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPhone: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var txtCPassword: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var lblToLogin: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtFullname = findViewById(R.id.txtFullname)
        txtEmail = findViewById(R.id.txtEmail)
        txtPhone = findViewById(R.id.txtPhone)
        txtPassword = findViewById(R.id.txtPassword)
        txtCPassword = findViewById(R.id.txtCPassword)
        btnRegister = findViewById(R.id.btnRegister)
        lblToLogin = findViewById(R.id.lblToLogin)

        lblToLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }

        btnRegister.setOnClickListener {
            validation()

            val jsonData = JSONObject().apply {
                put("fullname", txtFullname.text.toString().trim())
                put("email", txtEmail.text.toString().trim())
                put("phoneNumber", txtPhone.text.toString().trim())
                put("password", txtPassword.text.toString().trim())
                put("confirmPassword", txtCPassword.text.toString().trim())
            }
            Thread{
                val (responseCode ,responseText) = ApiHelper.post("register", jsonData)
                runOnUiThread {
                    val jsonResponse = JSONObject(responseText)

                    if (responseCode == 200 && responseText!=null){
                        Toast.makeText(this, "Register Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        this.finish()
                    }
                    else if(responseText!=null){
                        Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Server is not responding!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    private fun validation(): Boolean{
//        Emty Blok
        var root = findViewById<ViewGroup>(R.id.main)
        if (ValidationHelper.isNull(root, this)) return true

//        email format
        val emailText = txtEmail.text
        if(!emailText.toString().endsWith("@gmail.com")){
            Toast.makeText(this, "Email not Valid!!", Toast.LENGTH_SHORT).show()
            return true
        }

//        password weakness
        val password = txtPassword.text.toString()
        if(password.length<8){
            Toast.makeText(this, "Please try more long password!!", Toast.LENGTH_SHORT).show()
            return true
        }
        else if(!password.any{it.isUpperCase()}){
            Toast.makeText(this, "Please try matleast one uppercase char!!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}