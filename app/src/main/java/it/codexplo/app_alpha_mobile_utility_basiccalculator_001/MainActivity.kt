package it.codexplo.app_alpha_mobile_utility_basiccalculator_001

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var currentInput: String = ""
    private var operator: String? = null
    private var operand1: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Remove a ActionBar
        supportActionBar?.hide()

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#121212"))
            setPadding(16, 16, 16, 16)
        }

        // Display
        display = TextView(this).apply {
            text = "0"
            textSize = 36f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.parseColor("#1E1E1E"))
            setPadding(24, 24, 24, 24)
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        mainLayout.addView(display, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            1f
        ))

        // Grid de botões
        val buttonLayout = GridLayout(this).apply {
            rowCount = 5
            columnCount = 4
            setBackgroundColor(Color.parseColor("#121212"))
        }

        val buttons = arrayOf(
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C"
        )

        for (text in buttons) {
            val button = createButton(text)
            button.setOnClickListener { onButtonClick(text) }

            // LayoutParams para distribuir os botões igualmente
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8) // Espaçamento
            }
            button.layoutParams = params

            buttonLayout.addView(button)
        }

        mainLayout.addView(buttonLayout, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0,
            4f
        ))

        setContentView(mainLayout)
    }

    private fun createButton(text: String): Button {
        return Button(this).apply {
            this.text = text
            textSize = 24f
            setTextColor(Color.WHITE)
            setPadding(32, 32, 32, 32)
            background = createButtonBackground(Color.parseColor("#333333"))
            stateListAnimator = null // remove efeito de elevação
        }
    }

    private fun createButtonBackground(backgroundColor: Int): StateListDrawable {
        val states = StateListDrawable()
        states.apply {
            addState(intArrayOf(-android.R.attr.state_pressed), createRoundShape(backgroundColor))
            addState(intArrayOf(android.R.attr.state_pressed), createRoundShape(darker(backgroundColor)))
            addState(intArrayOf(android.R.attr.state_focused), createRoundShape(darker(backgroundColor)))
        }
        return states
    }

    private fun createRoundShape(color: Int): GradientDrawable {
        return GradientDrawable().apply {
            cornerRadius = 24f
            setColor(color)
        }
    }

    private fun darker(color: Int): Int {
        val factor = 0.8f
        val r = (Color.red(color) * factor).toInt()
        val g = (Color.green(color) * factor).toInt()
        val b = (Color.blue(color) * factor).toInt()
        return Color.rgb(r, g, b)
    }

    private fun onButtonClick(text: String) {
        when (text) {
            "C" -> {
                currentInput = ""
                operator = null
                operand1 = null
                display.text = "0"
            }
            "=" -> {
                val operand2 = currentInput.toDoubleOrNull()
                if (operand1 != null && operator != null && operand2 != null) {
                    val result = when (operator) {
                        "+" -> operand1!! + operand2
                        "-" -> operand1!! - operand2
                        "*" -> operand1!! * operand2
                        "/" -> if (operand2 != 0.0) operand1!! / operand2 else Double.NaN
                        else -> operand2
                    }
                    display.text = result.toString()
                    operand1 = result
                    currentInput = ""
                    operator = null
                }
            }
            "+", "-", "*", "/" -> {
                if (currentInput.isNotEmpty()) {
                    operand1 = currentInput.toDoubleOrNull()
                    currentInput = ""
                }
                operator = text
            }
            else -> {
                currentInput += text
                display.text = currentInput
            }
        }
    }
}
