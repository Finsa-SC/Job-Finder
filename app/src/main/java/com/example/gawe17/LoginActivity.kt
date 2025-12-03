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
import com.example.gawe17.Data.UserSession
import com.example.gawe17.Helper.ApiHelper
import com.example.gawe17.Helper.ValidationHelper
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var lblToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        txtEmail = findViewById<TextInputEditText>(R.id.txtEmail)
        txtPassword = findViewById<TextInputEditText>(R.id.txtPassword)
        btnLogin = findViewById<Button>(R.id.btnLogin)
        lblToRegister = findViewById<TextView>(R.id.lblToRegister)

        lblToRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }

        btnLogin.setOnClickListener {
            val root = findViewById<ViewGroup>(R.id.main)
            if(ValidationHelper.isEmtyBox(root, this)) return@setOnClickListener

            val jsonData = JSONObject().apply {
                put("email", txtEmail.text.toString().trim())
                put("password", txtPassword.text.toString().trim())
            }
            Thread{
                val (responseCode, responseText) = ApiHelper.post("auth", jsonData)
                runOnUiThread {
                    if(responseCode==200&&responseText!=null){
                        val json = JSONObject(responseText)
                        UserSession.userId = json.getInt("id")
                        UserSession.profilePicture = json.getString("profilePicture")
                        UserSession.fullName = json.getString("fullname")
                        UserSession.email = json.getString("email")
                        UserSession.phoneNumber = json.getString("phoneNumber")
                        UserSession.role = json.getString("role")
                        startActivity(Intent(this, MainActivity::class.java))
                        this.finish()
                    }
                    else if(responseText!=null){
                        val json = JSONObject(responseText)
                        Toast.makeText(this, json.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Server Not Responding!!", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }
}