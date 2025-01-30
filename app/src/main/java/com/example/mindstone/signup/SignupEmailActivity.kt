package com.example.mindstone.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.mindstone.R
import com.example.mindstone.databinding.ActivitySignupEmailBinding


class SignupEmailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailBinding
    private lateinit var signupViewModel : SignupViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupViewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        //초기 설정
        initClicker()

        binding.signupEmailTextTie.addTextChangedListener{ text ->
            validateEmail(text.toString())
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
                signupViewModel.email.value = input
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