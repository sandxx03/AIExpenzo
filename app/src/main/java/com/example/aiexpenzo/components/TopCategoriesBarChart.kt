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
fun TopCategoriesBarChart(
    modifier: Modifier = Modifier,
    categories: List<CategorySpend>
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply{
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    500
                )
                description.isEnabled = false
                legend.isEnabled = false
                axisRight.isEnabled = false

                axisLeft.apply {
                    axisMinimum = 0f
                    textColor= Color.WHITE
                }

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                    textColor = Color.WHITE
                    granularity = 1f
                    valueFormatter = IndexAxisValueFormatter(categories.map { it.title })
                }
            }
        },
        update = { chart ->
            val entries = categories.mapIndexed{index, cat ->
                BarEntry(index.toFloat(), cat.amount.toFloat())
            }
            val dataSet = BarDataSet(entries, "").apply {
                color = Color.rgb(100, 181, 246)
                valueTextColor = Color.WHITE
                valueTextSize = 12f
            }
            chart.data = BarData(dataSet)
            chart.invalidate()

        }
    )
}