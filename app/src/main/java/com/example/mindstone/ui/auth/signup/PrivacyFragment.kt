package com.example.mindstone.ui.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mindstone.databinding.FragmentPrivacyBinding

class PrivacyFragment : Fragment() {

    lateinit var binding : FragmentPrivacyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        val text = "<마인드 스톤>은 정보주체의 자유와 권리 보호를 위해 「개인정보 보호법」 및 관계 법령이 정한 바를 준수하여, 적법하게 개인정보를 처리하고 안전하게 관리하고 있습니다. 이에 「개인정보 보호법」 제30조에 따라 정보주체에게 개인정보 처리에 관한 절차 및 기준을 안내하고, 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립・공개합니다.\n" +
                "\n" +
                "<b>□ 개인정보의 처리 목적</b>\n" +
                "① <마인드 스톤>은 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경되는 경우에는 「개인정보 보호법」 제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.\n" +
                " 1. 회원 가입 및 관리\n" +
                " 회원 가입의사 확인, 본인 식별・인증, 만14세 이상 여부 확인, 회원자격 유지・관리, 서비스 부정이용 방지, 각종 고지・통지 등을 목적으로 개인정보를 처리합니다.\n" +
                " 2. 민원사무 처리\n" +
                " 민원인의 신원 확인, 민원사항 확인, 사실조사를 위한 연락・통지, 처리결과 통보 등의 목적으로 개인정보를 처리합니다.\n" +
                " 3. 마케팅 및 광고에의 활용\n" +
                "　맞춤형 광고 제공 등\n" +
                " 4. 서비스 제공\n" +
                " 요금 결제・정산, 본인인증, 연령인증, 기본/유료 서비스 제공\n" +
                " 00. <기타 공공기관의 개인정보 처리업무>\n" +
                " <개인정보 처리업무에 따른 처리목적>으로 개인정보를 처리합니다.\n" +
                "\n" +
                "<b>□ 개인정보 처리 항목</b>\n" +
                "① <마인드 스톤>은 서비스 이용자에 대해 다음의 개인정보항목을 수집하여 처리하고 있습니다.\n" +
                " 1. 회원 가입 시 기본수집사항\n" +
                "  로그인 SNS 식별자, 프로필 이름, 생년월일\n" +
                " 2. 서비스 이용과정에서 수집\n" +
                "  닉네임, 프로필 이미지, 일기 본문 이미지, 메시지 텍스트, 일기 본문 텍스\n"

        binding.privacyClauseTv.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.privacyCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.privacyAgreeIv.setOnClickListener {
            (activity as? ServiceActivity)?.setPersonalInformationUseOn()
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 맞춤
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}