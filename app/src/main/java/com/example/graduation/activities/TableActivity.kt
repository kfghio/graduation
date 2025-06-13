package com.example.graduation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduation.adapters.TableAdapter
import com.example.graduation.models.TableRow
import com.example.graduation.R

class TableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val tableData = intent.getParcelableArrayListExtra<TableRow>("table_data") ?: emptyList()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_table)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TableAdapter(tableData)
    }
}