package com.example.mindstone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindstone.databinding.ItemNotificationBinding

class NotiRVAdapter(private val itemList: ArrayList<NotificationItem>) :
    RecyclerView.Adapter<NotiRVAdapter.ViewHolder>() {

    // ViewHolder 클래스 정의
    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NotificationItem) {
            binding.itemNotiIconIv.setImageResource(item.notiIcon)
            binding.itemNotiTextTv.text = item.notiText
            binding.itemNotiDateTv.text = item.notiDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}

// 데이터 클래스 정의
data class NotificationItem(
    val notiIcon: Int,
    val notiText: String,
    val notiDate: String // 날짜는 String으로 표현
)
