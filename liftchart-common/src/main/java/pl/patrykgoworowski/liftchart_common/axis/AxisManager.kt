package pl.patrykgoworowski.liftchart_common.axis

import android.graphics.Canvas
import android.graphics.RectF
import pl.patrykgoworowski.liftchart_common.axis.horizontal.HorizontalAxis
import pl.patrykgoworowski.liftchart_common.axis.model.DataSetModel
import pl.patrykgoworowski.liftchart_common.data_set.entry.collection.EntriesModel
import pl.patrykgoworowski.liftchart_common.data_set.segment.SegmentProperties
import pl.patrykgoworowski.liftchart_common.dimensions.DataSetInsetter
import pl.patrykgoworowski.liftchart_common.dimensions.Dimensions
import pl.patrykgoworowski.liftchart_common.extension.half

public open class AxisManager(
    public open var startAxis: AxisRenderer<AxisPosition.Vertical.Start>? = VerticalAxis.start(),
    public open var topAxis: AxisRenderer<AxisPosition.Horizontal.Top>? = null,
    public open var endAxis: AxisRenderer<AxisPosition.Vertical.End>? = null,
    public open var bottomAxis: AxisRenderer<AxisPosition.Horizontal.Bottom>? = HorizontalAxis.bottom(),
) {

    private val hasLeftAxis: Boolean
        get() = leftAxis != null

    private val hasTopAxis: Boolean
        get() = topAxis != null

    private val hasRightAxis: Boolean
        get() = rightAxis != null

    private val hasBottomAxis: Boolean
        get() = bottomAxis != null

    public var isLTR: Boolean = true

    public val leftAxis: AxisRenderer<*>?
        get() = if (isLTR) startAxis else endAxis

    public val rightAxis: AxisRenderer<*>?
        get() = if (isLTR) endAxis else startAxis

    fun addInsetters(destination: MutableList<DataSetInsetter>) {
        startAxis?.let(destination::add)
        topAxis?.let(destination::add)
        endAxis?.let(destination::add)
        bottomAxis?.let(destination::add)
    }

    fun setAxesBounds(
        contentBounds: RectF,
        dataSetBounds: RectF,
        axesDimensions: Dimensions,
    ) {

        val horizontalAxisLeftDrawBound = dataSetBounds.left + (leftAxis?.axisThickness?.half
            ?: -axesDimensions.getLeft(isLTR))

        val horizontalAxisRightDrawBound = dataSetBounds.right - (rightAxis?.axisThickness?.half
            ?: -axesDimensions.getRight(isLTR))

        startAxis?.let { axis ->
            axis.setBounds(
                left = if (isLTR) contentBounds.left else contentBounds.right - axesDimensions.end,
                top = contentBounds.top + axesDimensions.top,
                right = if (isLTR)
                    contentBounds.left + axesDimensions.start + axis.axisThickness.half
                else
                    contentBounds.right,
                bottom = contentBounds.bottom - axesDimensions.bottom
            )
            axis.dataSetBounds.set(
                horizontalAxisLeftDrawBound,
                dataSetBounds.top + axis.maxAnyAxisLineThickness * if (hasTopAxis) 1 else -1,
                horizontalAxisRightDrawBound,
                dataSetBounds.bottom + if (hasBottomAxis) 0f else axis.maxAnyAxisLineThickness
            )
        }
        topAxis?.let { axis ->
            axis.setBounds(
                left = contentBounds.left + axesDimensions.start,
                top = contentBounds.top,
                right = contentBounds.right - axesDimensions.end,
                bottom = contentBounds.top + axesDimensions.top
            )
            axis.dataSetBounds.set(
                horizontalAxisLeftDrawBound,
                dataSetBounds.top,
                horizontalAxisRightDrawBound,
                dataSetBounds.bottom
            )
        }
        endAxis?.let { axis ->
            axis.setBounds(
                left = if (isLTR)
                    contentBounds.right - (axesDimensions.end + axis.axisThickness.half)
                else
                    contentBounds.left,
                top = contentBounds.top + axesDimensions.top,
                right = if (isLTR)
                    contentBounds.right
                else
                    contentBounds.left + axesDimensions.end,
                bottom = contentBounds.bottom - axesDimensions.bottom
            )
            axis.dataSetBounds.set(
                horizontalAxisLeftDrawBound,
                dataSetBounds.top + axis.maxAnyAxisLineThickness * if (hasTopAxis) 1 else -1,
                horizontalAxisRightDrawBound,
                dataSetBounds.bottom + if (hasBottomAxis) 0f else axis.maxAnyAxisLineThickness
            )
        }
        bottomAxis?.let { axis ->
            axis.setBounds(
                left = contentBounds.left + axesDimensions.start,
                top = contentBounds.bottom - axesDimensions.bottom,
                right = contentBounds.right - axesDimensions.end,
                bottom = contentBounds.bottom
            )
            axis.dataSetBounds.set(
                horizontalAxisLeftDrawBound,
                dataSetBounds.top + if (hasTopAxis) axis.axisThickness else 0f,
                horizontalAxisRightDrawBound,
                dataSetBounds.bottom
            )
        }
    }

    fun draw(
        canvas: Canvas,
        model: EntriesModel,
        dataSetModel: DataSetModel,
        segmentProperties: SegmentProperties,
    ) {
        startAxis?.draw(canvas, model, dataSetModel, segmentProperties)
        topAxis?.draw(canvas, model, dataSetModel, segmentProperties)
        endAxis?.draw(canvas, model, dataSetModel, segmentProperties)
        bottomAxis?.draw(canvas, model, dataSetModel, segmentProperties)
    }

}