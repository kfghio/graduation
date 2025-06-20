package com.example.graduation

import com.example.graduation.utils.TankCalculator
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI
import kotlin.math.pow

private const val EPS = 1e-6

class TankCalculatorTest {

    /* ──────────────── Прямоугольный бак ──────────────── */
    @Test fun rectangular_fullAndEmpty() {
        val rows = TankCalculator.generateRectangularTable(
            lengthMm = 1000.0,
            widthMm  = 1000.0,
            heightMm = 1000.0,
            stepMm   = 500.0,
            density  = 1.0
        )

        assertEquals(3, rows.size)

        val empty = rows.first()
        val full  = rows.last()

        assertEquals(0.0, empty.volumeM3, EPS)
        assertEquals(0.0, empty.percentage, EPS)

        assertEquals(1.0, full.volumeM3, EPS)
        assertEquals(100.0, full.percentage, EPS)
        assertEquals(1.0, full.massT, EPS)
    }

    /* ──────────────── Горизонтальный цилиндр ──────────────── */
    @Test fun horizontalCylinder_fullVolumeCheck() {

        val rows = TankCalculator.generateCylindricalTable(
            lengthMm  = 1000.0,
            diameterMm= 1000.0,
            stepMm = 500.0,
            density = 0.8
        )

        val r = 0.5
        val L = 1.0
        val vExact = PI * r * r * L

        val full = rows.last()

        assertEquals(vExact, full.volumeM3, 1e-5)
        assertEquals(100.0,  full.percentage, EPS)
        assertEquals(vExact * 0.8, full.massT, 1e-5)
    }

    /* ──────────────── Сфера ──────────────── */
    @Test fun sphere_firstAndLastRows() {

        val rows = TankCalculator.generateSphereTable(
            diameterMm = 2000.0,
            stepMm = 1000.0,
            density = 2.0
        )

        val R = 1.0
        val vFull = 4.0 / 3.0 * PI * R.pow(3)

        assertEquals(3, rows.size)

        val empty = rows[0]
        val half  = rows[1]
        val full  = rows[2]

        // пусто
        assertEquals(0.0, empty.volumeM3, EPS)
        assertEquals(0.0, empty.percentage, EPS)

        // половина сферы
        val vHalf = 2 * PI * R.pow(3) / 3.0
        assertEquals(vHalf, half.volumeM3, 1e-5)
        assertEquals(vHalf / vFull * 100, half.percentage, 1e-4)

        // полная сфера
        assertEquals(vFull, full.volumeM3, 1e-5)
        assertEquals(100.0, full.percentage, EPS)
        assertEquals(vFull * 2.0, full.massT, 1e-5)
    }

    /* ──────────────── Проверка «шаг-->кол-во строк»  ──────────────── */

    @Test fun listSizeIsHeightDivStepPlusOne() {
        val heightMm = 1200.0
        val stepMm = 300.0

        val rows = TankCalculator.generateVerticalCylindricalTable(
            heightMm = heightMm,
            diameterMm = 1000.0,
            stepMm = stepMm,
            density = 1.0
        )

        val expectedRows = (heightMm / stepMm).toInt() + 1
        assertEquals(expectedRows, rows.size)
    }

    /* ──────────────── Симметрия прямоуг.-округлого бака ──────────────── */
    @Test fun roundedRect_symmetryAt50Percent() {

        val rows = TankCalculator.generateRoundedRectTable(
            lengthMm = 1000.0,
            height1Mm = 1000.0,
            height2Mm = 100.0,
            widthMm = 1000.0,
            stepMm = 500.0,
            density = 1.0
        )

        val fifty = rows.first { it.levelCm == 50.0 }
        assertEquals(50.0, fifty.percentage, 2.0)
    }
}
