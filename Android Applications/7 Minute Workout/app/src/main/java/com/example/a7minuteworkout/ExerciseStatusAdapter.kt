package com.example.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkout.databinding.IitemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>): RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: IitemExerciseStatusBinding): RecyclerView.ViewHolder(binding.root)
    {
        val tvItem= binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(IitemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel= items[position]
        holder.tvItem.text= model.getId().toString()

        when{
            model.getIsSelected()->{
                holder.tvItem.background= ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_thin_color_accent_background)
                holder.tvItem.setTextColor(Color.parseColor("#06d6a0"))
            }
            model.getIsCompleted()->{
                holder.tvItem.background= ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(Color.parseColor("#184e77"))
            }
                    else->{
                        holder.tvItem.background= ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_gray_background)
                        holder.tvItem.setTextColor(Color.parseColor("#023047"))
                    }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}