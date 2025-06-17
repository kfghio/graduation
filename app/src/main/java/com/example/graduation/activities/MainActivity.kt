package com.example.graduation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduation.R
import com.example.graduation.models.LiquidRepository
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
        flipper   = findViewById(R.id.view_flipper)

        findViewById<Button>(R.id.button_prev_section).setOnClickListener {
            prevSection()
        }
        findViewById<Button>(R.id.button_next_section).setOnClickListener {
            nextSection()
        }

        /* ────────────────────────── Горизонтальные ────────────────────────── */
        findViewById<Button>(R.id.button_rectangular_tank).setOnClickListener {
            dlgRectHor()
        }
        findViewById<Button>(R.id.button_cylindrical_tank).setOnClickListener {
            dlgCylHor()
        }
        findViewById<Button>(R.id.button_elliptical_tank).setOnClickListener {
            dlgEllipEnds()
        }
        findViewById<Button>(R.id.button_hemispherical_tank).setOnClickListener {
            dlgHemiEnds()
        }
        findViewById<Button>(R.id.button_arc_tank).setOnClickListener {
            dlgArcEnds()
        }
        findViewById<Button>(R.id.button_conical_tank).setOnClickListener {
            dlgConeEnds()
        }
        findViewById<Button>(R.id.button_frustrum_conical_tank).setOnClickListener {
            dlgFrustumEnds()
        }
        findViewById<Button>(R.id.button_elliptical_horizontal_tank).setOnClickListener {
            dlgEllipHor()
        }
        findViewById<Button>(R.id.button_elliptical_truncated_tank).setOnClickListener {
            dlgEllipDoubleTrunc()
        }
        findViewById<Button>(R.id.button_rounded_rect_tank).setOnClickListener {
            dlgRoundedRect()
        }
        findViewById<Button>(R.id.button_suitcase_tank).setOnClickListener {
            dlgSuitcase()
        }

        /* ────────────────────────── Вертикальные ────────────────────────── */
        findViewById<Button>(R.id.button_vertical_cylindrical_tank).setOnClickListener {
            dlgCylVert()
        }
        findViewById<Button>(R.id.button_vertical_frustum_tank).setOnClickListener {
            dlgFrustumVert()
        }
        findViewById<Button>(R.id.button_vertical_wine_tank).setOnClickListener {
            dlgWineBarrel()
        }
        findViewById<Button>(R.id.button_vertical_rect_frustum_tank).setOnClickListener {
            dlgRectFrustumVert()
        }
        findViewById<Button>(R.id.button_vertical_arc_bottom).setOnClickListener {
            dlgSingleArc()
        }

        /* ────────────────────────── Прочие ────────────────────────── */
        findViewById<Button>(R.id.button_spherical_tank).setOnClickListener {
            dlgSphere()
        }
        findViewById<Button>(R.id.button_single_cone_tank).setOnClickListener {
            dlgSingleCone()
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

    /* ────────────────────────── 1. Прямоугольная (гор.) ────────────────────────── */
    private fun dlgRectHor() {
        val v = layoutInflater.inflate(R.layout.dialog_rectangular_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Прямоугольная ёмкость (гор.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL .text.toString().toDoubleOrNull()
                val B = etB .text.toString().toDoubleOrNull()
                val H = etH .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,B,H,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > H!! -> "Шаг не должен превышать высоту"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable("rectHor", "length" to L!!, "width" to B!!, "height" to H!!, "step" to st!!, "density" to liq.density)

            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 2. Цилиндр (гор.) ────────────────────────── */
    private fun dlgCylHor() {
        val v = layoutInflater.inflate(R.layout.dialog_cylindrical_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Цилиндрическая ёмкость (гор.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL .text.toString().toDoubleOrNull()
                val D = etD .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,D,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > D!! -> "Шаг > D"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "cylHor",
                    "length" to L!!,
                    "diameter" to D!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 3. Цилиндр с эллиптическими днищами ────────────────────────── */
    private fun dlgEllipEnds() {
        val v = layoutInflater.inflate(R.layout.dialog_cylindrical_elliptical_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Цилиндр + эллиптические днища")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL.text.toString().toDoubleOrNull()
                val D = etD.text.toString().toDoubleOrNull()
                val f = etF.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,D,f,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > D!! -> "Шаг > D"
                    f!! > D / 2 -> "f > D / 2"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "ellipEndsCyl",
                    "length" to L!!,
                    "diameter" to D!!,
                    "headH" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 4. Полусферические днища ────────────────────────── */
    private fun dlgHemiEnds() {
        commonThreeParamDialog(
            title = "Цилиндр + полусферические днища",
            layout = R.layout.dialog_himespherical_input,
            shapeId = "hemiEndsCyl"
        )
    }

    /* ────────────────────────── 5. Дуговые днища ────────────────────────── */
    private fun dlgArcEnds() {
        commonFourParamDialog(
            title = "Цилиндр + дуговые днища",
            layout = R.layout.dialog_arcs_input,
            shapeId = "arcEndsCyl",
            extra = "capH"
        )
    }

    /* ────────────────────────── 6. Конические днища ────────────────────────── */
    private fun dlgConeEnds() {
        commonFourParamDialog(
            title = "Цилиндр + конические днища",
            layout = R.layout.dialog_cons_input,
            shapeId = "coneEndsCyl",
            extra = "coneH"
        )
    }

    /* ────────────────────────── 7. Усечённо-конические днища ────────────────────────── */
    private fun dlgFrustumEnds() {
        val v = layoutInflater.inflate(R.layout.dialog_cylindrical_frustum_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_diameter)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Цилиндр + усеч. конич. днища")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL .text.toString().toDoubleOrNull()
                val D1 = etD1.text.toString().toDoubleOrNull()
                val D2 = etD2.text.toString().toDoubleOrNull()
                val f = etF .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,D1,D2,f,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    D2!! >= D1!! -> "D2 >= D1"
                    st!! > D1 -> "Шаг > D1"
                    else -> validateStep(D1, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "frustumEndsCyl",
                    "length" to L!!,
                    "bigD" to D1!!,
                    "smallD" to D2!!,
                    "capH" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 8. Эллиптическая (гор.) ────────────────────────── */
    private fun dlgEllipHor() {
        val v = layoutInflater.inflate(R.layout.dialog_elliptical_horizontal_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etA = v.findViewById<EditText>(R.id.edit_text_axis)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Эллиптическая ёмкость (гор.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL.text.toString().toDoubleOrNull()
                val H = etH.text.toString().toDoubleOrNull()
                val a = etA.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,H,a,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > H!! -> "Шаг > H"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "ellipHor",
                    "length" to L!!,
                    "height" to H!!,
                    "axis" to a!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 9. Эллипс с двойным срезом ────────────────────────── */
    private fun dlgEllipDoubleTrunc() {
        val v = layoutInflater.inflate(R.layout.dialog_elliptical_truncated_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etA = v.findViewById<EditText>(R.id.edit_text_axis)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Эллиптическо-усечённая ёмкость (гор.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL.text.toString().toDoubleOrNull()
                val H = etH.text.toString().toDoubleOrNull()
                val B = etB.text.toString().toDoubleOrNull()
                val a = etA.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,H,B,a,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > H!! -> "Шаг > H"
                    B!! > a!! -> "B > a"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "ellipDoubleTrunc",
                    "length" to L!!,
                    "height" to H!!,
                    "width" to B!!,
                    "axis" to a!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 10. Прямоугольно-округлая ────────────────────────── */
    private fun dlgRoundedRect() {
        val v = layoutInflater.inflate(R.layout.dialog_rounded_rect_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1 = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2 = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Прямоугольно-округлая ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL .text.toString().toDoubleOrNull()
                val H1 = etH1.text.toString().toDoubleOrNull()
                val H2 = etH2.text.toString().toDoubleOrNull()
                val B = etB .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,H1,H2,B,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    H1!! <= H2!! -> "H1 ≤ H2"
                    B!! <= H1 - H2 -> "B ≤ H1 − H2"
                    st!! > H1 -> "Шаг > H1"
                    else -> validateStep(H1, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "roundedRect",
                    "length" to L!!,
                    "height1" to H1!!,
                    "height2" to H2!!,
                    "width" to B!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 11. Чемоданная ────────────────────────── */
    private fun dlgSuitcase() {
        val v = layoutInflater.inflate(R.layout.dialog_suitcase_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1 = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2 = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Чемоданная ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL .text.toString().toDoubleOrNull()
                val H1 = etH1.text.toString().toDoubleOrNull()
                val H2 = etH2.text.toString().toDoubleOrNull()
                val B = etB .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(L,H1,H2,B,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    H1!! <= H2!! -> "H1 ≤ H2"
                    st!! > H1 -> "Шаг > H1"
                    else -> validateStep(H1, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "suitcase",
                    "length" to L!!,
                    "height1" to H1!!,
                    "height2" to H2!!,
                    "width" to B!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 12. Вертикальный цилиндр ────────────────────────── */
    private fun dlgCylVert() {
        val v = layoutInflater.inflate(R.layout.dialog_vertical_cylindrical_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Цилиндрическая ёмкость (верт.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val H = etH .text.toString().toDoubleOrNull()
                val D = etD .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(H,D,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > H!! -> "Шаг > H"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "cylVert",
                    "height"  to H!!,
                    "diameter" to D!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 13. Вертикальная, усеч. конусное дно ────────────────────────── */
    private fun dlgFrustumVert() {
        val v = layoutInflater.inflate(R.layout.dialog_vertical_frustum_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etF = v.findViewById<EditText>(R.id.edit_text_frustum)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Верт. ёмкость и усеч. конусное дно")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val H = etH .text.toString().toDoubleOrNull()
                val D1 = etD1.text.toString().toDoubleOrNull()
                val D2 = etD2.text.toString().toDoubleOrNull()
                val f = etF .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(H,D1,D2,f,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    D2!! >= D1!! -> "D2 ≥ D1"
                    f!! >= H!! -> "f ≥ H"
                    st!! > H -> "Шаг > H"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "frustumBottomVert",
                    "height" to H!!,
                    "bigD" to D1!!,
                    "smallD" to D2!!,
                    "frustum" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 14. Винная бочка ────────────────────────── */
    private fun dlgWineBarrel() {
        val v = layoutInflater.inflate(R.layout.dialog_wine_barrel_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Винная ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val H = etH .text.toString().toDoubleOrNull()
                val D1 = etD1.text.toString().toDoubleOrNull()
                val D2 = etD2.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(H,D1,D2,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    D2!! >= D1!! -> "D2 ≥ D1"
                    st!! > H!! -> "Шаг > H"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "wineBarrel",
                    "height" to H!!,
                    "bigD" to D1!!,
                    "smallD" to D2!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 15. Прямоуг. + усеч. пирамид. дно ────────────────────────── */
    private fun dlgRectFrustumVert() {
        val v = layoutInflater.inflate(R.layout.dialog_rect_frustum_input, null)

        fun EditText.d() = text.toString().toDoubleOrNull()

        val etH = v.findViewById<EditText>(R.id.et_height)
        val etA1 = v.findViewById<EditText>(R.id.et_bigA)
        val etB1 = v.findViewById<EditText>(R.id.et_bigB)
        val eta2 = v.findViewById<EditText>(R.id.et_smallA)
        val etb2 = v.findViewById<EditText>(R.id.et_smallB)
        val etF = v.findViewById<EditText>(R.id.et_frustum)
        val etSt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Верт. прямоугольная + пирамид. дно")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val H = etH .d()
                val A1 = etA1.d()
                val B1 = etB1.d()
                val a2 = eta2.d()
                val b2 = etb2.d()
                val f = etF .d()
                val st = etSt.d()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    listOf(H,A1,B1,a2,b2,f,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    a2!! >= A1!! || b2!! >= B1!! -> "a2,b2 ≥ A1,B1"
                    f!! >= H!! -> "f ≥ H"
                    st!! > H -> "Шаг > H"
                    else -> validateStep(H, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "rectFrustumVert",
                    "height" to H!!,
                    "bigA" to A1!!,
                    "bigB" to B1!!,
                    "smallA" to a2!!,
                    "smallB" to b2!!,
                    "frustum" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 16. Сфера ────────────────────────── */
    private fun dlgSphere() {
        val v = layoutInflater.inflate(R.layout.dialog_sphere_input, null)

        val etD  = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Сферическая ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val D  = etD .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                val error: String? = when {
                    D == null || st == null || D <= 0 || st <= 0 -> "Введите корректные значения"
                    st > D -> "Шаг > D"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "sphere",
                    "diameter" to D!!,
                    "step" to st!!,
                    "density" to liq.density
                )

            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 17. Одно дуговое днище (верт.) ────────────────────────── */
    private fun dlgSingleArc() {
        val v = layoutInflater.inflate(R.layout.dialog_arc_bottom_input, null)

        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_cap_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Дуговое днище (верт.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val D  = etD .text.toString().toDoubleOrNull()
                val f = etF .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                val error: String? = when {
                    D==null||f==null||st==null||D<=0||f<=0||st<=0 -> "Введите корректные значения"
                    f > D / 2 -> "f > D / 2"
                    st > D -> "Шаг > D"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "singleArc",
                    "diameter" to D!!,
                    "capH" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 18. Одно конусное днище (гор.) ────────────────────────── */
    private fun dlgSingleCone() {
        val v = layoutInflater.inflate(R.layout.dialog_single_cone_input, null)

        val etD = v.findViewById<EditText>(R.id.et_diameter)
        val etF = v.findViewById<EditText>(R.id.et_cone_height)
        val etSt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle("Коническое днище (отдельно)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val D = etD .text.toString().toDoubleOrNull()
                val f = etF .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                val error: String? = when {
                    D==null||f==null||st==null||D<=0||f<=0||st<=0 -> "Введите корректные значения"
                    st > D -> "Шаг > D"
                    f > D / 2 -> "f > D / 2"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    "singleCone",
                    "diameter" to D!!,
                    "coneH" to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun commonThreeParamDialog(title: String, layout: Int, shapeId: String) {
        val v = layoutInflater.inflate(layout, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL?.text?.toString()?.toDoubleOrNull() ?: 0.0
                val D = etD .text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                val error: String? = when {
                    D == null || st == null || L<=0 || D<=0 || st<=0 -> "Введите корректные значения"
                    st > D -> "Шаг > D"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    shapeId,
                    "length" to L,
                    "diameter" to D!!,
                    "step" to st!!,
                    "density" to liq.density
                )

            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun commonFourParamDialog(title: String, layout: Int, shapeId: String, extra: String) {
        val v = layoutInflater.inflate(layout, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).apply { bindLiquids(liquids, this@MainActivity) }

        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->
                val L = etL.text.toString().toDoubleOrNull()
                val D = etD.text.toString().toDoubleOrNull()
                val f = etF.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]


                val error: String? = when {
                    listOf(L,D,f,st).any { it==null||it<=0 } -> "Введите корректные значения"
                    st!! > D!! -> "Шаг > D"
                    shapeId=="coneEndsCyl" && f!! > D / 2 -> "Высота конуса > D/2"
                    shapeId=="arcEndsCyl" && f!! > D / 2 -> "Высота дуги > D/2"
                    else -> validateStep(D, st)
                }

                if (error != null) {
                    showError(error)
                    return@setPositiveButton
                }

                launchTable(
                    shapeId,
                    "length" to L!!,
                    "diameter" to D!!,
                    extra to f!!,
                    "step" to st!!,
                    "density" to liq.density
                )

            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
