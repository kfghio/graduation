package com.example.graduation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduation.R
import com.example.graduation.adapters.TableAdapter
import com.example.graduation.models.TableRow
import com.example.graduation.utils.TankCalculator

class TableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)

        val table = buildTableFromIntent()

        findViewById<RecyclerView>(R.id.recycler_view_table).apply {
            layoutManager = LinearLayoutManager(this@TableActivity)
            adapter       = TableAdapter(table)
        }
    }

    private fun buildTableFromIntent(): List<TableRow> = when (intent.getStringExtra("shape")) {

        /* ────────────────────────── Горизонтальные ────────────────────────── */
        "rectHor" -> TankCalculator.generateRectangularTable(
            lengthMm = d("length"),
            widthMm = d("width"),
            heightMm = d("height"),
            stepMm = d("step"),
            density  = d("density")
        )

        "cylHor" -> TankCalculator.generateCylindricalTable(
            lengthMm = d("length"),
            diameterMm= d("diameter"),
            stepMm = d("step"),
            density = d("density")
        )

        "ellipEndsCyl" -> TankCalculator.generateEllipticalEndedCylindricalTable(
            lengthMm = d("length"),
            diameterMm = d("diameter"),
            headHeightMm = d("headH"),
            stepMm = d("step"),
            density = d("density")
        )

        "hemiEndsCyl" -> TankCalculator.generateHemisphericalEndedCylindricalTable(
            lengthMm = d("length"),
            diameterMm = d("diameter"),
            stepMm = d("step"),
            density = d("density")
        )

        "arcEndsCyl" -> TankCalculator.generateArcEndedCylindricalTable(
            lengthMm = d("length"),
            diameterMm = d("diameter"),
            endHeightMm = d("capH"),
            stepMm = d("step"),
            density = d("density")
        )

        "coneEndsCyl" -> TankCalculator.generateConicalEndedCylindricalTable(
            lengthMm = d("length"),
            diameterMm = d("diameter"),
            endHeightMm = d("coneH"),
            stepMm = d("step"),
            density = d("density")
        )

        "frustumEndsCyl" -> TankCalculator.generateFrustumEndedCylindricalTable(
            lengthMm = d("length"),
            bigDiamMm = d("bigD"),
            smallDiamMm = d("smallD"),
            endHeightMm = d("capH"),
            stepMm = d("step"),
            density = d("density")
        )

        "ellipHor" -> TankCalculator.generateEllipticalHorizontalTable(
            lengthMm = d("length"),
            heightMm = d("height"),
            axisMm = d("axis"),
            stepMm = d("step"),
            density = d("density")
        )

        "ellipDoubleTrunc" -> TankCalculator.generateEllipticalDoubleTruncatedTable(
            lengthMm = d("length"),
            heightMm = d("height"),
            widthMm = d("width"),
            axisMm = d("axis"),
            stepMm = d("step"),
            density = d("density")
        )

        "roundedRect" -> TankCalculator.generateRoundedRectTable(
            lengthMm = d("length"),
            height1Mm = d("height1"),
            height2Mm = d("height2"),
            widthMm = d("width"),
            stepMm = d("step"),
            density = d("density")
        )

        "suitcase" -> TankCalculator.generateSuitcaseTable(
            lengthMm = d("length"),
            height1Mm = d("height1"),
            height2Mm = d("height2"),
            widthMm = d("width"),
            stepMm = d("step"),
            density = d("density")
        )

        /* ────────────────────────── Вертикальные ────────────────────────── */
        "cylVert" -> TankCalculator.generateVerticalCylindricalTable(
            heightMm = d("height"),
            diameterMm = d("diameter"),
            stepMm = d("step"),
            density = d("density")
        )

        "frustumBottomVert" -> TankCalculator.generateVerticalFrustumBottomTable(
            heightMm = d("height"),
            bigDiamMm = d("bigD"),
            smallDiamMm = d("smallD"),
            frustumMm = d("frustum"),
            stepMm = d("step"),
            density = d("density")
        )

        "wineBarrel" -> TankCalculator.generateWineBarrelTable(
            heightMm = d("height"),
            bigDiamMm = d("bigD"),
            smallDiamMm = d("smallD"),
            stepMm = d("step"),
            density = d("density")
        )

        "rectFrustumVert" -> TankCalculator.generateVerticalRectFrustumTable(
            heightMm = d("height"),
            bigAMm = d("bigA"),
            bigBMm = d("bigB"),
            smallAm = d("smallA"),
            smallBm = d("smallB"),
            frustumMm = d("frustum"),
            stepMm = d("step"),
            density = d("density")
        )

        /* ────────────────────────── Прочие ────────────────────────── */
        "sphere" -> TankCalculator.generateSphereTable(
            diameterMm = d("diameter"),
            stepMm = d("step"),
            density = d("density")
        )

        "singleArc" -> TankCalculator.generateSingleArcTankTable(
            diameterMm = d("diameter"),
            capHeightMm= d("capH"),
            stepMm = d("step"),
            density = d("density")
        )

        "singleCone" -> TankCalculator.generateSingleConeTankTable(
            diameterMm = d("diameter"),
            coneHeightMm = d("coneH"),
            stepMm = d("step"),
            density = d("density")
        )

        else -> emptyList()
    }

    private fun d(key: String) = intent.getDoubleExtra(key, 0.0)
}
