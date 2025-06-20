package com.example.graduation.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.graduation.R
import com.example.graduation.models.LiquidRepository
import com.example.graduation.ui.dialogs.InputDialogBuilder
import com.example.graduation.utils.*

class MainActivity : AppCompatActivity() {

    private var currentSection = Section.HORIZONTAL
    private lateinit var titleView: TextView
    private lateinit var flipper: ViewFlipper
    private val liquids = LiquidRepository.list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleView = findViewById(R.id.text_section_title)
        flipper = findViewById(R.id.view_flipper)

        findViewById<Button>(R.id.button_prev_section).setOnClickListener {
            prevSection()
        }
        findViewById<Button>(R.id.button_next_section).setOnClickListener {
            nextSection()
        }

        /* ────────────────────────── Горизонтальные ────────────────────────── */
        findViewById<Button>(R.id.button_rectangular_tank).setOnClickListener {
            InputDialogBuilder.rectHor(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_cylindrical_tank).setOnClickListener {
            InputDialogBuilder.cylHor(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_elliptical_tank).setOnClickListener {
            InputDialogBuilder.ellipEnds(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_hemispherical_tank).setOnClickListener {
            InputDialogBuilder.hemiEnds(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_arc_tank).setOnClickListener {
            InputDialogBuilder.arcEnds(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_conical_tank).setOnClickListener {
            InputDialogBuilder.coneEnds(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_frustrum_conical_tank).setOnClickListener {
            InputDialogBuilder.frustumEnds(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_elliptical_horizontal_tank).setOnClickListener {
            InputDialogBuilder.ellipHor(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_elliptical_truncated_tank).setOnClickListener {
            InputDialogBuilder.ellipDoubleTrunc(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_rounded_rect_tank).setOnClickListener {
            InputDialogBuilder.roundedRect(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_suitcase_tank).setOnClickListener {
            InputDialogBuilder.suitcase(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }

        /* ────────────────────────── Вертикальные ────────────────────────── */
        findViewById<Button>(R.id.button_vertical_cylindrical_tank).setOnClickListener {
            InputDialogBuilder.cylVert(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_vertical_frustum_tank).setOnClickListener {
            InputDialogBuilder.frustumVert(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_vertical_wine_tank).setOnClickListener {
            InputDialogBuilder.wineBarrel(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_vertical_rect_frustum_tank).setOnClickListener {
            InputDialogBuilder.rectFrustumVert(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }

        /* ────────────────────────── Прочие ────────────────────────── */
        findViewById<Button>(R.id.button_spherical_tank).setOnClickListener {
            InputDialogBuilder.sphere(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }
        findViewById<Button>(R.id.button_single_cone_tank).setOnClickListener {
            InputDialogBuilder.singleCone(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }

        findViewById<Button>(R.id.button_vertical_arc_bottom).setOnClickListener {
            InputDialogBuilder.singleArc(
                act = this,
                liquids = liquids,
                launch = ::launchTable
            )
        }

        showSection()
    }

    private fun prevSection() {
        currentSection = currentSection.prev()
        showSection()
    }

    private fun nextSection() {
        currentSection = currentSection.next()
        showSection()
    }

    private fun showSection() {
        findViewById<TextView>(R.id.text_section_title).text = currentSection.title()
        findViewById<ViewFlipper>(R.id.view_flipper).displayedChild = currentSection.pageIndex()
    }

}