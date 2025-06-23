package com.example.graduation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduation.models.TableRow
import com.example.graduation.R

class TableAdapter(private val data: List<TableRow>) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewLevel: TextView = view.findViewById(R.id.text_view_level)
        val textViewPercentage: TextView = view.findViewById(R.id.text_view_percentage)
        val textViewVolume: TextView = view.findViewById(R.id.text_view_volume)
        val textViewMass: TextView = view.findViewById(R.id.text_view_mass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_row, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val row = data[position]
        holder.textViewLevel.text = String.format("%.1f см", row.levelCm)
        holder.textViewPercentage.text = String.format("%.1f %%", row.percentage)
        holder.textViewVolume.text = String.format("%.2f м³", row.volumeM3)
        holder.textViewMass.text = String.format("%.2f т", row.massT)
    }

    override fun getItemCount() = data.size
}
