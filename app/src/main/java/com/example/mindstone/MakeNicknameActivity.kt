package com.example.mindstone

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.databinding.ActivityMakeNicknameBinding

class MakeNicknameActivity : AppCompatActivity() {

    lateinit var binding:ActivityMakeNicknameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.makeNicknameBackIv.setOnClickListener {
            val intent = Intent(this, AfterLoginActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}