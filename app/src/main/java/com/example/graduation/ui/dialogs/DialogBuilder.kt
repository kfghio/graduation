package com.example.graduation.ui.dialogs

import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduation.R
import com.example.graduation.models.Liquid
import com.example.graduation.utils.bindLiquids
import com.example.graduation.utils.showError
import com.example.graduation.utils.validateStep

typealias LaunchFn = (shape: String, extras: Array<Pair<String, Double>>) -> Unit

object InputDialogBuilder {

    /* ────────────────────────── Публичные функции для вызова из Activity ────────────────────────── */

    fun rectHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgRectHor(act, liquids, launch)

    fun cylHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgCylHor(act, liquids, launch)

    fun ellipEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgEllipEnds(act, liquids, launch)

    fun hemiEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgHemiEnds(act, liquids, launch)

    fun arcEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgArcEnds(act, liquids, launch)

    fun coneEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgConeEnds(act, liquids, launch)

    fun frustumEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgFrustumEnds(act, liquids, launch)

    fun ellipHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgEllipHor(act, liquids, launch)

    fun ellipDoubleTrunc(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgEllipDoubleTrunc(act, liquids, launch)

    fun roundedRect(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgRoundedRect(act, liquids, launch)

    fun suitcase(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgSuitcase(act, liquids, launch)

    fun cylVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgCylVert(act, liquids, launch)

    fun frustumVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgFrustumVert(act, liquids, launch)

    fun wineBarrel(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgWineBarrel(act, liquids, launch)

    fun rectFrustumVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgRectFrustumVert(act, liquids, launch)

    fun sphere(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgSphere(act, liquids, launch)

    fun singleArc(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgSingleArc(act, liquids, launch)

    fun singleCone(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) =
        dlgSingleCone(act, liquids, launch)

    private fun Spinner.prepare(
        liquids: List<Liquid>,
        act: AppCompatActivity
    ): Spinner = apply { bindLiquids(liquids, act) }

    private inline fun doLaunch(
        launch: LaunchFn,
        shape: String,
        vararg kv: Pair<String, Double>
    ) = launch(shape, arrayOf(*kv))

    private fun err(msg: String?, act: AppCompatActivity): Boolean {
        msg?.let { act.showError(it) }
        return msg != null
    }

    /* ────────────────────────── 1. Прямоугольная (гор.) ────────────────────────── */
    private fun dlgRectHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_rectangular_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)

        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(launch, "rectHor", "length" to L!!, "width" to B!!, "height" to H!!, "step" to st!!, "density" to liq.density)

            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /* ────────────────────────── 2. Цилиндр (гор.) ────────────────────────── */
    private fun dlgCylHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_cylindrical_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)

        AlertDialog.Builder(act)
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
                    err(error,act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgEllipEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_cylindrical_elliptical_input, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgHemiEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        commonThreeParamDialog(
            title = "Цилиндр + полусферические днища",
            layout = R.layout.dialog_himespherical_input,
            shapeId = "hemiEndsCyl",
            act,
            liquids,
            launch
        )
    }

    /* ────────────────────────── 5. Дуговые днища ────────────────────────── */
    private fun dlgArcEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        commonFourParamDialog(
            title = "Цилиндр + дуговые днища",
            layout = R.layout.dialog_arcs_input,
            shapeId = "arcEndsCyl",
            extra = "capH",
            act,
            liquids,
            launch
        )
    }

    /* ────────────────────────── 6. Конические днища ────────────────────────── */
    private fun dlgConeEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        commonFourParamDialog(
            title = "Цилиндр + конические днища",
            layout = R.layout.dialog_cons_input,
            shapeId = "coneEndsCyl",
            extra = "coneH",
            act,
            liquids,
            launch
        )
    }

    /* ────────────────────────── 7. Усечённо-конические днища ────────────────────────── */
    private fun dlgFrustumEnds(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_cylindrical_frustum_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_diameter)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgEllipHor(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_elliptical_horizontal_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etA = v.findViewById<EditText>(R.id.edit_text_axis)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgEllipDoubleTrunc(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_elliptical_truncated_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etA = v.findViewById<EditText>(R.id.edit_text_axis)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgRoundedRect(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_rounded_rect_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1 = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2 = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgSuitcase(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_suitcase_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1 = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2 = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgCylVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_vertical_cylindrical_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgFrustumVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_vertical_frustum_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etF = v.findViewById<EditText>(R.id.edit_text_frustum)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgWineBarrel(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_wine_barrel_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgRectFrustumVert(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_rect_frustum_input, null)

        fun EditText.d() = text.toString().toDoubleOrNull()

        val etH = v.findViewById<EditText>(R.id.et_height)
        val etA1 = v.findViewById<EditText>(R.id.et_bigA)
        val etB1 = v.findViewById<EditText>(R.id.et_bigB)
        val eta2 = v.findViewById<EditText>(R.id.et_smallA)
        val etb2 = v.findViewById<EditText>(R.id.et_smallB)
        val etF = v.findViewById<EditText>(R.id.et_frustum)
        val etSt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgSphere(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_sphere_input, null)

        val etD  = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgSingleArc(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_arc_bottom_input, null)

        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_cap_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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
    private fun dlgSingleCone(act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(R.layout.dialog_single_cone_input, null)

        val etD = v.findViewById<EditText>(R.id.et_diameter)
        val etF = v.findViewById<EditText>(R.id.et_cone_height)
        val etSt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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

    private fun commonThreeParamDialog(title: String, layout: Int, shapeId: String, act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(layout, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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

    private fun commonFourParamDialog(title: String, layout: Int, shapeId: String, extra: String, act: AppCompatActivity, liquids: List<Liquid>, launch: LaunchFn) {
        val v = act.layoutInflater.inflate(layout, null)
        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_head_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid).prepare(liquids, act)
        AlertDialog.Builder(act)
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
                    err(error, act)
                    return@setPositiveButton
                }

                doLaunch(
                    launch,
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