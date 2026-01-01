package com.example.gawe17.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.MainActivity
import com.example.gawe17.model.UserSession
import com.example.gawe17.R
import com.example.gawe17.core.session.SessionManager
import com.example.gawe17.core.util.ValidationHelper
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.databinding.ActivityLoginBinding
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

        binding.lblToRegister.setOnClickListener { startActivity(
            Intent(
                this,
                RegisterActivity::class.java
            )
        ) }

        binding.btnLogin.setOnClickListener {
            UserLogin()

            if(ValidationHelper.isNull(binding.main, this)) return@setOnClickListener

        }
    }

    private fun UserLogin(){
        val jsonData = JSONObject().apply {
            put("email", "agus@gmail.com")
            put("password", "Agus080200")
//            put("email", binding.txtEmail.text.toString())
//            put("password", binding.txtPassword.text.toString())
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