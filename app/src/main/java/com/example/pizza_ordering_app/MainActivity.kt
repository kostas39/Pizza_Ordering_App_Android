package com.example.pizza_ordering_app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var totalPriceText: TextView
    private lateinit var ingredientsIconsLayout: LinearLayout

    private val toppingsPrices = mapOf(
        R.id.tomatoesCheckbox to 2,
        R.id.peppersCheckbox to 1,
        R.id.mushroomsCheckbox to 2,
        R.id.olivesCheckbox to 1,
        R.id.salamiCheckbox to 3,
        R.id.cornCheckbox to 1
    )

    private val toppingsIcons = mapOf(
        R.id.tomatoesCheckbox to R.drawable.tomatoes,
        R.id.peppersCheckbox to R.drawable.peppers,
        R.id.mushroomsCheckbox to R.drawable.mushrooms,
        R.id.olivesCheckbox to R.drawable.olives,
        R.id.salamiCheckbox to R.drawable.salami,
        R.id.cornCheckbox to R.drawable.corn
    )

    private var basePrice = 6
    private var totalPrice = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalPriceText = findViewById(R.id.totalPriceText)
        ingredientsIconsLayout = findViewById(R.id.ingredientsIconsLayout)

        // Set up checkbox listeners
        toppingsPrices.keys.forEach { checkboxId ->
            findViewById<CheckBox>(checkboxId).setOnCheckedChangeListener { _, isChecked ->
                updateOrder(checkboxId, isChecked)
            }
        }

        // Order button
        findViewById<Button>(R.id.orderButton).setOnClickListener {
            Toast.makeText(this, "Your order is taken! Total: £$totalPrice", Toast.LENGTH_SHORT).show()
        }

        // Clear button
        findViewById<Button>(R.id.clearButton).setOnClickListener {
            clearOrder()
        }
    }

    private fun updateOrder(checkboxId: Int, isChecked: Boolean) {
        val priceChange = toppingsPrices[checkboxId] ?: 0
        val iconResource = toppingsIcons[checkboxId]

        if (isChecked) {
            totalPrice += priceChange
            if (iconResource != null) {
                val imageView = ImageView(this).apply {
                    setImageResource(iconResource)
                    layoutParams = LinearLayout.LayoutParams(
                        dpToPx(50), // Width in dp
                        dpToPx(40)  // Height in dp
                    ).apply {
                        setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4)) // Margins in dp
                    }
                }
                imageView.tag = checkboxId // Use tag to identify this icon
                ingredientsIconsLayout.addView(imageView)
            }
        } else {
            totalPrice -= priceChange
            // Remove the corresponding icon from the layout
            val imageView = ingredientsIconsLayout.findViewWithTag<View>(checkboxId)
            ingredientsIconsLayout.removeView(imageView)
        }

        totalPriceText.text = "Total needs to be paid: £$totalPrice"
    }

    private fun clearOrder() {
        totalPrice = basePrice
        totalPriceText.text = "Total needs to be paid: £$totalPrice"
        ingredientsIconsLayout.removeAllViews()

        // Uncheck all checkboxes
        toppingsPrices.keys.forEach { checkboxId ->
            findViewById<CheckBox>(checkboxId).isChecked = false
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
