package com.example.graduation.models

data class Liquid(val name: String, val density: Double)

object LiquidRepository {
    val list = listOf(
        Liquid("Вода", 1.0000),
        Liquid("Керосин", 0.8201),
        Liquid("Бензин", 0.7370),
        Liquid("Этиловый спирт", 0.7910),
        Liquid("Серная кислота 95%", 1.8390),
        Liquid("Дизель", 0.8200),
        Liquid("Мазут", 0.8900),
        Liquid("Сырая нефть", 0.8730),
        Liquid("СПГ", 0.4500)
    )
}