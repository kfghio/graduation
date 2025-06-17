package com.example.graduation.utils

enum class Section { HORIZONTAL, VERTICAL, OTHER }

fun Section.prev() = when (this) {
    Section.HORIZONTAL -> Section.OTHER
    Section.VERTICAL -> Section.HORIZONTAL
    Section.OTHER -> Section.VERTICAL
}

fun Section.next() = when (this) {
    Section.HORIZONTAL -> Section.VERTICAL
    Section.VERTICAL -> Section.OTHER
    Section.OTHER -> Section.HORIZONTAL
}

fun Section.title() = when (this) {
    Section.HORIZONTAL -> "Горизонтальные ёмкости"
    Section.VERTICAL -> "Вертикальные ёмкости"
    Section.OTHER -> "Прочие"
}

fun Section.pageIndex() = when (this) {
    Section.HORIZONTAL -> 0
    Section.VERTICAL -> 1
    Section.OTHER -> 2
}
