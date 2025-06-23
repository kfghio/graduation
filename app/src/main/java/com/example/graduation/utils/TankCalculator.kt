package com.example.graduation.utils

import com.example.graduation.models.TableRow
import kotlin.math.*

object TankCalculator {

    /* ────────────────────────── Прямоугольная ───────────────────────── */
    fun generateRectangularTable(
        lengthMm: Double,
        widthMm: Double,
        heightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val fullVolM3 = (lengthMm * widthMm * heightMm) / 1e9   // м³
        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= heightMm + 1e-6) {
            val levelCm   = h / 10.0
            val volumeM3  = (lengthMm * widthMm * h) / 1e9
            val percentage= volumeM3 / fullVolM3 * 100.0
            val massT     = volumeM3 * density
            rows += TableRow(levelCm, percentage, volumeM3, massT)
            h += stepMm
        }
        return rows
    }

    /* ────────────────────────── Цилиндрическая ──────────────────────── */
    fun generateCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val r = diameterMm / 2000.0
        val lengthM = lengthMm / 1000.0
        val fullVolM3 = Math.PI * r * r * lengthM
        val rows = mutableListOf<TableRow>()

        var h = 0.0
        while (h <= diameterMm + 1e-6) {
            val levelCm = h / 10.0
            val hMeters = h / 1000.0
            val volumeM3 = calculateHorizontalCylinderVolume(r, lengthM, hMeters)
            val percentage = volumeM3 / fullVolM3 * 100.0
            val massT = volumeM3 * density
            rows += TableRow(levelCm, percentage, volumeM3, massT)
            h += stepMm
        }
        return rows
    }

    private fun calculateHorizontalCylinderVolume(r: Double, l: Double, h: Double): Double {
        if (h <= 0) return 0.0
        if (h >= 2 * r) return Math.PI * r * r * l
        val theta = 2 * acos((r - h) / r)
        val area  = (r * r / 2) * (theta - sin(theta))
        return area * l
    }

    /* ────────────────── Цилиндр с эллиптическими днищами ─────────────── */
    fun generateEllipticalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        headHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val r = diameterMm / 2000.0
        val lengthM = lengthMm / 1000.0
        val cylinderFull = Math.PI * r * r * lengthM
        val headVol = calculateEllipticalHeadVolume(diameterMm, headHeightMm)
        val fullVolM3 = cylinderFull + 2 * headVol

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= diameterMm + 1e-6) {
            val levelCm = h / 10.0
            val hMeters = h / 1000.0
            val cylVol  = calculateHorizontalCylinderVolume(r, lengthM, hMeters)
            val headsPart = 2 * headVol * (h / diameterMm)
            val volumeM3 = cylVol + headsPart
            val percentage = volumeM3 / fullVolM3 * 100.0
            val massT = volumeM3 * density
            rows += TableRow(levelCm, percentage, volumeM3, massT)
            h += stepMm
        }
        return rows
    }

    private fun calculateEllipticalHeadVolume(diameterMm: Double, heightMm: Double): Double {
        val a = diameterMm / 2.0 / 1000.0
        val h = heightMm / 1000.0
        return (Math.PI * h * (3 * a * a + h * h)) / 6.0
    }

    /* ──────────────── Цилиндр с полусферическими днищами ─────────────── */
    fun generateHemisphericalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val r = diameterMm / 2000.0
        val lengthM = lengthMm / 1000.0
        val cylinderFull = Math.PI * r * r * lengthM
        val hemisphereVol = 2.0 / 3.0 * Math.PI * r.pow(3)
        val fullVolM3 = cylinderFull + 2 * hemisphereVol

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= diameterMm + 1e-6) {
            val levelCm = h / 10.0
            val hM = h / 1000.0
            val cylVol = calculateHorizontalCylinderVolume(r, lengthM, hM)
            val hemisPart = 2 * hemisphereVol * (h / diameterMm)
            val volumeM3 = cylVol + hemisPart
            val percentage = volumeM3 / fullVolM3 * 100.0
            val massT = volumeM3 * density
            rows += TableRow(levelCm, percentage, volumeM3, massT)
            h += stepMm
        }
        return rows
    }

    /* ──────────────── Цилиндр + два дуговых днища (точный объём) ─────────────── */
    fun generateArcEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        endHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val R = diameterMm / 2.0 / 1000.0
        val f = endHeightMm / 1000.0
        val L = lengthMm / 1000.0
        val stepM = stepMm / 1000.0
        val H = 2 * R

        val planeX = -R + f

        val headFull = volumeUpTo(R, R, planeX)

        val cylFull = Math.PI * R * R * L

        val fullVol = cylFull + 2 * headFull

        val rows = mutableListOf<TableRow>()
        var level = 0.0

        while (level <= H + 1e-9) {

            val cylPart = calculateHorizontalCylinderVolume(R, L, level)

            val z= -R + level
            val headPart = volumeUpTo(z, R, planeX)

            val volM3   = cylPart + 2 * headPart
            val massT   = volM3 * density
            val percent = volM3 / fullVol * 100.0

            rows += TableRow(level * 100.0, percent, volM3, massT)
            level += stepM
        }
        return rows
    }



    /* ──────────────── Цилиндр с коническими днищами ──────────────────── */
    fun generateConicalEndedCylindricalTable(
        lengthMm: Double,
        diameterMm: Double,
        endHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val R = diameterMm / 2.0 / 1000.0
        val Hc = endHeightMm / 1000.0
        val L = lengthMm / 1000.0
        val stepM = stepMm / 1000.0
        val H = 2 * R

        val cylFull = Math.PI * R * R * L
        val coneFull = Math.PI * R * R * Hc / 3.0
        val fullVol = cylFull + 2 * coneFull

        val rows = mutableListOf<TableRow>()
        var level = 0.0
        while (level <= H + 1e-9) {

            val cylPart = calculateHorizontalCylinderVolume(R, L, level)

            val conePartOne = partialFrustumVolume(r1 = R, r2 = 0.0, hCone = Hc, fillH = level)

            val volM3 = cylPart + 2 * conePartOne
            val massT = volM3 * density
            val percent = volM3 / fullVol * 100.0

            rows += TableRow(level * 100.0, percent, volM3, massT)
            level += stepM
        }
        return rows
    }

    /* ──────────────── Цилиндр с усечённо-коническими днищами ─────────── */
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
        val cylLenM = lengthMm / 1000.0

        val vCylFull  = Math.PI * r1 * r1 * cylLenM
        val vConeFull = Math.PI * hCone / 3.0 * (r1 * r1 + r1 * r2 + r2 * r2)
        val vFull = vCylFull + 2 * vConeFull

        val rows = mutableListOf<TableRow>()
        var hMm = 0.0
        while (hMm <= bigDiamMm + 1e-6) {
            val hM = hMm / 1000.0
            val cylVol   = calculateHorizontalCylinderVolume(r1, cylLenM, hM)
            val coneVol  = 2 * partialFrustumVolume(r1, r2, hCone, hM)
            val volumeM3 = cylVol + coneVol
            val pct      = volumeM3 / vFull * 100.0
            val massT    = volumeM3 * density

            rows += TableRow(hMm / 10.0, pct, volumeM3, massT)
            hMm += stepMm
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

        if (fillH >= 2 * r1 - 1e-9) {
            return Math.PI * hCone / 3.0 * (r1 * r1 + r1 * r2 + r2 * r2)
        }

        if (fillH <= 0) return 0.0

        val dx = hCone / slices
        var v = 0.0
        for (i in 0 until slices) {
            val x = (i + 0.5) * dx
            val rSlice = r1 - (r1 - r2) * (x / hCone)

            val localFill = fillH - (r1 - rSlice)
            if (localFill <= 0) continue

            val area = if (localFill >= 2 * rSlice)
                Math.PI * rSlice * rSlice
            else
                circularSegmentArea(rSlice, localFill)

            v += area * dx
        }
        return v
    }

    private fun circularSegmentArea(r: Double, h: Double): Double = when {
        h <= 0 -> 0.0
        h >= 2 * r -> Math.PI * r * r
        else -> {
            val theta = 2 * acos((r - h) / r)
            0.5 * r * r * (theta - sin(theta))
        }
    }


    /* ─────────────────── Горизонтальный чистый эллипс ───────────────── */
    fun generateEllipticalHorizontalTable(
        lengthMm: Double,
        heightMm: Double,
        axisMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {
        val a = heightMm / 2000.0
        val b = axisMm   / 2000.0
        val lengthM = lengthMm / 1000.0
        val fullVolM3 = Math.PI * a * b * lengthM

        val rows = mutableListOf<TableRow>()
        val maxHeight = heightMm / 1000.0
        val stepM = stepMm / 1000.0
        var hM = 0.0
        while (hM <= maxHeight + 1e-9) {
            val y = -a + hM
            val area = segmentEllipticalArea(y, b, a)
            val volumeM3 = area * lengthM
            val percentage = volumeM3 / fullVolM3 * 100.0
            val massT = volumeM3 * density
            rows += TableRow(hM * 100.0, percentage, volumeM3, massT)
            hM += stepM
        }
        return rows
    }

    /* ──────────────── Эллипс с симметричным двойным срезом ──────────────── */
    fun generateEllipticalDoubleTruncatedTable(
        lengthMm: Double,
        heightMm: Double,
        widthMm: Double,
        axisMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val a_mm = axisMm / 2.0
        val H_mm = heightMm
        val y0_mm = H_mm / 2.0

        val denom = 1.0 - (widthMm * widthMm) / (4.0 * a_mm * a_mm)
        val b_mm = y0_mm / sqrt(denom)

        val a = a_mm / 1000.0
        val b = b_mm / 1000.0
        val y0 = y0_mm / 1000.0
        val lengthM = lengthMm / 1000.0
        val workH = H_mm / 1000.0

        val areaBottom = segmentEllipticalArea(-y0, a, b)
        val areaTop    = segmentEllipticalArea( y0, a, b)
        val areaStrip  = areaTop - areaBottom
        val fullVolM3  = areaStrip * lengthM

        val rows = mutableListOf<TableRow>()
        val stepM = stepMm / 1000.0
        var hM = 0.0
        while (hM <= workH + 1e-9) {
            val y = -y0 + hM
            val area = segmentEllipticalArea(y, a, b) - areaBottom
            val vol  = area * lengthM
            rows += TableRow(
                levelCm = hM * 100.0,
                percentage= vol / fullVolM3 * 100.0,
                volumeM3 = vol,
                massT = vol * density
            )
            hM += stepM
        }
        return rows
    }

    private fun segmentEllipticalArea(y: Double, a: Double, b: Double): Double {
        val t = y.coerceIn(-b, b) / b
        return a * b * (asin(t) + Math.PI / 2 + t * sqrt(1 - t * t))
    }

    /* ───── Горизонтальная прямоугольно-округлая ёмкость (плоские днища) ───── */
    fun generateRoundedRectTable(
        lengthMm: Double,
        height1Mm: Double,
        height2Mm: Double,
        widthMm : Double,
        stepMm  : Double,
        density : Double
    ): List<TableRow> {

        val H1 = height1Mm / 1000.0
        val H2 = height2Mm / 1000.0
        val R = (H1 - H2) / 2.0
        val B = widthMm  / 1000.0
        val L = lengthMm / 1000.0
        val step = stepMm / 1000.0
        val centerW = B - 2 * R

        val fullArea = crossSectionRoundedRectArea(H1, R, centerW, H2, B)
        val fullVol  = fullArea * L

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= H1 + 1e-9) {
            val area = crossSectionRoundedRectArea(h, R, centerW, H2, B)
            val vol = area * L
            val levelCm = h * 100.0
            val percent = vol / fullVol * 100.0
            val volumeM3 = vol
            val massT = vol * density
            rows.add (TableRow(levelCm, percent, volumeM3, massT))
            h += step
        }
        return rows
    }

    private fun crossSectionRoundedRectArea(h: Double, R: Double, centerW: Double, H2: Double, B: Double,  slices: Int = 1000 ): Double {
        val dy = h / slices
        var s = 0.0
        for (i in 0 until slices) {
            val y = (i + 0.5) * dy
            val w = when {
                y <  R -> centerW + 2.0 * sqrt(R*R - (R - y)*(R - y))
                y <= R + H2 -> B
                else -> {
                    val y2 = y - (R + H2)
                    centerW + 2.0 * sqrt(R*R - y2*y2)
                }
            }
            s += w * dy
        }
        return s
    }


    /* ──────────────── Чемоданная емкость с плоскими днищами ──────────────── */
    fun generateSuitcaseTable(
        lengthMm: Double,
        height1Mm: Double,
        height2Mm: Double,
        widthMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val a = widthMm / 2.0 / 1000.0
        val b = (height1Mm - height2Mm) / 2.0 / 1000.0
        val H1 = height1Mm / 1000.0
        val H2 = height2Mm / 1000.0
        val widthM = widthMm / 1000.0
        val lengthM = lengthMm / 1000.0

        val halfEllipseArea = Math.PI * a * b / 2.0

        val fullCrossArea = halfEllipseArea * 2 + widthM * H2
        val fullVolume = fullCrossArea * lengthM

        val rows = mutableListOf<TableRow>()
        val stepM = stepMm / 1000.0
        var h = 0.0

        while (h <= H1 + 1e-9) {

            val areaM2: Double = when {
                h <= b -> {
                    halfEllipseSuitcaseSegmentArea(h, b, a)
                }

                h <= b + H2 -> {
                    halfEllipseArea + widthM * (h - b)
                }

                else -> {
                    val ht = h - (b + H2)
                    val topSegment = halfEllipseArea - halfEllipseSuitcaseSegmentArea(b - ht, b, a)
                    halfEllipseArea + widthM * H2 + topSegment
                }
            }

            val volume  = areaM2 * lengthM
            val mass = volume * density

            val levelCm = h * 100.0
            val percentage = volume / fullVolume * 100.0
            val volumeM3 = volume
            val massT = mass

            rows.add(TableRow(levelCm, percentage, volumeM3, massT))
            h += stepM
        }
        return rows
    }

    private fun halfEllipseSuitcaseSegmentArea(h: Double, b: Double, a: Double): Double {
        val y = (-b + h).coerceIn(-b, 0.0)
        val t = y / b
        return a * b * (asin(t) + Math.PI / 2.0 + t * sqrt(1 - t * t))
    }

    /* ──────────────── Вертикальная цилиндрическая ёмкость с плоскими днищами ──────────────── */
    fun generateVerticalCylindricalTable(
        heightMm: Double,
        diameterMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val radiusM = diameterMm / 2.0 / 1000.0
        val areaM2 = Math.PI * radiusM * radiusM
        val fullVol = areaM2 * heightMm / 1000.0

        val rows = mutableListOf<TableRow>()
        var hMm = 0.0

        while (hMm <= heightMm + 1e-9) {
            val volM3 = areaM2 * (hMm / 1000.0)
            val massT = volM3 * density
            val levelCm = hMm / 10.0
            val percent = volM3 / fullVol * 100.0

            rows.add(TableRow(levelCm, percent, volM3, massT))
            hMm += stepMm
        }
        return rows
    }

    /* ──────────────── Вертикальная цилиндрическая ёмкость с усечённо-конусным днищем ──────────────── */
    fun generateVerticalFrustumBottomTable(
        heightMm: Double,
        bigDiamMm: Double,
        smallDiamMm: Double,
        frustumMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val r1 = bigDiamMm / 2.0 / 1000.0
        val r2 = smallDiamMm / 2.0 / 1000.0
        val f = frustumMm / 1000.0
        val hC = (heightMm - frustumMm) / 1000.0
        val stepM = stepMm / 1000.0

        val frustumFullVol = verticalFrustumPartialVolume(f, r2, r1, f)
        val cylSectionArea = Math.PI * r1 * r1
        val fullVolume = frustumFullVol + cylSectionArea * hC

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        val H = hC + f

        while (h <= H + 1e-9) {

            val volM3 = if (h <= f)
                verticalFrustumPartialVolume(h, r2, r1, f)
            else
                frustumFullVol + cylSectionArea * (h - f)

            val massT = volM3 * density
            val levelCm = h * 100.0
            val percent = volM3 / fullVolume * 100.0

            rows.add(TableRow(levelCm, percent, volM3, massT))
            h += stepM
        }
        return rows
    }

    private fun radiusVerticalFrustumAt(t: Double, r2: Double, r1: Double, f: Double) = r2 + (r1 - r2) * (t / f)

    private fun verticalFrustumPartialVolume(t: Double, r2: Double, r1: Double, f: Double): Double {
        if (t <= 0.0) return 0.0
        val rt = radiusVerticalFrustumAt(t, r2, r1, f)
        return Math.PI * t / 3.0 * (r2 * r2 + r2 * rt + rt * rt)
    }

    /* ──────────────── Винная емкость вертикальная с плоскими днищами ──────────────── */
    fun generateWineBarrelTable(
        heightMm: Double,
        bigDiamMm: Double,
        smallDiamMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val H  = heightMm / 1000.0
        val r1 = bigDiamMm / 2.0 / 1000.0
        val r2 = smallDiamMm / 2.0 / 1000.0
        val deltaR  = r1 - r2
        val stepM = stepMm / 1000.0

        val fullVolume = volumeWineBarrelTo(H, H, r2, deltaR)

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= H + 1e-9) {

            val volM3 = volumeWineBarrelTo(h, H, r2, deltaR)
            val massT = volM3 * density
            val levelCm = h * 100.0
            val percent = volM3 / fullVolume * 100.0

            rows.add(TableRow(levelCm, percent, volM3, massT))
            h += stepM
        }
        return rows
    }

    private fun volumeWineBarrelTo(h: Double, H: Double, r2: Double, deltaR: Double): Double {
        val a = Math.PI * h / H
        val term1 = Math.PI * r2 * r2 * h
        val term2 = -2.0 * r2 * deltaR * H * (cos(a) - 1.0)
        val term3 = Math.PI * deltaR * deltaR * h / 2.0
        val term4 = -deltaR * deltaR * H * sin(2.0 * a) / 4.0
        return term1 + term2 + term3 + term4
    }

    /* ──────────────── Вертикальная прямоугольная ёмкость с усечённо-пирамидальным днищем ──────────────── */
    fun generateVerticalRectFrustumTable(
        heightMm: Double,
        bigAMm: Double,
        bigBMm: Double,
        smallAm: Double,
        smallBm: Double,
        frustumMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val A1 = bigAMm / 1000.0
        val B1 = bigBMm / 1000.0
        val a2 = smallAm / 1000.0
        val b2 = smallBm / 1000.0
        val f = frustumMm / 1000.0
        val hC = (heightMm - frustumMm) / 1000.0
        val step = stepMm / 1000.0
        val H = hC + f

        val p = (A1 - a2) / f
        val q = (B1 - b2) / f

        val frustFullVol = pyramidPartialVerticalRectFrustumVolume(f, a2, b2, q, p)
        val rectArea = A1 * B1
        val fullVolume = frustFullVol + rectArea * hC

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        while (h <= H + 1e-9) {

            val volM3 = if (h <= f)
                pyramidPartialVerticalRectFrustumVolume(h, a2, b2, q, p)
            else
                frustFullVol + rectArea * (h - f)

            val massT = volM3 * density
            val levelCm = h * 100.0
            val percent = volM3 / fullVolume * 100.0

            rows.add(TableRow(levelCm, percent, volM3, massT))
            h += step
        }
        return rows
    }

    private fun pyramidPartialVerticalRectFrustumVolume(t: Double, a2: Double, b2: Double, q: Double, p: Double): Double {
        if (t <= 0.0) return 0.0
        val t2 = t * t
        val t3 = t2 * t
        return a2 * b2 * t + (a2 * q + b2 * p) * t2 / 2.0 + p * q * t3 / 3.0
    }

    /* ──────────────── Сферическая ёмкость ──────────────── */
    fun generateSphereTable(
        diameterMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val R = diameterMm / 2.0 / 1000.0
        val stepM = stepMm / 1000.0
        val fullVol = 4.0 / 3.0 * Math.PI * R * R * R

        val rows = mutableListOf<TableRow>()
        var h = 0.0
        val H = 2 * R

        while (h <= H + 1e-9) {

            val volM3 = Math.PI * h * h * (3 * R - h) / 3.0
            val massT = volM3 * density

            val levelCm = h * 100.0
            val percent = volM3 / fullVol * 100.0

            rows.add(TableRow(levelCm, percent, volM3, massT))
            h += stepM
        }
        return rows
    }

    /* ──────────────── Вертикальное дуговое днище ──────────────── */
    fun generateSingleArcTankTable(
        diameterMm: Double,
        capHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val R = diameterMm / 2.0 / 1000.0
        val f = capHeightMm / 1000.0
        val H = 2 * R
        val stepM = stepMm / 1000.0
        val planeX = -R + f

        val fullVol = volumeUpTo(R, R, planeX)

        val rows = mutableListOf<TableRow>()
        var level = 0.0
        while (level <= H + 1e-9) {
            val z = -R + level
            val volM3 = volumeUpTo(z, R, planeX)
            val massT = volM3 * density
            rows += TableRow(
                levelCm = level * 100.0,
                percentage = volM3 / fullVol * 100.0,
                volumeM3 = volM3,
                massT = massT
            )
            level += stepM
        }
        return rows
    }

    private fun circleCapAreaLeft(r: Double, planeX: Double): Double = when {
        planeX <= -r -> 0.0
        planeX >= r -> Math.PI * r * r
        else -> {
            val d = planeX
            val areaRight = r * r * acos(d / r) - d * sqrt(r * r - d * d)
            Math.PI * r * r - areaRight
        }
    }

    private fun volumeUpTo(z: Double, R: Double, planeX: Double, slices: Int = 800): Double {
        val dz = (z + R) / slices
        var v = 0.0
        for (i in 0 until slices) {
            val yMid = -R + dz * (i + 0.5)
            val r = sqrt(R * R - yMid * yMid)
            v += circleCapAreaLeft(r, planeX) * dz
        }
        return v
    }

    /* ──────────────── Горизонтальная ёмкость одно коническое днище ──────────────── */
    fun generateSingleConeTankTable(
        diameterMm: Double,
        coneHeightMm: Double,
        stepMm: Double,
        density: Double
    ): List<TableRow> {

        val R = diameterMm / 2.0 / 1000.0
        val Hc = coneHeightMm / 1000.0
        val H = 2 * R
        val stepM = stepMm / 1000.0
        val fullVol = Math.PI * R * R * Hc / 3.0

        val rows = mutableListOf<TableRow>()
        var level = 0.0
        while (level <= H + 1e-9) {

            val volM3 = partialFrustumVolume(R,r2 = 0.0, Hc, level)
            val massT = volM3 * density
            val percent = volM3 / fullVol * 100.0

            rows += TableRow(levelCm = level * 100.0, percentage = percent, volumeM3 = volM3, massT = massT)
            level += stepM
        }
        return rows
    }

}
