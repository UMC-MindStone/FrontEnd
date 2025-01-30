package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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