package com.example.gawe17.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gawe17.R
import com.example.gawe17.core.util.ValidationHelper
import com.example.gawe17.core.network.ApiHelper
import com.example.gawe17.databinding.ActivityRegisterBinding
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.lblToLogin.setOnClickListener { startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        ) }

        binding.btnRegister.setOnClickListener {
            if (validation()) return@setOnClickListener

            val jsonData = JSONObject().apply {
                put("fullname", binding.txtFullname.text.toString().trim())
                put("email", binding.txtEmail.text.toString().trim())
                put("phoneNumber", binding.txtPhone.text.toString().trim())
                put("password", binding.txtPassword.text.toString().trim())
                put("confirmPassword", binding.txtCPassword.text.toString().trim())
            }
            Thread{
                val (responseCode ,responseText) = ApiHelper.post("register", jsonData)
                runOnUiThread {
                    val jsonResponse = if(responseText!=null) JSONObject(responseText) else null

                    if (responseCode == 200 && responseText!=null){
                        Toast.makeText(this, "Register Successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        this.finish()
                    }
                    else if(jsonResponse!=null){
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
        val root = findViewById<ViewGroup>(R.id.main)
        if (ValidationHelper.isNull(root, this)) return true

//        email format
        val emailText = binding.txtEmail.text.toString()
        if(!emailText.toString().endsWith("@gmail.com")){
            Toast.makeText(this, "Email not Valid!!", Toast.LENGTH_SHORT).show()
            return true
        }

//        phone number
        var phone = binding.txtPhone.text.toString()
        if(phone.contains("+62")) phone = phone.replace("+62", "0")
        if(phone.contains(" ")) phone = phone.replace(" ", "")
        if(!phone.startsWith("08")){
            Toast.makeText(this, "Phone Number not Valid!!", Toast.LENGTH_SHORT).show()
            return true
        }
        if(phone.length !in 10..12){
            Toast.makeText(this, "Phone Number not Valid!!", Toast.LENGTH_SHORT).show()
            return true
        }

//        password weakness
        val password = binding.txtPassword.text.toString()
        if(password.length<8){
            Toast.makeText(this, "Please try more long password!!", Toast.LENGTH_SHORT).show()
            return true
        }
        else if(!password.any{it.isUpperCase()}){
            Toast.makeText(this, "Please try atleast one uppercase char!!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}