package pl.patrykgoworowski.liftchart_common.path

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import kotlin.math.absoluteValue

abstract class CornerShape(
    private val topLeft: Float = 0f,
    private val topRight: Float = 0f,
    private val bottomRight: Float = 0f,
    private val bottomLeft: Float = 0f
) : Shape {

    private var tL = 0f
    private var tR = 0f
    private var bR = 0f
    private var bL = 0f

    private val minHeight by lazy {
        getMinimumHeight(topLeft, topRight, bottomRight, bottomLeft)
    }

    override fun drawShape(
        canvas: Canvas,
        paint: Paint,
        path: Path,
        bounds: RectF
    ) {
        val height = bounds.height().absoluteValue
        when {
            height == 0f -> return
            height < minHeight -> {
                val scale = height / minHeight
                tL = topLeft * scale
                tR = topRight * scale
                bR = bottomRight * scale
                bL = bottomLeft * scale
            }
            else -> {
                tL = topLeft
                tR = topRight
                bR = bottomRight
                bL = bottomLeft
            }
        }
        drawBarPathWithCorners(
            canvas,
            paint,
            path,
            bounds,
            tL,
            tR,
            bR,
            bL
        )
    }

    abstract fun drawBarPathWithCorners(
        canvas: Canvas,
        paint: Paint,
        barPath: Path,
        barBounds: RectF,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    )

}