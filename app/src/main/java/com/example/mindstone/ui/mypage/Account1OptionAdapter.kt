package com.example.mindstone.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindstone.databinding.ItemOptionAccount1Binding

class Account1OptionAdapter(
    private val settings: List<String>,
    private val onSettingClick: (String) -> Unit
) : RecyclerView.Adapter<Account1OptionAdapter.Account1OptionViewHolder>() {

    inner class Account1OptionViewHolder(
        private val binding: ItemOptionAccount1Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(setting: String) {
            binding.tvOptionAccount1.text = setting
            binding.root.setOnClickListener {
                onSettingClick(setting)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Account1OptionViewHolder {
        val binding = ItemOptionAccount1Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Account1OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Account1OptionViewHolder, position: Int) {
        holder.bind(settings[position])
    }

    override fun getItemCount(): Int = settings.size
}
