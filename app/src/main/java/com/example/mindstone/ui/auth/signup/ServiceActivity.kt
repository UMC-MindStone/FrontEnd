package com.example.mindstone.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mindstone.R
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.databinding.ActivityServiceBinding
import kotlinx.coroutines.launch

class ServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceBinding
    private lateinit var signupViewModel : SignupViewModel
    private val retrofitService = RetrofitClient.create(SignupService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]

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

    binding.serviceNextIv.setOnClickListener {
        if(binding.serviceChecked1Iv.visibility == View.VISIBLE){
            lifecycleScope.launch {
                try {
                    val response = retrofitService.signup(
                        signupRequest(
                            email = signupViewModel.email.value.toString(),
                            password = signupViewModel.password.value.toString(),
                            nickname = "Ean",
                            mbti = "intj",
                            birthday = "2001-01-01",
                            job = "student",
                            shareScope = true,
                            marketingAgree = true,
                            role = "user"
                        )
                    )
                    // 성공 시 화면 전환 기능까지 붙여주기...?
                    if (response.isSuccess){
                        Log.d("isSuccess Signup", "yes")
                    } else{
                        Log.d("isSuccess Signup", "no")
                    }
                    val intent = Intent(this@ServiceActivity, CompleteActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@ServiceActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
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
