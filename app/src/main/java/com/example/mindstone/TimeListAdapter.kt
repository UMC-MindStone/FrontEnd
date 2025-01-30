package com.example.mindstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindstone.databinding.ItemAddButtonBinding
import com.example.mindstone.databinding.ItemTimeBinding

class TimeListAdapter(
    private val times: MutableList<String>,
    private val onTimeClick: (Int, String) -> Unit,
    private val onAddClick: () -> Unit // + 버튼 클릭 콜백 추가
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_TIME = 0
        private const val VIEW_TYPE_ADD = 1
    }

    inner class TimeViewHolder(private val binding: ItemTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, time: String) {
            binding.tvTimeItem.text = time
            binding.root.setOnClickListener {
                onTimeClick(position, time)
            }
        }
    }

    inner class AddButtonViewHolder(private val binding: ItemAddButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvAddButton.setOnClickListener {
                onAddClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < times.size) VIEW_TYPE_TIME else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TIME) {
            val binding = ItemTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            TimeViewHolder(binding)
        } else {
            val binding = ItemAddButtonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            AddButtonViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TimeViewHolder) {
            holder.bind(position, times[position])
        } else if (holder is AddButtonViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int = times.size + 1 // + 버튼 포함
}
