package com.example.colorguess

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_row.view.*

class ItemAdapter(val context: Context, val items: ArrayList<user>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.items_row, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items.get(position)
        holder.itemView.tvName.text = item.name
        holder.itemView.tvDate.text = item.date
        holder.itemView.tvResult.text = item.result

        if (position % 2 == 0) {
            holder.itemView.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightGray
                )
            )
        } else {
            holder.itemView.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.lightBlue
                )
            )
        }

    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}