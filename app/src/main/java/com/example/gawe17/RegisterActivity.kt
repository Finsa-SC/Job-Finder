package com.example.gawe17

import android.content.Intent
import android.os.Bundle
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

        txtFullname = findViewById<TextInputEditText>(R.id.txtFullname)
        txtEmail = findViewById<TextInputEditText>(R.id.txtEmail)
        txtPhone = findViewById<TextInputEditText>(R.id.txtPhone)
        txtPassword = findViewById<TextInputEditText>(R.id.txtPassword)
        txtCPassword = findViewById<TextInputEditText>(R.id.txtCPassword)
        btnRegister = findViewById<Button>(R.id.btnRegister)
        lblToLogin = findViewById<TextView>(R.id.lblToLogin)

        lblToLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }

        btnRegister.setOnClickListener {
        }
    }
}