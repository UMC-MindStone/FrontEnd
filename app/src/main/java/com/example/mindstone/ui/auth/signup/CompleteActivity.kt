package com.example.mindstone.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.R
import com.example.mindstone.databinding.ActivityCompleteBinding
import com.example.mindstone.databinding.ActivityServiceBinding
import com.example.mindstone.ui.auth.login.LoginActivity

class CompleteActivity : AppCompatActivity() {
    lateinit var binding : ActivityCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.completeLoginIv.setOnClickListener{
            val intent = Intent(this@CompleteActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}