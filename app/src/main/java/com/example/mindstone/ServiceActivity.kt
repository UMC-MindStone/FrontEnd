package com.example.mindstone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mindstone.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.serviceGo1Iv.setOnClickListener {
            openFragment(TermFragment())
        }

        binding.serviceGo2Iv.setOnClickListener {
            openFragment(PrivacyFragment())
        }
    }

    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.service_container_fl,fragment,)
            .addToBackStack(null)
            .commit()
    }
}
