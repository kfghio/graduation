package com.example.graduation.utils

private const val MAX_ROWS = 1_000_000.00

fun validateStep(totalMm: Double, stepMm: Double): String? {
    if (stepMm <= 0) return "Шаг должен быть > 0"
    val rows = totalMm / stepMm
    var example = "Сделайте шаг ≥ ${"%.2f".format(totalMm / MAX_ROWS)} мм"
    return if (rows > MAX_ROWS)
        "Таблица будет слишком большой $example"
    else null
}
