package com.example.mindstone

import android.os.Bundle
import android.view.View
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

    override fun onStart() {
        super.onStart()

        binding.serviceUncheck1Iv.setOnClickListener {
            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE
        }

        binding.serviceChecked1Iv.setOnClickListener {
            binding.serviceUncheck1Iv.visibility = View.VISIBLE
            binding.serviceChecked1Iv.visibility = View.GONE
        }

        binding.serviceUncheck2Iv.setOnClickListener {
            binding.serviceUncheck2Iv.visibility = View.GONE
            binding.serviceChecked2Iv.visibility = View.VISIBLE
        }

        binding.serviceChecked2Iv.setOnClickListener {
            binding.serviceUncheck2Iv.visibility = View.VISIBLE
            binding.serviceChecked2Iv.visibility = View.GONE
        }

        binding.serviceUncheck3Iv.setOnClickListener {
            binding.serviceUncheck3Iv.visibility = View.GONE
            binding.serviceChecked3Iv.visibility = View.VISIBLE
        }

        binding.serviceChecked3Iv.setOnClickListener {
            binding.serviceUncheck3Iv.visibility = View.VISIBLE
            binding.serviceChecked3Iv.visibility = View.GONE
        }

        binding.serviceUncheck4Iv.setOnClickListener {
            binding.serviceUncheck4Iv.visibility = View.GONE
            binding.serviceChecked4Iv.visibility = View.VISIBLE
        }

        binding.serviceChecked4Iv.setOnClickListener {
            binding.serviceUncheck4Iv.visibility = View.VISIBLE
            binding.serviceChecked4Iv.visibility = View.GONE
        }
    }

    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.service_container_fl,fragment,)
            .addToBackStack(null)
            .commit()
    }
}
