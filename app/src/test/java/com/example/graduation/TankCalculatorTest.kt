package com.example.graduation

import com.example.graduation.utils.TankCalculator
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.E
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

    /* ─────────── Цилиндр + конические днища ─────────── */
    @Test fun conicalEndedCylinder_fullVolume() {
        val rM = 0.5
        val lM = 1.0
        val hcM = 0.3

        val expectedFull =
            PI * rM.pow(2) * lM + 2 * PI * rM.pow(2) * hcM / 3.0

        val rows = TankCalculator.generateConicalEndedCylindricalTable(
            lengthMm = lM * 1000,
            diameterMm = rM * 2000,
            endHeightMm = hcM * 1000,
            stepMm = 100.0,
            density = 1.2
        )

        val full = rows.last()
        assertEquals(100.0, full.percentage, EPS)
        assertEquals(expectedFull, full.volumeM3, 1e-4)
        assertEquals(expectedFull * 1.2, full.massT, 1e-4)
    }

    /* ─────────── Одно коническое днище (гор.) ─────────── */
    @Test fun singleCone_fullVolume() {
        val r = 1.0
        val hc = 0.5

        val vFull = PI * r.pow(2) * hc / 3.0

        val rows = TankCalculator.generateSingleConeTankTable(
            diameterMm = r * 2000,
            coneHeightMm = hc * 1000,
            stepMm = 100.0,
            density = 0.95
        )

        assertEquals(vFull, rows.last().volumeM3, 1e-4)
        assertEquals(100.0, rows.last().percentage, EPS)
    }

    /* ─────────── Одно дуговое днище (верт.) ─────────── */
    @Test fun singleArc_percentagesMonotone() {
        val rows = TankCalculator.generateSingleArcTankTable(
            diameterMm = 1000.0,
            capHeightMm = 250.0,
            stepMm = 100.0,
            density = 1.0
        )

        var prev = -1.0
        rows.forEach {
            assertTrue(it.percentage >= prev - EPS)
            prev = it.percentage
        }
        assertEquals(100.0, rows.last().percentage, EPS)
    }

    /* ─────────── Горизонтальный «чистый» эллипс ─────────── */
    @Test fun ellipticalHorizontal_fullVolume() {
        val a = 1.0
        val b = 0.5
        val len = 2.0

        val fullExpected = PI * a * b * len

        val rows = TankCalculator.generateEllipticalHorizontalTable(
            lengthMm = len * 1000,
            heightMm = a * 2000,
            axisMm = b * 2000,
            stepMm = 200.0,
            density = 0.7
        )

        assertEquals(fullExpected, rows.last().volumeM3, 1e-4)
    }

    /* ─────────── Эллипс с двойным срезом — крайние строки ─────────── */
    @Test fun ellipDoubleTrunc_emptyAndFull() {
        val rows = TankCalculator.generateEllipticalDoubleTruncatedTable(
            lengthMm = 1500.0,
            heightMm = 1000.0,
            widthMm  = 800.0,
            axisMm   = 1200.0,
            stepMm   = 500.0,
            density  = 1.0
        )

        assertEquals(0.0, rows.first().percentage, EPS)
        assertEquals(100.0, rows.last().percentage, EPS)
    }

    /* ─────────── Вертикальный цилиндр с фрустум-дном ─────────── */
    @Test fun verticalFrustumBottom_rowCount() {
        val rows = TankCalculator.generateVerticalFrustumBottomTable(
            heightMm = 2000.0,
            bigDiamMm = 1200.0,
            smallDiamMm = 400.0,
            frustumMm = 600.0,
            stepMm = 250.0,
            density = 1.0
        )

        assertEquals(9, rows.size)
        assertEquals(100.0, rows.last().percentage, EPS)
    }

    /* ─────────── Прямоугольно-округлая: монотонный рост массы ─────────── */
    @Test fun roundedRect_massMonotone() {
        val rows = TankCalculator.generateRoundedRectTable(
            lengthMm = 1000.0,
            height1Mm = 800.0,
            height2Mm = 200.0,
            widthMm   = 600.0,
            stepMm    = 100.0,
            density   = 0.9
        )

        rows.reduce { prev, next ->
            assertTrue(next.massT >= prev.massT - EPS)
            next
        }
    }

    /* ─────────── Винная бочка — полный объём совпадает ─────────── */
    @Test fun wineBarrel_fullPercent() {
        val rows = TankCalculator.generateWineBarrelTable(
            heightMm = 1200.0,
            bigDiamMm = 1000.0,
            smallDiamMm = 600.0,
            stepMm = 300.0,
            density = 1.0
        )
        assertEquals(100.0, rows.last().percentage, EPS)
    }

    /* ────────── Цилиндр + усеч.-конические днища ────────── */
    @Test fun frustumEndedCylinder_fullVolume() {
        val rBig = 0.6
        val rSmall = 0.2
        val hCone = 0.4
        val len = 1.5

        val cyl = PI * rBig.pow(2) * len
        val cone = PI * hCone / 3.0 * (rBig.pow(2) + rBig * rSmall + rSmall.pow(2))
        val expected = cyl + 2 * cone

        val rows = TankCalculator.generateFrustumEndedCylindricalTable(
            lengthMm   = len   * 1000,
            bigDiamMm  = rBig  * 2000,
            smallDiamMm= rSmall* 2000,
            endHeightMm= hCone * 1000,
            stepMm     = 100.0,
            density    = 0.85
        )

        val last = rows.last()
        assertEquals(100.0, last.percentage, E)
        assertEquals(expected, last.volumeM3, 2e-3)
        assertEquals(expected * 0.85, last.massT, 2e-3)
    }

    /* ────────── Чемоданная ёмкость: полный объём ────────── */
    @Test fun suitcase_fullVolume() {
        val length = 1.0
        val h1  = 0.8
        val h2  = 0.3
        val width = 0.6

        val a = width / 2.0
        val b = (h1 - h2) / 2.0
        val halfEllipseArea = PI * a * b / 2.0
        val cross = 2 * halfEllipseArea + width * h2
        val expected = cross * length

        val rows = TankCalculator.generateSuitcaseTable(
            lengthMm = length * 1000,
            height1Mm= h1 * 1000,
            height2Mm= h2 * 1000,
            widthMm  = width * 1000,
            stepMm   = 100.0,
            density  = 1.0
        )

        assertEquals(expected, rows.last().volumeM3, 1e-3)
        assertEquals(100.0, rows.last().percentage, E)
    }

    /* ────────── Вертикальный прямоуг. + пир. дно ────────── */
    @Test fun verticalRectFrustum_fullVolume() {
        val A  = 1.2
        val B  = 1.0
        val a2 = 0.6
        val b2 = 0.4
        val f  = 0.5
        val hC = 1.0
        val step = 100.0

        val p = (A - a2) / f
        val q = (B - b2) / f
        val vFrust = a2 * b2 * f + (a2 * q + b2 * p) * f*f / 2 + p * q * f*f*f / 3
        val vExpected = vFrust + A * B * hC

        val rows = TankCalculator.generateVerticalRectFrustumTable(
            heightMm = (hC + f) * 1000,
            bigAMm   = A * 1000,
            bigBMm   = B * 1000,
            smallAm  = a2*1000,
            smallBm  = b2*1000,
            frustumMm= f * 1000,
            stepMm   = step,
            density  = 0.9
        )

        assertEquals(vExpected, rows.last().volumeM3, 3e-3)
        assertEquals(100.0,   rows.last().percentage, E)
    }

    /* ────────── Цилиндр + дуговые днища: % растёт монотонно ────────── */
    @Test fun arcEndedCylinder_percentMonotone() {
        val rows = TankCalculator.generateArcEndedCylindricalTable(
            lengthMm  = 1000.0,
            diameterMm= 800.0,
            endHeightMm = 250.0,
            stepMm = 100.0,
            density = 1.0
        )
        rows.reduce { a, b ->
            assertTrue(b.percentage >= a.percentage - 1e-6)
            b
        }
        assertEquals(100.0, rows.last().percentage, E)
    }

    /* ────────── Полусферические днища: контроль половины ────────── */
    @Test fun hemisphericalEnded_halfLevelApprox50() {
        val rows = TankCalculator.generateHemisphericalEndedCylindricalTable(
            lengthMm  = 1000.0,
            diameterMm= 1000.0,
            stepMm    = 100.0,
            density   = 1.0
        )

        val half = rows.first { kotlin.math.abs(it.levelCm - 50.0) < 1e-3 }
        assertEquals(50.0, half.percentage, 3.0)
    }

    @Test fun ellipticalEnds_fullEmptyMonotone() {

        val length = 1000.0
        val diameter = 1000.0
        val headH = 200.0
        val step = 100.0
        val rho = 0.9

        val r = diameter / 2000.0
        val cyl = PI * r.pow(2) * (length / 1000.0)
        val a = diameter / 2.0 / 1000.0
        val h = headH / 1000.0
        val head = PI * h * (3 * a * a + h * h) / 6.0
        val fullExpected = cyl + 2 * head

        val rows = TankCalculator.generateEllipticalEndedCylindricalTable(
            lengthMm = length,
            diameterMm = diameter,
            headHeightMm = headH,
            stepMm = step,
            density = rho
        )

        assertEquals(0.0, rows.first().volumeM3, EPS)
        assertEquals(0.0, rows.first().percentage, EPS)

        val last = rows.last()
        assertEquals(100.0, last.percentage, EPS)
        assertEquals(fullExpected, last.volumeM3, 2e-3)
        assertEquals(fullExpected * rho, last.massT, 2e-3)

        rows.reduce { prev, next ->
            assertTrue(next.percentage + 1e-6 >= prev.percentage)
            next
        }
    }
}
