package com.example.mindstone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.mindstone.databinding.ActivityMainBinding.inflate
import com.example.mindstone.databinding.ActivitySignupEmailBinding


class SignupEmailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupEmailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignupEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupEmailTextTie.addTextChangedListener{ text ->
            validateEmail(text.toString())
        }

    }
    private fun validateEmail(input:String){
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com"

        if(input.isEmpty()){
            binding.signupEmailTil.apply{
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@SignupEmailActivity,R.drawable.ic_emailerror)
                boxStrokeColor = getColor(R.color.error)
            }
        } else if(!input.matches(emailPattern.toRegex())){
            binding.signupEmailTil.apply{
                helperText = "example@example.com"
                errorIconDrawable = ContextCompat.getDrawable(this@SignupEmailActivity,R.drawable.ic_emailerror)
                boxStrokeColor = getColor(R.color.error)

            }
        } else {
            binding.signupEmailTil.apply{
                helperText = null
                errorIconDrawable = null
                boxStrokeColor = getColor(R.color.black)
            }
        }
    }
}