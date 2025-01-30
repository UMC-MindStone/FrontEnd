package com.example.mindstone.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mindstone.NotiRVAdapter
import com.example.mindstone.NotificationItem
import com.example.mindstone.R
import com.example.mindstone.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("NotificationFragment", "RecyclerView width: ${binding.notiRl.width}, height: ${binding.notiRl.height}")

        // mock data
        val notificationList = arrayListOf(
            NotificationItem(
                notiIcon = R.drawable.ic_emotion,
                notiText = "습관 기록 안한지 1일이 지났어요!",
                notiDate = "10분 전"
            ),
            NotificationItem(
                notiIcon = R.drawable.ic_emotion,
                notiText = "오늘의 일기를 기록해볼까요?",
                notiDate = "1일 전"
            )
        )

        // RecyclerView와 Adapter 설정
        val adapter = NotiRVAdapter(notificationList)
        binding.notiRl.apply{
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}