package com.example.graduation.utils

import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.example.graduation.activities.TableActivity
import com.example.graduation.models.Liquid

fun AppCompatActivity.showError(msg: String) =
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun AppCompatActivity.launchTable(shape: String, vararg extras: Pair<String, Double>) {
    Intent(this, TableActivity::class.java).apply {
        putExtra("shape", shape)
        extras.forEach { putExtra(it.first, it.second) }
        startActivity(this)
    }
}

fun Spinner.bindLiquids(liquids: List<Liquid>, activity: AppCompatActivity) {
    adapter = ArrayAdapter(
        activity,
        android.R.layout.simple_spinner_item,
        liquids.map { it.name }
    ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
}
