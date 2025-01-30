package com.example.mindstone.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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