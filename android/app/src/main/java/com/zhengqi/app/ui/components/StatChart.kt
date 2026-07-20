package com.zhengqi.app.ui.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.zhengqi.app.ui.theme.StatusGreen

@Composable
fun WeeklyBarChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp)) {
            Text(
                text = "暂无数据",
                modifier = Modifier.fillMaxSize().wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val primaryArgb = MaterialTheme.colorScheme.primary.toArgb()
    val inkArgb = MaterialTheme.colorScheme.onBackground.toArgb()
    val inkMutedArgb = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val dividerArgb = MaterialTheme.colorScheme.outlineVariant.toArgb()

    AndroidView(
        modifier = modifier.fillMaxWidth().height(220.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                setBackgroundColor(AndroidColor.TRANSPARENT)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = inkMutedArgb
                    textSize = 11f
                }
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = dividerArgb
                    textColor = inkMutedArgb
                    textSize = 11f
                    axisMinimum = 0f
                    granularity = 1f
                }
                axisRight.isEnabled = false

                val entries = data.entries.mapIndexed { index, entry ->
                    BarEntry(index.toFloat(), entry.value.toFloat())
                }
                val dataSet = BarDataSet(entries, "").apply {
                    color = primaryArgb
                    valueTextColor = inkArgb
                    valueTextSize = 11f
                    setDrawValues(true)
                }
                val labels = data.keys.toList()
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                this.data = BarData(dataSet)
                animateY(800)
                invalidate()
            }
        }
    )
}

@Composable
fun MonthlyLineChart(
    data: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth().height(200.dp)) {
            Text(
                text = "暂无数据",
                modifier = Modifier.fillMaxSize().wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val primaryArgb = MaterialTheme.colorScheme.primary.toArgb()
    val inkArgb = MaterialTheme.colorScheme.onBackground.toArgb()
    val inkMutedArgb = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val dividerArgb = MaterialTheme.colorScheme.outlineVariant.toArgb()

    AndroidView(
        modifier = modifier.fillMaxWidth().height(220.dp),
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setScaleEnabled(false)
                setPinchZoom(false)
                setDrawGridBackground(false)
                setBackgroundColor(AndroidColor.TRANSPARENT)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = inkMutedArgb
                    textSize = 10f
                    labelRotationAngle = -45f
                }
                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = dividerArgb
                    textColor = inkMutedArgb
                    textSize = 11f
                    axisMinimum = 0f
                    axisMaximum = 1f
                    granularity = 0.2f
                    valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                        override fun getFormattedValue(value: Float): String =
                            "${(value * 100).toInt()}%"
                    }
                }
                axisRight.isEnabled = false

                val entries = data.entries.mapIndexed { index, entry ->
                    Entry(index.toFloat(), entry.value)
                }
                val dataSet = LineDataSet(entries, "").apply {
                    color = primaryArgb
                    valueTextColor = inkArgb
                    valueTextSize = 10f
                    setDrawValues(true)
                    setDrawCircles(true)
                    circleRadius = 3f
                    setCircleColor(primaryArgb)
                    lineWidth = 2f
                    valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                        override fun getPointLabel(entry: Entry?): String =
                            entry?.let { "${(it.y * 100).toInt()}%" } ?: ""
                    }
                }
                val labels = data.keys.toList()
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                this.data = LineData(dataSet)
                animateX(800)
                invalidate()
            }
        }
    )
}

@Composable
fun YearlyHeatmap(
    data: Map<String, Int>,
    year: Int = java.time.LocalDate.now().year,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "${year}年打卡热力图",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val maxCount = data.values.maxOrNull() ?: 1
        val days = listOf("一", "二", "三", "四", "五", "六", "日")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (day in 0..6) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = days[day],
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(18.dp)
                    )
                    for (week in 0..20) {
                        val dateStr = calculateDateForCell(year, week, day)
                        val count = data[dateStr] ?: 0
                        val intensity = if (maxCount > 0) count.toFloat() / maxCount else 0f
                        val color = when {
                            intensity == 0f -> MaterialTheme.colorScheme.outlineVariant
                            intensity < 0.33f -> StatusGreen.copy(alpha = 0.3f)
                            intensity < 0.66f -> StatusGreen.copy(alpha = 0.6f)
                            else -> StatusGreen
                        }
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .background(
                                    color = color,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text("少", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(12.dp).background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.size(12.dp).background(StatusGreen.copy(alpha = 0.3f), RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.size(12.dp).background(StatusGreen.copy(alpha = 0.6f), RoundedCornerShape(2.dp)))
            Box(modifier = Modifier.size(12.dp).background(StatusGreen, RoundedCornerShape(2.dp)))
            Spacer(modifier = Modifier.width(4.dp))
            Text("多", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun calculateDateForCell(year: Int, week: Int, dayOfWeek: Int): String {
    val jan1 = java.time.LocalDate.of(year, 1, 1)
    val firstMonday = if (jan1.dayOfWeek.value <= 4) {
        jan1.minusDays((jan1.dayOfWeek.value - 1).toLong())
    } else {
        jan1.plusDays((8 - jan1.dayOfWeek.value).toLong())
    }
    val date = firstMonday.plusWeeks(week.toLong()).plusDays(dayOfWeek.toLong())
    return if (date.year == year) java.time.format.DateTimeFormatter.ISO_LOCAL_DATE.format(date) else ""
}
