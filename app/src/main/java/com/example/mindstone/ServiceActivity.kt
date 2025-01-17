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

        //전체 동의 버튼 ON
    binding.serviceUncheck1Iv.setOnClickListener {
        binding.serviceUncheck1Iv.visibility = View.GONE
        binding.serviceChecked1Iv.visibility = View.VISIBLE

        if(binding.serviceUncheck2Iv.visibility == View.VISIBLE){
            binding.serviceUncheck2Iv.visibility = View.GONE
            binding.serviceChecked2Iv.visibility = View.VISIBLE
        }

        if(binding.serviceUncheck3Iv.visibility == View.VISIBLE){
            binding.serviceUncheck3Iv.visibility = View.GONE
            binding.serviceChecked3Iv.visibility = View.VISIBLE
        }

        if(binding.serviceUncheck4Iv.visibility == View.VISIBLE){
            binding.serviceUncheck4Iv.visibility = View.GONE
            binding.serviceChecked4Iv.visibility = View.VISIBLE
        }
    }

    //전체 동의 버튼 OFF
    binding.serviceChecked1Iv.setOnClickListener {
        binding.serviceUncheck1Iv.visibility = View.VISIBLE
        binding.serviceChecked1Iv.visibility = View.GONE

        binding.serviceUncheck2Iv.visibility = View.VISIBLE
        binding.serviceChecked2Iv.visibility = View.GONE

        binding.serviceUncheck3Iv.visibility = View.VISIBLE
        binding.serviceChecked3Iv.visibility = View.GONE

        binding.serviceUncheck4Iv.visibility = View.VISIBLE
        binding.serviceChecked4Iv.visibility = View.GONE
    }


    //(필수) 만 14세 이상입니다. ON
    binding.serviceUncheck2Iv.setOnClickListener {
        binding.serviceUncheck2Iv.visibility = View.GONE
        binding.serviceChecked2Iv.visibility = View.VISIBLE

        //버튼이 모두 ON 상태가 되면 전체 동의 버튼 ON
        if(binding.serviceUncheck2Iv.visibility == View.GONE &&
            binding.serviceUncheck3Iv.visibility == View.GONE &&
            binding.serviceUncheck4Iv.visibility == View.GONE){

            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE

        }
    }

    //(필수) 만 14세 이상입니다. OFF
    binding.serviceChecked2Iv.setOnClickListener {
        binding.serviceUncheck2Iv.visibility = View.VISIBLE
        binding.serviceChecked2Iv.visibility = View.GONE

        //버튼이 하나라도 OFF가 되면 전체 동의 버튼 OFF
        if(binding.serviceChecked1Iv.visibility == View.VISIBLE){
            binding.serviceUncheck1Iv.visibility = View.VISIBLE
            binding.serviceChecked1Iv.visibility = View.GONE
        }
    }


    //(필수) 서비스 이용 약관 ON
    binding.serviceUncheck3Iv.setOnClickListener {
        binding.serviceUncheck3Iv.visibility = View.GONE
        binding.serviceChecked3Iv.visibility = View.VISIBLE

        //버튼이 모두 ON 상태가 되면 전체 동의 버튼 ON
        if(binding.serviceUncheck2Iv.visibility == View.GONE &&
            binding.serviceUncheck3Iv.visibility == View.GONE &&
            binding.serviceUncheck4Iv.visibility == View.GONE){

            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE

        }
    }

    //(필수) 서비스 이용 약관 OFF
    binding.serviceChecked3Iv.setOnClickListener {
        binding.serviceUncheck3Iv.visibility = View.VISIBLE
        binding.serviceChecked3Iv.visibility = View.GONE

        //버튼이 하나라도 OFF가 되면 전체 동의 버튼 OFF
        if(binding.serviceChecked1Iv.visibility == View.VISIBLE){
            binding.serviceUncheck1Iv.visibility = View.VISIBLE
            binding.serviceChecked1Iv.visibility = View.GONE
        }
    }


    //(필수) 개인정보 수집 및 이용 동의 ON
    binding.serviceUncheck4Iv.setOnClickListener {
        binding.serviceUncheck4Iv.visibility = View.GONE
        binding.serviceChecked4Iv.visibility = View.VISIBLE

        //버튼이 모두 ON 상태가 되면 전체 동의 버튼 ON
        if(binding.serviceUncheck2Iv.visibility == View.GONE &&
            binding.serviceUncheck3Iv.visibility == View.GONE &&
            binding.serviceUncheck4Iv.visibility == View.GONE){

            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE
        }
    }

    //(필수) 개인정보 수집 및 이용 동의 OFF
    binding.serviceChecked4Iv.setOnClickListener {
        binding.serviceUncheck4Iv.visibility = View.VISIBLE
        binding.serviceChecked4Iv.visibility = View.GONE

        //버튼이 하나라도 OFF가 되면 전체 동의 버튼 OFF
        if(binding.serviceChecked1Iv.visibility == View.VISIBLE){
            binding.serviceUncheck1Iv.visibility = View.VISIBLE
            binding.serviceChecked1Iv.visibility = View.GONE
        }
    }
}

    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.service_container_fl,fragment,)
            .addToBackStack(null)
            .commit()
    }

    fun setServiceUseOn() {
        binding.serviceUncheck3Iv.visibility = View.GONE
        binding.serviceChecked3Iv.visibility = View.VISIBLE

        //버튼이 모두 ON 상태가 되면 전체 동의 버튼 ON
        if (binding.serviceUncheck2Iv.visibility == View.GONE &&
            binding.serviceUncheck3Iv.visibility == View.GONE &&
            binding.serviceUncheck4Iv.visibility == View.GONE
        ) {

            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE
        }
    }

    fun setPersonalInformationUseOn() {
        binding.serviceUncheck4Iv.visibility = View.GONE
        binding.serviceChecked4Iv.visibility = View.VISIBLE

        //버튼이 모두 ON 상태가 되면 전체 동의 버튼 ON
        if (binding.serviceUncheck2Iv.visibility == View.GONE &&
            binding.serviceUncheck3Iv.visibility == View.GONE &&
            binding.serviceUncheck4Iv.visibility == View.GONE
        ) {

            binding.serviceUncheck1Iv.visibility = View.GONE
            binding.serviceChecked1Iv.visibility = View.VISIBLE
        }
    }
}
