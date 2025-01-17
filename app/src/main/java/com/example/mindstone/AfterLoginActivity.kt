package com.example.mindstone

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.databinding.ActivityAfterLoginBinding

class AfterLoginActivity : AppCompatActivity() {

    lateinit var binding:ActivityAfterLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.afterLoginNextIv.setOnClickListener {
            val intent = Intent(this, MakeNicknameActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}