/*
 * Copyright 2024 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.patrykandpatrick.vico.sample.showcase.charts

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.decoration.rememberHorizontalLine
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.BaseAxis
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.decoration.HorizontalLine
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.columnSeries
import com.patrykandpatrick.vico.databinding.Chart2Binding
import com.patrykandpatrick.vico.sample.showcase.Defaults
import com.patrykandpatrick.vico.sample.showcase.UISystem
import com.patrykandpatrick.vico.sample.showcase.rememberMarker
import com.patrykandpatrick.vico.views.dimensions.dimensionsOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.util.Locale
import kotlin.random.Random

@Composable
internal fun Chart2(
    uiSystem: UISystem,
    modifier: Modifier,
) {
    val modelProducer = remember { CartesianChartModelProducer.build() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            while (isActive) {
                modelProducer.tryRunTransaction { columnSeries { series(List(47) { 2 + Random.nextFloat() * 18 }) } }
                delay(Defaults.TRANSACTION_INTERVAL_MS)
            }
        }
    }
    when (uiSystem) {
        UISystem.Compose -> ComposeChart2(modelProducer, modifier)
        UISystem.Views -> ViewChart2(modelProducer, modifier)
    }
}

@Composable
private fun ComposeChart2(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
) {
    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(
                    listOf(
                        rememberLineComponent(
                            color = Color(0xffff5500),
                            thickness = 16.dp,
                            shape = Shapes.roundedCornerShape(allPercent = 40),
                        ),
                    ),
                ),
                startAxis = rememberStartAxis(),
                bottomAxis =
                    rememberBottomAxis(
                        valueFormatter = bottomAxisValueFormatter,
                        itemPlacer =
                            remember { AxisItemPlacer.Horizontal.default(spacing = 3, addExtremeLabelPadding = true) },
                    ),
                decorations = listOf(rememberComposeHorizontalLine()),
            ),
        modelProducer = modelProducer,
        modifier = modifier,
        marker = rememberMarker(),
        horizontalLayout = HorizontalLayout.fullWidth(),
    )
}

@Composable
private fun ViewChart2(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
) {
    val marker = rememberMarker()
    AndroidViewBinding(
        { inflater, parent, attachToParent ->
            Chart2Binding
                .inflate(inflater, parent, attachToParent)
                .apply {
                    with(chartView) {
                        chart?.addDecoration(getViewHorizontalLine())
                        this.modelProducer = modelProducer
                        (chart?.bottomAxis as BaseAxis).valueFormatter = bottomAxisValueFormatter
                        this.marker = marker
                    }
                }
        },
        modifier,
    )
}

@Composable
private fun rememberComposeHorizontalLine(): HorizontalLine {
    val color = Color(HORIZONTAL_LINE_COLOR)
    return rememberHorizontalLine(
        y = { HORIZONTAL_LINE_Y },
        line = rememberLineComponent(color, HORIZONTAL_LINE_THICKNESS_DP.dp),
        labelComponent =
            rememberTextComponent(
                background = rememberShapeComponent(Shapes.pillShape, color),
                padding =
                    dimensionsOf(
                        HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP.dp,
                        HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP.dp,
                    ),
                margins = dimensionsOf(HORIZONTAL_LINE_LABEL_MARGIN_DP.dp),
                typeface = Typeface.MONOSPACE,
            ),
    )
}

private fun getViewHorizontalLine() =
    HorizontalLine(
        y = { HORIZONTAL_LINE_Y },
        line = LineComponent(HORIZONTAL_LINE_COLOR, HORIZONTAL_LINE_THICKNESS_DP),
        labelComponent =
            TextComponent.build {
                background = ShapeComponent(Shapes.pillShape, HORIZONTAL_LINE_COLOR)
                padding =
                    dimensionsOf(HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP, HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP)
                margins = dimensionsOf(HORIZONTAL_LINE_LABEL_MARGIN_DP)
                typeface = Typeface.MONOSPACE
            },
    )

private const val HORIZONTAL_LINE_Y = 14f
private const val HORIZONTAL_LINE_COLOR = -2893786
private const val HORIZONTAL_LINE_THICKNESS_DP = 2f
private const val HORIZONTAL_LINE_LABEL_HORIZONTAL_PADDING_DP = 8f
private const val HORIZONTAL_LINE_LABEL_VERTICAL_PADDING_DP = 2f
private const val HORIZONTAL_LINE_LABEL_MARGIN_DP = 4f

private val monthNames = DateFormatSymbols.getInstance(Locale.US).shortMonths
private val bottomAxisValueFormatter =
    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _, _ ->
        "${monthNames[x.toInt() % 12]} ’${20 + x.toInt() / 12}"
    }
