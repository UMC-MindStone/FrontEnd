package com.example.mindstone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.databinding.ActivitySignupCodeBinding

class SignupCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}