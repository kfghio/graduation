package com.example.graduation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_input, null)
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
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_elliptical_input, null)
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
                } else if (endHeight >= diameter / 2) {
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
    } // проверить

    private fun showConicalTankInputDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_cylindrical_elliptical_input, null)
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
    } //проверить

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
    } //проверить !!!

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


}