package com.example.aiexpenzo.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun DailyLineChart(
    modifier: Modifier = Modifier,
    dailyTotals: List<Float>
){
    AndroidView(
        modifier = modifier,
        factory = {context ->
           LineChart(context).apply{
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, 500
                )

               // X-Axis
               xAxis.apply {
                   position = XAxis.XAxisPosition.BOTTOM
                   setDrawLabels(true)
                   setDrawAxisLine(true)
                   setDrawGridLines(false)
                   granularity = 1f
                   labelRotationAngle = -45f
                   textSize = 12f
                   textColor = Color.BLACK

                   valueFormatter = object : ValueFormatter() {
                       override fun getFormattedValue(value: Float): String {
                           return value.toInt().toString()
                       }
                   }
               }

               // Y-Axis
               axisLeft.apply {
                   setDrawLabels(true)
                   setDrawAxisLine(true)
                   setDrawGridLines(true)
                   axisMinimum = 0f // Y-axis starts from 0
                   textSize = 12f
                   textColor = Color.BLACK
               }

               description.isEnabled = false
                setTouchEnabled(false)
                axisRight.isEnabled = false


               //Legend
               legend.isEnabled = true
               legend.textSize = 12f
               legend.form = Legend.LegendForm.LINE
               legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
               legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
               legend.orientation = Legend.LegendOrientation.HORIZONTAL
               legend.setDrawInside(false)

               setNoDataText("No expense data for this month")
            }
        },
        update = {chart ->
            val entries = dailyTotals.mapIndexed{ index, value ->
                BarEntry((index + 1).toFloat(), maxOf(0f,value))
            }
            val dataSet = LineDataSet(entries, "Daily Totals").apply {
                color = Color.rgb(33, 57, 93)
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(false)   // remove nodes
                setDrawValues(false)
                mode = LineDataSet.Mode.LINEAR

            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}