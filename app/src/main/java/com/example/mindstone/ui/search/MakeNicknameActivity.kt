package com.example.mindstone.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mindstone.data.local.PreferenceManager
import com.example.mindstone.databinding.ActivityMakeNicknameBinding
import com.example.mindstone.data.remote.RetrofitClient
import com.example.mindstone.data.remote.SurveyService
import com.example.mindstone.data.local.UserData
import com.example.mindstone.ui.auth.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeNicknameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMakeNicknameBinding
    private val apiService: SurveyService by lazy { RetrofitClient.surveyService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 뒤로 가기 버튼 클릭 시 AfterLoginActivity로 이동
        binding.makeNicknameBackIv.setOnClickListener {
            val intent = Intent(this, AfterLoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 🔹 닉네임 입력 후 다음 버튼 클릭 시 API 호출 후 Search1Activity로 이동
        binding.afterLoginNextIv.setOnClickListener {
            val nickname = binding.nicknameInput.text?.toString()?.trim() ?: ""

            if (nickname.isBlank()) {
                binding.makeNicknameNameTil.error = "닉네임을 입력하세요."
                return@setOnClickListener
            }

            binding.makeNicknameNameTil.error = null // 에러 메시지 초기화
//            sendNicknameToServer(nickname)

            // ✅ SharedPreferences에 닉네임 저장
            saveUserNickname(nickname)

            val userData = UserData(nickname = nickname)
            val intent = Intent(this, Search1Activity::class.java)
            intent.putExtra("userData", userData)
            startActivity(intent)
            finish()
        }
    }

    // ✅ SharedPreferences에 닉네임 저장
    private fun saveUserNickname(nickname: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_nickname", nickname)
        editor.apply()
    }


//    private fun sendNicknameToServer(nickname: String) {
//        val userData = UserData(nickname = nickname)
//        val token = PreferenceManager.getAccessToken() ?: ""
//
//        Log.d("API_AUTH", "when Nickname save request ->  AccessToken: $token") // ✅ 로그로 AccessToken 확인
//
//        apiService.sendNickname(userData) //"Bearer $token", userData
//            .enqueue(object : Callback<UserData> {
//                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//                    Log.d("API_RESPONSE", "Nickname Save Response Code: ${response.code()}")
//
//                    if (response.isSuccessful) {
//                        Log.d("API_SUCCESS", "Nickname Save Finish: ${response.body()?.nickname}")
//
//                        val intent = Intent(this@MakeNicknameActivity, Search1Activity::class.java)
//                        intent.putExtra("userData", userData)
//                        startActivity(intent)
//                        finish()
//                    } else if (response.code() == 302) { // ✅ 서버가 302 리디렉션을 반환하면 로그인 화면으로 이동
//                        Log.e("API_ERROR", "AccessToken is finished or not auth. Login Again.")
//
//                        // ✅ 기존 토큰 삭제
//                        PreferenceManager.clearAccessToken()
//
//                        // ✅ 로그인 화면으로 이동
//                        val intent = Intent(this@MakeNicknameActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        Log.e("API_ERROR", "닉네임 저장 실패: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<UserData>, t: Throwable) {
//                    Log.e("API_FAILURE", "Network Error: ${t.message}")
//                }
//            })
//    }
//private fun sendNicknameToServer(nickname: String) {
//    val userData = UserData(nickname = nickname)
//
//    apiService.sendNickname(userData)
//        .enqueue(object : Callback<UserData> {
//            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
//                Log.d("API_RESPONSE", "Nickname Save Response Code: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    Log.d("API_SUCCESS", "Nickname Save Finish: ${response.body()?.nickname}")
//
//                    val intent = Intent(this@MakeNicknameActivity, Search1Activity::class.java)
//                    intent.putExtra("userData", userData)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Log.e("API_ERROR", "닉네임 저장 실패: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<UserData>, t: Throwable) {
//                Log.e("API_FAILURE", "네트워크 오류: ${t.message}")
//            }
//        })
//}

}
