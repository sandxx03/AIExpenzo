package com.example.aiexpenzo.components

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.aiexpenzo.data.model.CategorySpend
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun CategoriesBarChart(
    modifier: Modifier = Modifier,
    categories: List<CategorySpend>
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply{
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false

                // Disable touch interactions
                setTouchEnabled(false)
                isHighlightPerTapEnabled = false
                isHighlightPerDragEnabled = false

                axisLeft.apply {
                    axisMinimum = 0f
                    textColor= Color.BLACK
                }

                setupXAxis(categories.map { it.title })
            }
        },
        update = { chart ->
            // ensure x-axis labels stay aligned and refreshed
            chart.setupXAxis(categories.map { it.title })

            // map entries with correct index
            val entries = categories.mapIndexed{index, cat ->
                BarEntry(index.toFloat(), cat.amount.coerceAtLeast(0f))
            }
            val dataSet = BarDataSet(entries, "").apply {
                color = Color.rgb(33, 57, 93)
                valueTextColor = Color.BLACK
                valueTextSize = 12f
            }
            val barData = BarData(dataSet).apply {
                barWidth = 0.9f  // Set bar width less than 1.0 to avoid bars sticking together
            }
            chart.data = barData

            chart.invalidate()

        }
    )
}

private fun BarChart.setupXAxis(labels: List<String>){
    xAxis.apply {
        valueFormatter = IndexAxisValueFormatter(labels)
        granularity = 1f
        isGranularityEnabled = true
        setLabelCount(labels.size)
        labelRotationAngle = 90f
        axisMinimum = -0.5f
        axisMaximum = labels.size.toFloat()
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawAxisLine(true)
        textSize = 12f
        textColor = Color.BLACK
    }

}