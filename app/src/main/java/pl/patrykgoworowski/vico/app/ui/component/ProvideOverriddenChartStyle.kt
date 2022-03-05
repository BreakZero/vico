/*
 * Copyright (c) 2022. Patryk Goworowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.patrykgoworowski.vico.app.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import pl.patrykgoworowski.vico.app.ChartStyleOverrideManager
import pl.patrykgoworowski.vico.app.ChartStyleOverrides
import pl.patrykgoworowski.vico.compose.style.LocalChartStyle

@Composable
internal fun ProvideOverriddenChartStyle(
    chartStyleOverrides: ChartStyleOverrides,
    content: @Composable () -> Unit,
) {
    val chartStyle = LocalChartStyle.current
    val overriddenChartStyle = remember(chartStyle, chartStyleOverrides) {
        ChartStyleOverrideManager.overrideChartStyle(
            chartStyle = chartStyle,
            chartStyleOverrides = chartStyleOverrides,
        )
    }

    CompositionLocalProvider(
        LocalChartStyle provides overriddenChartStyle,
        content = content,
    )
}