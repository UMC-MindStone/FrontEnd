package com.example.mindstone.ui.auth.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.mindstone.databinding.FragmentTermBinding

class TermFragment : Fragment() {

    lateinit var binding : FragmentTermBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTermBinding.inflate(inflater, container, false)

        val text = "<b>제1조 (목적)</b> 이 약관은 마인드 스톤(이하 “회사”라 합니다)이 모바일 기기를 통해 제공하는 게임 서비스 및 이에 부수하는 네트워크, 웹사이트, 기타 서비스(이하 “서비스”라 합니다)의 이용에 대한 회사와 서비스 이용자의 권리ㆍ의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다.\n" +
                "   \n" +
                "<b>제2조 (용어의 정의)</b> ① 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                " 1. “회사”라 함은 모바일 기기를 통하여 서비스를 제공하는 사업자를 의미합니다.\n" +
                " 2. “회원”이란 이 약관에 따라 이용계약을 체결하고, 회사가 제공하는 서비스를 이용하는 자를 의미합니다.\n" +
                " 3. “모바일 기기”란 콘텐츠를 다운로드 받거나 설치하여 사용할 수 있는 기기로서, 휴대폰, 스마트폰, 태블릿 등을 의미합니다.\n" +
                " 4. “계정정보”란 회원의 회원번호와 외부계정정보, 기기정보, 닉네임, 프로필 사진, 친구목록 등 회원이 회사에 제공한 정보와 이용정보 (캐릭터 정보, 아이템 등), 이용요금 결제 정보 등을 통칭합니다.\n" +
                " 5. “콘텐츠”란 모바일 기기로 이용할 수 있도록 회사가 서비스 제공과 관련하여 디지털 방식으로 제작한 유료 또는 무료의 내용물 일체(이미지 및 네트워크 서비스, 애플리케이션, 아이템 등)를 의미합니다.\n" +
                " 6. “오픈마켓”이란 모바일 기기에서 콘텐츠를 설치하고 결제할 수 있도록 구축된 전자상거래 환경을 의미합니다.\n" +
                " 7. “애플리케이션”이란 회사가 제공하는 서비스를 이용하기 위하여 모바일 기기를 통해 다운로드 받거나 설치하여 사용하는 프로그램 일체를 의미합니다.\n" +
                " 8. “서비스”라 함은 회사가 제공하는 서비스의 하나로서 회원이 모바일 기기에서 실행하는 일기장 작성 및 공유와 이에 부수하는 서비스를 의미합니다.\n" +
                "② 이 약관에서 사용하는 용어의 정의는 본 조 제1항에서 정하는 것을 제외하고는 관계법령 및 서비스별 정책에서 정하는 바에 의하며, 이에 정하지 아니한 것은 일반적인 상 관례에 따릅니다.\n" +
                "\n"

        binding.termClauseTv.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.termCloseIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.termAgreeIv.setOnClickListener {
            (activity as? ServiceActivity)?.setServiceUseOn()
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        return binding.root
    }
}