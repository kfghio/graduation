package com.example.graduation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduation.R
import com.example.graduation.models.Liquid
import com.example.graduation.utils.TankCalculator

class MainActivity : AppCompatActivity() {
    private val liquids = listOf(
        Liquid("Вода", 1.0000),
        Liquid("Керосин", 0.8201),
        Liquid("Бензин", 0.7370),
        Liquid("Этиловый спирт", 0.7910),
        Liquid("Серная кислота 95% концентрации", 1.8390),
        Liquid("Дизельное топливо", 0.8200),
        Liquid("Мазут", 0.8900),
        Liquid("Сырая нефть", 0.8730),
        Liquid("СПГ", 0.4500)

    )

    private enum class Section { HORIZONTAL, VERTICAL, OTHER }
    private var currentSection = Section.HORIZONTAL

    private lateinit var titleView: TextView
    private lateinit var flipper: ViewFlipper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleView = findViewById(R.id.text_section_title)
        flipper   = findViewById(R.id.view_flipper)

        findViewById<Button>(R.id.button_prev_section).setOnClickListener {
            currentSection = when (currentSection) {
                Section.HORIZONTAL -> Section.OTHER
                Section.VERTICAL -> Section.HORIZONTAL
                Section.OTHER -> Section.VERTICAL
            }
            showSection()
        }

        findViewById<Button>(R.id.button_next_section).setOnClickListener {
            currentSection = when (currentSection) {
                Section.HORIZONTAL -> Section.VERTICAL
                Section.VERTICAL -> Section.OTHER
                Section.OTHER -> Section.HORIZONTAL
            }
            showSection()
        }

        val buttonRectangularTank = findViewById<Button>(R.id.button_rectangular_tank)
        buttonRectangularTank.setOnClickListener {
            showRectangularInputDialog()
        }

        val buttonCylindricalTank = findViewById<Button>(R.id.button_cylindrical_tank)
        buttonCylindricalTank.setOnClickListener {
            showCylindricalInputDialog()
        }

        val buttonEllipticalTank = findViewById<Button>(R.id.button_elliptical_tank)
        buttonEllipticalTank.setOnClickListener {
            showEllipticalInputDialog()
        }

        val buttonHemisphericalTank = findViewById<Button>(R.id.button_hemispherical_tank)
        buttonHemisphericalTank.setOnClickListener {
            showHemisphericalInputDialog()
        }

        val buttonArcTank = findViewById<Button>(R.id.button_arc_tank)
        buttonArcTank.setOnClickListener {
            showArcTankInputDialog()
        }

        val buttonConicalTank = findViewById<Button>(R.id.button_conical_tank)
        buttonConicalTank.setOnClickListener {
            showConicalTankInputDialog()
        }

        val buttonFrustumTank = findViewById<Button>(R.id.button_frustrum_conical_tank)
        buttonFrustumTank.setOnClickListener {
            showFrustumTankInputDialog()
        }

        val buttonEllipticalHorizontalTank = findViewById<Button>(R.id.button_elliptical_horizontal_tank)
        buttonEllipticalHorizontalTank.setOnClickListener {
            showEllipticalHorizontalInputDialog()
        }

        val buttonEllipticalTruncatedTank = findViewById<Button>(R.id.button_elliptical_truncated_tank)
        buttonEllipticalTruncatedTank.setOnClickListener {
            showEllipticalTruncatedInputDialog()
        }

        val buttonRoundedRectTank = findViewById<Button>(R.id.button_rounded_rect_tank)
        buttonRoundedRectTank.setOnClickListener {
            showRoundedRectInputDialog()
        }

        val buttonSuitcaseTank = findViewById<Button>(R.id.button_suitcase_tank)
        buttonSuitcaseTank.setOnClickListener {
            showSuitcaseInputDialog()
        }

        val buttonVerticalCylindricalTank = findViewById<Button>(R.id.button_vertical_cylindrical_tank)
        buttonVerticalCylindricalTank.setOnClickListener {
            showVerticalCylindricalInputDialog()
        }

        val buttonVerticalFrustumTank = findViewById<Button>(R.id.button_vertical_frustum_tank)
        buttonVerticalFrustumTank.setOnClickListener {
            showVerticalFrustumInputDialog()
        }

        val buttonVerticalWineTank = findViewById<Button>(R.id.button_vertical_wine_tank)
        buttonVerticalWineTank.setOnClickListener {
            showWineBarrelInputDialog()
        }

        val buttonVerticalRectFrustumTank = findViewById<Button>(R.id.button_vertical_rect_frustum_tank)
        buttonVerticalRectFrustumTank.setOnClickListener {
            showRectFrustumInputDialog()
        }

        val buttonSphericalTank = findViewById<Button>(R.id.button_spherical_tank)
        buttonSphericalTank.setOnClickListener {
            showSphereInputDialog()
        }

        val buttonVerticalArcTank = findViewById<Button>(R.id.button_vertical_arc_bottom)
        buttonVerticalArcTank.setOnClickListener {
            showArcBottomInputDialog()
        }

        val buttonSingleConeTank = findViewById<Button>(R.id.button_single_cone_tank)
        buttonSingleConeTank.setOnClickListener {
            showSingleConeDialog()
        }

        showSection()
    }

    private fun showSection() {
        when (currentSection) {
            Section.HORIZONTAL -> {
                titleView.text = "Горизонтальные ёмкости"
                flipper.displayedChild = 0
            }
            Section.VERTICAL -> {
                titleView.text = "Вертикальные ёмкости"
                flipper.displayedChild = 1
            }
            Section.OTHER -> {
                titleView.text = "Прочие"
                flipper.displayedChild = 2
            }
        }
    }

    private fun showError(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun showCylindricalInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextDiameter = dialogView.findViewById<EditText>(R.id.edit_text_diameter)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Ввод данных для цилиндрической ёмкости")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val diameter = editTextDiameter.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || diameter == null || step == null || length <= 0 || diameter <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > diameter) {
                    showError("Шаг не должен превышать диаметр ($diameter мм)")
                } else {
                    val tableData = TankCalculator.generateCylindricalTable(length, diameter, step, selectedLiquid.density)
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEllipticalInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_elliptical_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextDiameter = dialogView.findViewById<EditText>(R.id.edit_text_diameter)
        val editTextHeadHeight = dialogView.findViewById<EditText>(R.id.edit_text_head_height)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Цилиндр с эллиптическими днищами")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val diameter = editTextDiameter.text.toString().toDoubleOrNull()
                val headHeight = editTextHeadHeight.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val liquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || diameter == null || headHeight == null || step == null
                    || length <= 0 || diameter <= 0 || headHeight <= 0 || step <= 0) {
                    showError("Введите положительные числа")
                } else if (step > diameter) {
                    showError("Шаг не должен превышать диаметр ($diameter мм)")
                } else if (headHeight > diameter / 2) {
                    showError("Высота эллиптического днища не может превышать радиус (≤ ${diameter / 2} мм)")
                } else {
                    val tableData = TankCalculator.generateEllipticalEndedCylindricalTable(
                        lengthMm = length,
                        diameterMm = diameter,
                        headHeightMm = headHeight,
                        stepMm = step,
                        density = liquid.density
                    )
                    val intent = Intent(this, TableActivity::class.java).apply {
                        putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    }
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    } // проверить

    private fun showHemisphericalInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_himespherical_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextDiameter = dialogView.findViewById<EditText>(R.id.edit_text_diameter)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Горизонтальная ёмкость с полусферическими днищами")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val diameter = editTextDiameter.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || diameter == null || step == null || length <= 0 || diameter <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > diameter) {
                    showError("Шаг не должен превышать диаметр ($diameter мм)")
                } else {
                    val tableData = TankCalculator.generateHemisphericalEndedCylindricalTable(length, diameter, step, selectedLiquid.density)
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    } // проверить

    private fun showArcTankInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_arcs_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextDiameter = dialogView.findViewById<EditText>(R.id.edit_text_diameter)
        val editTextEndHeight = dialogView.findViewById<EditText>(R.id.edit_text_head_height)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Горизонтальная ёмкость с дуговыми днищами")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val diameter = editTextDiameter.text.toString().toDoubleOrNull()
                val endHeight = editTextEndHeight.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || diameter == null || endHeight == null || step == null ||
                    length <= 0 || diameter <= 0 || endHeight <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (endHeight > diameter / 2) {
                    showError("Высота дугового днища должна быть < радиуса (≤ ${diameter / 2} мм)")
                } else if (step > diameter) {
                    showError("Шаг не должен превышать диаметр ($diameter мм)")
                } else {
                    val tableData = TankCalculator.generateArcEndedCylindricalTable(length, diameter, endHeight, step, selectedLiquid.density)
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showConicalTankInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cons_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextDiameter = dialogView.findViewById<EditText>(R.id.edit_text_diameter)
        val editTextEndHeight = dialogView.findViewById<EditText>(R.id.edit_text_head_height)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Горизонтальная ёмкость с коническими днищами")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val diameter = editTextDiameter.text.toString().toDoubleOrNull()
                val endHeight = editTextEndHeight.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || diameter == null || endHeight == null || step == null ||
                    length <= 0 || diameter <= 0 || endHeight <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                }else if (step > diameter) {
                    showError("Шаг не должен превышать диаметр ($diameter мм)")
                } else if (endHeight > diameter / 2) {
                    showError("Высота конуса не может быть больше радиуса (≤ ${diameter / 2} мм)")
                } else {
                    val tableData = TankCalculator.generateConicalEndedCylindricalTable(length, diameter, endHeight, step, selectedLiquid.density)
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showFrustumTankInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_frustum_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextBigD = dialogView.findViewById<EditText>(R.id.edit_text_big_diameter)
        val editTextSmallD = dialogView.findViewById<EditText>(R.id.edit_text_small_diameter)
        val editTextHeight = dialogView.findViewById<EditText>(R.id.edit_text_head_height)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        spinnerLiquid.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Горизонтальная ёмкость с усечённо-конусными днищами")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->

                val length = editTextLength.text.toString().toDoubleOrNull()
                val d1 = editTextBigD.text.toString().toDoubleOrNull()
                val d2 = editTextSmallD.text.toString().toDoubleOrNull()
                val hEnd = editTextHeight.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val liquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || d1 == null || d2 == null || hEnd == null || step == null) {
                    showError("Заполните все поля")
                } else if (length <= 0 || d1 <= 0 || d2 <= 0 || hEnd <= 0 || step <= 0) {
                    showError("Числа должны быть > 0")
                } else if (d2 >= d1) {
                    showError("d2 должно быть меньше d1")
                } else if (step > d1) {
                    showError("Шаг не должен превышать d1 ($d1 мм)")
                } else {
                    val data = TankCalculator.generateFrustumEndedCylindricalTable(
                        length, d1, d2, hEnd, step, liquid.density
                    )
                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(data))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    } //проверить

    private fun showRectangularInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rectangular_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextWidth = dialogView.findViewById<EditText>(R.id.edit_text_width)
        val editTextHeight = dialogView.findViewById<EditText>(R.id.edit_text_height)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Ввод данных для прямоугольной цистерны")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val width = editTextWidth.text.toString().toDoubleOrNull()
                val height = editTextHeight.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]



                if (length == null || width == null || height == null || step == null || length <= 0 || width <= 0 || height <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > height) {
                    showError("Шаг не должен превышать высоту ($height мм)")
                } else {

                    val tableData = TankCalculator.generateRectangularTable(length, width, height, step, selectedLiquid.density)
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEllipticalHorizontalInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_elliptical_horizontal_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextHeight = dialogView.findViewById<EditText>(R.id.edit_text_height)
        val editTextAxis = dialogView.findViewById<EditText>(R.id.edit_text_axis)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Ввод данных для эллиптической горизонтальной ёмкости")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val height = editTextHeight.text.toString().toDoubleOrNull()
                val axis = editTextAxis.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || height == null || axis == null || step == null ||
                    length <= 0 || height <= 0 || axis <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > height) {
                    showError("Шаг не должен превышать высоту ($height мм)")
                } else {
                    val tableData = TankCalculator.generateEllipticalHorizontalTable(
                        length, height, axis, step, selectedLiquid.density
                    )
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showEllipticalTruncatedInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_elliptical_truncated_input, null)
        val editTextLength = dialogView.findViewById<EditText>(R.id.edit_text_length)
        val editTextHeight = dialogView.findViewById<EditText>(R.id.edit_text_height)
        val editTextWidth = dialogView.findViewById<EditText>(R.id.edit_text_width)
        val editTextAxis = dialogView.findViewById<EditText>(R.id.edit_text_axis)
        val editTextStep = dialogView.findViewById<EditText>(R.id.edit_text_step)
        val spinnerLiquid = dialogView.findViewById<Spinner>(R.id.spinner_liquid)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, liquids.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLiquid.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Эллиптическо-усеченная горизонтальная ёмкость")
            .setView(dialogView)
            .setPositiveButton("Рассчитать") { _, _ ->
                val length = editTextLength.text.toString().toDoubleOrNull()
                val height = editTextHeight.text.toString().toDoubleOrNull()
                val width = editTextWidth.text.toString().toDoubleOrNull()
                val axis = editTextAxis.text.toString().toDoubleOrNull()
                val step = editTextStep.text.toString().toDoubleOrNull()
                val selectedLiquid = liquids[spinnerLiquid.selectedItemPosition]

                if (length == null || height == null || width == null || axis == null || step == null ||
                    length <= 0 || height <= 0 || width <= 0 || axis <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > height) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($height мм)", Toast.LENGTH_SHORT).show()
                } else if (width > axis) {
                    Toast.makeText(this, "Ширина сечения В не должна превышать ось а", Toast.LENGTH_SHORT).show()
                } else {
                    val tableData = TankCalculator.generateEllipticalDoubleTruncatedTable(
                        length, height, width, axis, step, selectedLiquid.density
                    )
                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    } // проверить

    private fun showRoundedRectInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_rounded_rect_input, null)

        val etL = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1 = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2 = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Прямоугольно-округлая ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val length = etL.text.toString().toDoubleOrNull()
                val h1 = etH1.text.toString().toDoubleOrNull()
                val h2 = etH2.text.toString().toDoubleOrNull()
                val width = etB.text.toString().toDoubleOrNull()
                val step = etSt.text.toString().toDoubleOrNull()
                val liquid = liquids[sp.selectedItemPosition]

                if (length == null || h1 == null || h2 == null || width == null || step == null ||
                    length <= 0 || h1 <= 0 || h2 <= 0 || width <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (h1 <= h2) {
                    Toast.makeText(this, "H1 должно быть больше H2", Toast.LENGTH_SHORT).show()
                } else if (width <= h1 - h2) {
                    Toast.makeText(this, "B должно быть больше (H1 − H2)", Toast.LENGTH_SHORT).show()
                } else if (step > h1) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($h1 мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val tableData = TankCalculator.generateRoundedRectTable(lengthMm  = length, height1Mm = h1, height2Mm = h2, widthMm   = width, stepMm    = step, density   = liquid.density)

                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showSuitcaseInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_suitcase_input, null)

        val etL   = v.findViewById<EditText>(R.id.edit_text_length)
        val etH1  = v.findViewById<EditText>(R.id.edit_text_height1)
        val etH2  = v.findViewById<EditText>(R.id.edit_text_height2)
        val etB   = v.findViewById<EditText>(R.id.edit_text_width)
        val etSt  = v.findViewById<EditText>(R.id.edit_text_step)
        val sp    = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Чемоданная ёмкость (плоские днища)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val length = etL.text.toString().toDoubleOrNull()
                val h1 = etH1.text.toString().toDoubleOrNull()
                val h2 = etH2.text.toString().toDoubleOrNull()
                val width = etB.text.toString().toDoubleOrNull()
                val step = etSt.text.toString().toDoubleOrNull()
                val liquid = liquids[sp.selectedItemPosition]

                if (length == null || h1 == null || h2 == null || width == null || step == null ||
                    length <= 0 || h1 <= 0 || h2 <= 0 || width <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (h1 <= h2) {
                    Toast.makeText(this, "H1 должно быть больше H2", Toast.LENGTH_SHORT).show()
                } else if (step > h1) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($h1 мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val tableData = TankCalculator.generateSuitcaseTable(lengthMm  = length, height1Mm = h1, height2Mm = h2, widthMm   = width, stepMm    = step, density   = liquid.density)

                    val intent = Intent(this, TableActivity::class.java)
                    intent.putParcelableArrayListExtra("table_data", ArrayList(tableData))
                    startActivity(intent)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showVerticalCylindricalInputDialog() {
        val dialog = layoutInflater.inflate(R.layout.dialog_vertical_cylindrical_input, null)

        val etH = dialog.findViewById<EditText>(R.id.edit_text_height)
        val etD = dialog.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = dialog.findViewById<EditText>(R.id.edit_text_step)
        val sp = dialog.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Вертикальная цилиндрическая ёмкость")
            .setView(dialog)
            .setPositiveButton("Рассчитать") { _, _ ->

                val height = etH.text.toString().toDoubleOrNull()
                val diameter = etD.text.toString().toDoubleOrNull()
                val step = etSt.text.toString().toDoubleOrNull()
                val liquid = liquids[sp.selectedItemPosition]

                if (height == null || diameter == null || step == null ||
                    height <= 0 || diameter <= 0 || step <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (step > height) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($height мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val data = TankCalculator.generateVerticalCylindricalTable(heightMm = height, diameterMm = diameter, stepMm = step, density = liquid.density)

                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(data))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showVerticalFrustumInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_vertical_frustum_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etF = v.findViewById<EditText>(R.id.edit_text_frustum)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Вертикальная ёмкость с усечённо-конусным днищем")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val H = etH.text.toString().toDoubleOrNull()
                val D1 = etD1.text.toString().toDoubleOrNull()
                val D2 = etD2.text.toString().toDoubleOrNull()
                val f = etF.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                if (H == null || D1 == null || D2 == null || f == null || st == null || H <= 0 || D1 <= 0 || D2 <= 0 || f <= 0 || st <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (D2 >= D1) {
                    Toast.makeText(this, "D2 должно быть меньше D1", Toast.LENGTH_SHORT).show()
                } else if (f >= H) {
                    Toast.makeText(this, "f должно быть меньше общей высоты H", Toast.LENGTH_SHORT).show()
                } else if (st > H) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($H мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val table = TankCalculator.generateVerticalFrustumBottomTable(heightMm = H, bigDiamMm = D1, smallDiamMm = D2, frustumMm = f, stepMm = st, density = liq.density
                    )
                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showWineBarrelInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_wine_barrel_input, null)

        val etH = v.findViewById<EditText>(R.id.edit_text_height)
        val etD1 = v.findViewById<EditText>(R.id.edit_text_big_d)
        val etD2 = v.findViewById<EditText>(R.id.edit_text_small_d)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Винная ёмкость (вертик.)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val H = etH.text.toString().toDoubleOrNull()
                val D1 = etD1.text.toString().toDoubleOrNull()
                val D2 = etD2.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                if (H == null || D1 == null || D2 == null || st == null ||
                    H <= 0 || D1 <= 0 || D2 <= 0 || st <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (D2 >= D1) {
                    Toast.makeText(this, "D2 должно быть меньше D1", Toast.LENGTH_SHORT).show()
                } else if (st > H) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($H мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val table = TankCalculator.generateWineBarrelTable(heightMm = H, bigDiamMm = D1, smallDiamMm = D2, stepMm = st, density = liq.density)

                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showRectFrustumInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_rect_frustum_input, null)

        fun EditText.d() = text.toString().toDoubleOrNull()

        val H = v.findViewById<EditText>(R.id.et_height)
        val A1 = v.findViewById<EditText>(R.id.et_bigA)
        val B1 = v.findViewById<EditText>(R.id.et_bigB)
        val a2 = v.findViewById<EditText>(R.id.et_smallA)
        val b2 = v.findViewById<EditText>(R.id.et_smallB)
        val fEt = v.findViewById<EditText>(R.id.et_frustum)
        val stepEt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Прямоугольная ёмкость (усеч. пирамидальное дно)")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val h = H.d()
                val A = A1.d()
                val B = B1.d()
                val a = a2.d()
                val b = b2.d()
                val f = fEt.d()
                val st = stepEt.d()
                val liq = liquids[sp.selectedItemPosition]

                if (listOf(h, A, B, a, b, f, st).any { it == null || it <= 0 }) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (a!! >= A!! || b!! >= B!!) {
                    Toast.makeText(this, "a2 и b2 должны быть меньше A1 и B1", Toast.LENGTH_SHORT).show()
                } else if (f!! >= h!!) {
                    Toast.makeText(this, "f должно быть меньше H", Toast.LENGTH_SHORT).show()
                } else if (st!! > h) {
                    Toast.makeText(this, "Шаг не должен превышать высоту ($h мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val table = TankCalculator.generateVerticalRectFrustumTable(heightMm = h, bigAMm = A, bigBMm = B, smallAm = a, smallBm = b, frustumMm = f, stepMm = st, density = liq.density
                    )
                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showSphereInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_sphere_input, null)

        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Сферическая ёмкость")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val D = etD.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq = liquids[sp.selectedItemPosition]

                if (D == null || st == null || D <= 0 || st <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (st > D) {
                    Toast.makeText(this, "Шаг не должен превышать диаметр ($D мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val table = TankCalculator.generateSphereTable(diameterMm = D, stepMm = st, density = liq.density)

                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showArcBottomInputDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_arc_bottom_input, null)

        val etD = v.findViewById<EditText>(R.id.edit_text_diameter)
        val etF = v.findViewById<EditText>(R.id.edit_text_cap_height)
        val etSt = v.findViewById<EditText>(R.id.edit_text_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Вертикальное дуговое днище")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val D = etD.text.toString().toDoubleOrNull()
                val f = etF.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liquid = liquids[sp.selectedItemPosition]

                if (D == null || f == null || st == null || D <= 0 || f <= 0 || st <= 0) {
                    Toast.makeText(this, "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                } else if (f > D / 2) {
                    Toast.makeText(this, "f не может быть больше D / 2", Toast.LENGTH_SHORT).show()
                } else if (st > D) {
                    Toast.makeText(this, "Шаг не должен превышать диаметр сегмента ($D мм)", Toast.LENGTH_SHORT).show()
                } else {
                    val table = TankCalculator.generateSingleArcTankTable(diameterMm = D, capHeightMm = f, stepMm = st, density = liquid.density)

                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showSingleConeDialog() {
        val v = layoutInflater.inflate(R.layout.dialog_single_cone_input, null)

        val etD = v.findViewById<EditText>(R.id.et_diameter)
        val etF = v.findViewById<EditText>(R.id.et_cone_height)
        val etSt = v.findViewById<EditText>(R.id.et_step)
        val sp = v.findViewById<Spinner>(R.id.spinner_liquid)

        sp.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, liquids.map { it.name }
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        AlertDialog.Builder(this)
            .setTitle("Коническое днище")
            .setView(v)
            .setPositiveButton("Рассчитать") { _, _ ->

                val D  = etD.text.toString().toDoubleOrNull()
                val f  = etF.text.toString().toDoubleOrNull()
                val st = etSt.text.toString().toDoubleOrNull()
                val liq= liquids[sp.selectedItemPosition]

                if (D == null || f == null || st == null ||
                    D <= 0 || f <= 0 || st <= 0) {
                    showError("Введите корректные положительные значения")
                } else if (f > D / 2) {
                    showError("f не может быть больше D / 2")
                } else if (st > D) {
                    showError("Шаг не должен превышать высоту D")
                } else {
                    val table = TankCalculator.generateSingleConeTankTable(diameterMm = D, coneHeightMm = f, stepMm = st, density = liq.density)

                    startActivity(
                        Intent(this, TableActivity::class.java).apply {
                            putParcelableArrayListExtra("table_data", ArrayList(table))
                        }
                    )
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


}