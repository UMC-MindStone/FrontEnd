package com.example.mindstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindstone.databinding.ItemHabitCardBinding

class HsettingAdapter(
    private val habits: List<Habit>,
    private val onSwitchToggle: (Habit, Boolean) -> Unit
) : RecyclerView.Adapter<HsettingAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(private val binding: ItemHabitCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.tvHabitTitle.text = habit.title
            binding.tvDurationValue.text = habit.duration
            binding.tvDaysValue.text = habit.alarmDays
            binding.tvTimeValue.text = habit.alarmTimes

            // 초기 Switch 상태 설정
            binding.swHabitToggle.isChecked = habit.isEnabled
            updateViewVisibility(habit.isEnabled)

            // Switch 상태 변경 리스너
            binding.swHabitToggle.setOnCheckedChangeListener { _, isChecked ->
                habit.isEnabled = isChecked // 데이터 갱신
                updateViewVisibility(isChecked) // View 갱신
                onSwitchToggle(habit, isChecked)
            }
        }

        private fun updateViewVisibility(isEnabled: Boolean) {
            val visibility = if (isEnabled) View.VISIBLE else View.GONE
            binding.tvDurationLabel.visibility = visibility
            binding.tvDurationValue.visibility = visibility
            binding.tvDaysLabel.visibility = visibility
            binding.tvDaysValue.visibility = visibility
            binding.tvTimeLabel.visibility = visibility
            binding.tvTimeValue.visibility = visibility
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size
}
