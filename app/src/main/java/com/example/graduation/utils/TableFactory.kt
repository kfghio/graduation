package com.example.graduation.utils

import android.content.Intent
import com.example.graduation.models.TableRow
object TableFactory {
    fun buildTableFromIntent(intent: Intent): List<TableRow> = when (intent.getStringExtra("shape")) {

        /* ────────────────────────── Горизонтальные ────────────────────────── */
        "rectHor" -> TankCalculator.generateRectangularTable(
            lengthMm = d(intent, "length"),
            widthMm = d(intent, "width"),
            heightMm = d(intent, "height"),
            stepMm = d(intent, "step"),
            density  = d(intent, "density")
        )

        "cylHor" -> TankCalculator.generateCylindricalTable(
            lengthMm = d(intent, "length"),
            diameterMm= d(intent, "diameter"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "ellipEndsCyl" -> TankCalculator.generateEllipticalEndedCylindricalTable(
            lengthMm = d(intent, "length"),
            diameterMm = d(intent, "diameter"),
            headHeightMm = d(intent, "headH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "hemiEndsCyl" -> TankCalculator.generateHemisphericalEndedCylindricalTable(
            lengthMm = d(intent, "length"),
            diameterMm = d(intent, "diameter"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "arcEndsCyl" -> TankCalculator.generateArcEndedCylindricalTable(
            lengthMm = d(intent, "length"),
            diameterMm = d(intent, "diameter"),
            endHeightMm = d(intent, "capH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "coneEndsCyl" -> TankCalculator.generateConicalEndedCylindricalTable(
            lengthMm = d(intent, "length"),
            diameterMm = d(intent, "diameter"),
            endHeightMm = d(intent, "coneH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "frustumEndsCyl" -> TankCalculator.generateFrustumEndedCylindricalTable(
            lengthMm = d(intent, "length"),
            bigDiamMm = d(intent, "bigD"),
            smallDiamMm = d(intent, "smallD"),
            endHeightMm = d(intent, "capH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "ellipHor" -> TankCalculator.generateEllipticalHorizontalTable(
            lengthMm = d(intent, "length"),
            heightMm = d(intent, "height"),
            axisMm = d(intent, "axis"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "ellipDoubleTrunc" -> TankCalculator.generateEllipticalDoubleTruncatedTable(
            lengthMm = d(intent, "length"),
            heightMm = d(intent, "height"),
            widthMm = d(intent, "width"),
            axisMm = d(intent, "axis"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "roundedRect" -> TankCalculator.generateRoundedRectTable(
            lengthMm = d(intent, "length"),
            height1Mm = d(intent, "height1"),
            height2Mm = d(intent, "height2"),
            widthMm = d(intent, "width"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "suitcase" -> TankCalculator.generateSuitcaseTable(
            lengthMm = d(intent, "length"),
            height1Mm = d(intent, "height1"),
            height2Mm = d(intent, "height2"),
            widthMm = d(intent, "width"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        /* ────────────────────────── Вертикальные ────────────────────────── */
        "cylVert" -> TankCalculator.generateVerticalCylindricalTable(
            heightMm = d(intent, "height"),
            diameterMm = d(intent, "diameter"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "frustumBottomVert" -> TankCalculator.generateVerticalFrustumBottomTable(
            heightMm = d(intent, "height"),
            bigDiamMm = d(intent, "bigD"),
            smallDiamMm = d(intent, "smallD"),
            frustumMm = d(intent, "frustum"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "wineBarrel" -> TankCalculator.generateWineBarrelTable(
            heightMm = d(intent, "height"),
            bigDiamMm = d(intent, "bigD"),
            smallDiamMm = d(intent, "smallD"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "rectFrustumVert" -> TankCalculator.generateVerticalRectFrustumTable(
            heightMm = d(intent, "height"),
            bigAMm = d(intent, "bigA"),
            bigBMm = d(intent, "bigB"),
            smallAm = d(intent, "smallA"),
            smallBm = d(intent, "smallB"),
            frustumMm = d(intent, "frustum"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        /* ────────────────────────── Прочие ────────────────────────── */
        "sphere" -> TankCalculator.generateSphereTable(
            diameterMm = d(intent, "diameter"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "singleArc" -> TankCalculator.generateSingleArcTankTable(
            diameterMm = d(intent, "diameter"),
            capHeightMm= d(intent, "capH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        "singleCone" -> TankCalculator.generateSingleConeTankTable(
            diameterMm = d(intent, "diameter"),
            coneHeightMm = d(intent, "coneH"),
            stepMm = d(intent, "step"),
            density = d(intent, "density")
        )

        else -> emptyList()
    }

    private fun d(intent: Intent, key: String) = intent.getDoubleExtra(key, 0.0)
}