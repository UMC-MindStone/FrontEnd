package com.example.mindstone.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindstone.R

class MyPageOptionAdapter(
    private val options: List<String>,
    private val onOptionClick: (String) -> Unit
) : RecyclerView.Adapter<MyPageOptionAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvOptionName: TextView = itemView.findViewById(R.id.tv_option_name)

        fun bind(option: String) {
            tvOptionName.text = option
            itemView.setOnClickListener {
                onOptionClick(option)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_option_mypage, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size
}
