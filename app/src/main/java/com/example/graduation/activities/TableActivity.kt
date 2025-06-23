package com.example.graduation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduation.R
import com.example.graduation.adapters.TableAdapter
import com.example.graduation.utils.TableFactory

class TableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val table = TableFactory.buildTableFromIntent(intent)

        findViewById<RecyclerView>(R.id.recycler_view_table).apply {
            layoutManager = LinearLayoutManager(this@TableActivity)
            adapter = TableAdapter(table)
        }
    }

}
