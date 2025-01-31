package com.example.mindstone.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.MyApplication
import com.example.mindstone.R
import com.example.mindstone.databinding.ActivitySignupEmailBinding


class SignupEmailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signupViewModel = (application as MyApplication).signupViewModel


        initClicker()

        binding.signupEmailTextTie.addTextChangedListener{ text ->
            validateEmail(text.toString())
        }

        // 뒤로 가기 버튼 클릭 -> LoginActivity로 이동
        binding.signupemailBackIv.setOnClickListener {
            finish() // 현재 액티비티 종료 (LoginActivity로 돌아감)
        }

    }
    private fun validateEmail(input:String){
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com"

        if(input.isEmpty()){
            binding.signupEmailTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@SignupEmailActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)
            }
        } else if(!input.matches(emailPattern.toRegex())){
            binding.signupEmailTil.apply{
                hintTextColor= getColorStateList(R.color.black)
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@SignupEmailActivity,
                    R.drawable.ic_emailerror
                )
                boxStrokeColor = getColor(R.color.error)

            }
        }
//        else if( // 여기에 이미 등록된 이메일 일 때 ){
//        }
        else {
            binding.signupEmailTil.apply{
                helperText = null
                errorIconDrawable = null
                boxStrokeColor = getColor(R.color.black)

                // 뷰모델에 이메일 업데이트
                signupViewModel.updateEmail(input)

                Log.d("email viewmodel", "${signupViewModel.email.value}")

            }
        }
    }

    private fun initClicker(){
        binding.signupEmailNextBtn.setOnClickListener{
            val intent = Intent(this, SignupCodeActivity::class.java)

            startActivity(intent)
        }

    }
}