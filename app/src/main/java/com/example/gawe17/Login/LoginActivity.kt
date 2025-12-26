package com.example.gawe17.Login

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
import com.example.gawe17.Helper.SessionManager
import com.example.gawe17.Helper.ValidationHelper
import com.example.gawe17.Main.MainActivity
import com.example.gawe17.Models.UserSession
import com.example.gawe17.R
import com.example.gawe17.Register.RegisterActivity
import com.example.gawe17.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lblToRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }

        binding.btnLogin.setOnClickListener {
            val main = findViewById<ViewGroup>(R.id.main)
            if(ValidationHelper.isNull(main, this)) return@setOnClickListener

            UserLogin()
        }
    }

    private fun UserLogin(){
        val jsonData = JSONObject().apply {
            put("email", binding.txtEmail.text.toString())
            put("password", binding.txtPassword.text.toString())
        }

        Thread{
            val (responseCode, responseText) = ApiHelper.post("auth", jsonData)

            runOnUiThread {
                val jsonResponse = if(responseText!=null) JSONObject(responseText) else null
                if(responseCode == 200 && jsonResponse != null){
                    val json = jsonResponse.getJSONObject("data")
                    val user = UserSession(
                        userId = json.getInt("id"),
                        profilePicture = json.optString("profilePicture", null),
                        fullName = json.getString("fullname"),
                        email = json.getString("email"),
                        phoneNumber = json.getString("phoneNumber"),
                        role = json.getString("role"),
                    )
                    Toast.makeText(this, "Success Login as "+ user.fullName, Toast.LENGTH_SHORT).show()
                    SessionManager.user = user
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }
                else if (responseText != null){
                    Toast.makeText(this, jsonResponse?.getString("message"), Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Server not responding!!", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}