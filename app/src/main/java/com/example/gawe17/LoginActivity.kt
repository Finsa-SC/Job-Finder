package com.example.gawe17

import android.content.Intent
import android.os.Bundle
import android.os.UserManager
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.Helper.ApiHelper
import com.example.gawe17.Helper.SessionManager
import com.example.gawe17.Helper.ValidationHelper
import com.example.gawe17.Model.UserSession
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
            val main = findViewById<ViewGroup>(R.id.main)
            if(ValidationHelper.isNull(main, this)) return@setOnClickListener

            if(txtEmail.text.toString() == "admin" && txtPassword.text.toString() == "admin"){
                startActivity(Intent(this, MainActivity::class.java))
            }


        }
    }

    private fun UserLogin(){
        val jsonData = JSONObject().apply {
            put("email", txtEmail.text.toString())
            put("password", txtPassword.text.toString())
        }

        Thread{
            val (responseCode, responseText) = ApiHelper.post("auth", jsonData)

            runOnUiThread {
                val jsonResponse = JSONObject(responseText)
                if(responseCode == 200 && responseText != null){
                    val user = UserSession(
                        userId = jsonResponse.getInt("id"),
                        profilePicture = jsonResponse.optString("profilePicture", null),
                        fullName = jsonResponse.getString("fullName"),
                        email = jsonResponse.getString("email"),
                        phoneNumber = jsonResponse.getString("phoneNumber"),
                        role = jsonResponse.getString("role"),
                    )
                    Toast.makeText(this, "Success Login as "+ user.fullName, Toast.LENGTH_SHORT).show()
                    SessionManager.user = user
                }
                else if (responseText != null){
                    Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Server not responding!!", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()

    }
}