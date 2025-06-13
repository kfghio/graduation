package com.example.graduation.utils

import com.example.graduation.models.TableRow
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object TankCalculator {
    fun generateRectangularTable(lengthMm: Double, widthMm: Double, heightMm: Double, stepMm: Double, density: Double): List<TableRow> {
        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= heightMm) {
            val levelCm = h / 10.0
            val percentage = (h / heightMm) * 100.0
            val volumeM3 = (lengthMm / 1000.0) * (widthMm / 1000.0) * (h / 1000.0)
            val massT = volumeM3 * density
            rows.add(TableRow(levelCm, percentage, volumeM3, massT))
            h += stepMm
        }
        return rows
    }

    fun generateCylindricalTable(lengthMm: Double, diameterMm: Double, stepMm: Double, density: Double): List<TableRow> {
        val rows = mutableListOf<TableRow>()
        val radiusMm = diameterMm / 2.0
        val maxHeightMm = diameterMm

        var h = 0.0
        while (h <= maxHeightMm + 1e-6) {
            val levelCm = h / 10.0
            val percentage = (h / maxHeightMm) * 100.0

            val r = radiusMm / 1000.0
            val l = lengthMm / 1000.0
            val hMeters = h / 1000.0

            val volumeM3 = calculateHorizontalCylinderVolume(r, l, hMeters)
            val massT = volumeM3 * density

            rows.add(TableRow(levelCm, percentage, volumeM3, massT))
            h += stepMm
        }
        return rows
    }

    private fun calculateHorizontalCylinderVolume(r: Double, l: Double, h: Double): Double {
        if (h <= 0) return 0.0
        if (h >= 2 * r) return Math.PI * r * r * l

        val theta = 2 * acos((r - h) / r)
        val area = (r * r / 2) * (theta - sin(theta))
        return area * l
    }

    fun generateEllipticalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        headHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val radiusMm = diameterMm / 2.0
        val maxHeightMm = diameterMm
        val cylinderLengthMm = lengthMm
        val headVolumeM3 = calculateEllipticalHeadVolume(diameterMm, headHeightMm)
        val l = cylinderLengthMm / 1000.0
        val r = radiusMm / 1000.0

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= maxHeightMm) {
            val levelCm = h / 10.0
            val percentage = (h / maxHeightMm) * 100.0

            val hMeters = h / 1000.0
            val cylVolumeM3 = calculateHorizontalCylinderVolume(r, l, hMeters)
            val totalVolumeM3 = cylVolumeM3 + 2 * headVolumeM3 * (h / maxHeightMm)

            val massT = totalVolumeM3 * density

            rows.add(TableRow(levelCm, percentage, totalVolumeM3, massT))
            h += stepMm
        }

        return rows
    }

    private fun calculateEllipticalHeadVolume(diameterMm: Double, heightMm: Double): Double {
        val a = diameterMm / 2.0 / 1000.0
        val h = heightMm / 1000.0
        return (Math.PI * h * (3 * a * a + h * h)) / 6.0
    }

    fun generateHemisphericalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val radiusM = diameterMm / 2.0 / 1000.0
        val totalHeightMm = diameterMm
        val cylinderLengthM = lengthMm / 1000.0
        val hemisphereVolumeM3 = (2.0 / 3.0) * Math.PI * Math.pow(radiusM, 3.0)

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= totalHeightMm) {
            val levelCm = h / 10.0
            val percentage = (h / totalHeightMm) * 100.0
            val hMeters = h / 1000.0

            val cylVolumeM3 = calculateHorizontalCylinderVolume(radiusM, cylinderLengthM, hMeters)
            val hemispheresContribution = 2 * hemisphereVolumeM3 * (h / totalHeightMm)

            val totalVolumeM3 = cylVolumeM3 + hemispheresContribution
            val massT = totalVolumeM3 * density

            rows.add(TableRow(levelCm, percentage, totalVolumeM3, massT))
            h += stepMm
        }

        return rows
    }

    fun generateArcEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        endHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val radiusM = diameterMm / 2.0 / 1000.0
        val totalHeightMm = diameterMm
        val endHeightM = endHeightMm / 1000.0
        val arcEndVolumeM3 = calculateArcEndVolume(diameterMm, endHeightMm)
        val cylinderLengthM = lengthMm / 1000.0

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= totalHeightMm) {
            val levelCm = h / 10.0
            val percentage = (h / totalHeightMm) * 100.0
            val hMeters = h / 1000.0

            val cylVolumeM3 = calculateHorizontalCylinderVolume(radiusM, cylinderLengthM, hMeters)
            val arcEndsContribution = 2 * arcEndVolumeM3 * (h / totalHeightMm)

            val totalVolumeM3 = cylVolumeM3 + arcEndsContribution
            val massT = totalVolumeM3 * density

            rows.add(TableRow(levelCm, percentage, totalVolumeM3, massT))
            h += stepMm
        }

        return rows
    }

    private fun calculateArcEndVolume(diameterMm: Double, heightMm: Double): Double {
        val R = diameterMm / 2.0 / 1000.0
        val h = heightMm / 1000.0
        if (h <= 0 || h >= 2 * R) return 0.0

        val theta = 2 * acos((R - h) / R)
        val segmentArea = (R * R / 2) * (theta - sin(theta))
        return segmentArea * diameterMm / 1000.0
    }

    fun generateConicalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        endHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val radiusM = diameterMm / 2.0 / 1000.0
        val endHeightM = endHeightMm / 1000.0
        val cylinderLengthM = lengthMm / 1000.0
        val totalHeightMm = diameterMm

        val conicalEndVolumeM3 = (1.0 / 3.0) * Math.PI * radiusM * radiusM * endHeightM

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= totalHeightMm) {
            val levelCm = h / 10.0
            val percentage = (h / totalHeightMm) * 100.0
            val hMeters = h / 1000.0

            val cylVolumeM3 = calculateHorizontalCylinderVolume(radiusM, cylinderLengthM, hMeters)
            val coneContribution = 2 * conicalEndVolumeM3 * (h / totalHeightMm)

            val totalVolumeM3 = cylVolumeM3 + coneContribution
            val massT = totalVolumeM3 * density

            rows.add(TableRow(levelCm, percentage, totalVolumeM3, massT))
            h += stepMm
        }

        return rows
    }

    fun generateFrustumEndedCylindricalTable(
        lengthMm: Double,
        bigDiamMm: Double,
        smallDiamMm: Double,
        endHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val r1 = bigDiamMm   / 2000.0
        val r2 = smallDiamMm / 2000.0
        val hCone = endHeightMm / 1000.0
        val lCyl  = lengthMm   / 1000.0
        val hMaxMm = bigDiamMm

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= hMaxMm + 1e-6) {
            val fillH = h / 1000.0
            val levelCm    = h / 10.0
            val percentage = h / hMaxMm * 100.0

            val cylVol = calculateHorizontalCylinderVolume(r1, lCyl, fillH)

            val frustumVol = 2 * partialFrustumVolume(r1, r2, hCone, fillH)

            val totalVol = cylVol + frustumVol
            val massT = totalVol * density

            rows.add(TableRow(levelCm, percentage, totalVol, massT))
            h += stepMm
        }
        return rows
    }

    private fun partialFrustumVolume(
        r1: Double,
        r2: Double,
        hCone: Double,
        fillH: Double,
        slices: Int = 1000
    ): Double {
        if (fillH <= 0) return 0.0
        if (fillH >= 2 * r1) {
            return (Math.PI * hCone / 3.0) * (r1 * r1 + r1 * r2 + r2 * r2)
        }

        val dx = hCone / slices
        var vol = 0.0
        for (i in 0 until slices) {
            val x = (i + 0.5) * dx
            val r = r1 - (r1 - r2) * (x / hCone)
            val area = circularSegmentArea(r, fillH.coerceAtMost(2 * r))
            vol += area * dx
        }
        return vol
    }

    private fun circularSegmentArea(r: Double, h: Double): Double {
        return when {
            h <= 0 -> 0.0
            h >= 2 * r -> Math.PI * r * r
            else -> {
                val theta = 2 * acos((r - h) / r)
                0.5 * r * r * (theta - sin(theta))
            }
        }
    }

    fun generateEllipticalHorizontalTable(
        lengthMm: Double,
        heightMm: Double,
        axisMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val a = (heightMm / 2.0) / 1000.0
        val b = (axisMm / 2.0) / 1000.0
        val length = lengthMm / 1000.0
        val step = stepMm / 1000.0
        val maxHeight = heightMm / 1000.0

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= maxHeight + 1e-6) {
            val levelCm = h * 100.0
            val percentage = (h / maxHeight) * 100.0
            val areaM2 = calculateEllipticalSegmentArea(h, a, b)
            val volumeM3 = areaM2 * length
            val massT = volumeM3 * density
            rows.add(TableRow(levelCm, percentage, volumeM3, massT))
            h += step
        }
        return rows
    }

    private fun calculateEllipticalSegmentArea(h: Double, a: Double, b: Double, n: Int = 100): Double {
        if (h <= 0) return 0.0
        if (h >= 2 * a) return Math.PI * a * b

        val dy = h / n
        var sum = 0.0
        for (i in 0..n) {
            val y = i * dy
            val term = 1 - ((y - a) / a).pow(2)
            if (term < 0) continue
            val width = 2 * b * sqrt(term)
            sum += if (i == 0 || i == n) width / 2.0 else width
        }
        return sum * dy
    }

    fun generateEllipticalDoubleTruncatedTable(
        lengthMm: Double,
        heightMm: Double,
        widthMm: Double,
        axisMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val a = axisMm   / 2.0 / 1000.0
        val b = heightMm / 2.0 / 1000.0
        val lengthM = lengthMm / 1000.0

        val k = widthMm / axisMm
        require(k in 0.0..1.0) { "widthMm must be ≤ axisMm" }

        val y0 = b * sqrt(1 - k * k)
        val yBottom = -y0
        val yTop    =  y0
        val workH   = yTop - yBottom

        val areaBottom = segmentEllipticalArea(yBottom, a, b)
        val areaTop = segmentEllipticalArea(yTop, a, b)
        val areaStrip = areaTop - areaBottom
        val volFull = areaStrip * lengthM

        val rows = mutableListOf<TableRow>()
        val stepM = stepMm / 1000.0
        var hM = 0.0
        while (hM <= workH + 1e-9) {
            val y = yBottom + hM
            val area = segmentEllipticalArea(y, a, b) - areaBottom
            val vol  = area * lengthM
            rows += TableRow(
                levelCm   = hM * 100.0,
                percentage= vol / volFull * 100.0,
                volumeM3  = vol,
                massT     = vol * density
            )
            hM += stepM
        }
        return rows
    }

    private fun segmentEllipticalArea(y: Double, a: Double, b: Double): Double {
        val t = y.coerceIn(-b, b) / b
        return a * b * (asin(t) + Math.PI / 2 + t * sqrt(1 - t * t))
    }

    // Здесь можно добавить функции для других типов цистерн

}
