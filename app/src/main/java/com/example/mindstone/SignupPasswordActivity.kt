package com.example.mindstone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.databinding.ActivitySignupPasswordBinding

class SignupPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //초기 설정
        initClicker()



    }
    private fun validatePassword(input: String){
        if(input.isEmpty()){
            binding.signupPasswordTil.apply{
                boxStrokeColor = getColor(R.color.error)
                helperText = "영문, 숫자, 특수문자 포함 8자 이상."
            }
        } else if(containsCharacter(input) && containsNumber(input) && containsSpecialCharacter(input) && input.length >= 8){
            binding.signupPasswordTil.apply{
                boxStrokeColor = getColor(R.color.black)
                helperText = null
            }
        } else{
            binding.signupPasswordTil.apply{
                boxStrokeColor = getColor(R.color.error)
                helperText = "영문, 숫자, 특수문자 포함 8자 이상."
            }
        }
    }
    private fun containsNumber(input: String): Boolean {
        val numberRegex = ".*\\d.*".toRegex() // 숫자를 포함한 정규식
        return numberRegex.matches(input)
    }
    private fun containsSpecialCharacter(input: String): Boolean {
        val specialCharRegex = ".*[!@#\$%^&*()_+\\-=\\[\\]{};':/?].*".toRegex()
        return specialCharRegex.matches(input)
    }
    private fun containsCharacter(input:String):Boolean {
        val characterRegex = ".*[a-zA-Z].*".toRegex()
        return characterRegex.matches(input)
    }

    private fun initClicker(){
        binding.signupPasswordNextBtn.setOnClickListener{
            val input = binding.signupPasswordTextTie.text.toString()
            validatePassword(input)
        }

    }

}