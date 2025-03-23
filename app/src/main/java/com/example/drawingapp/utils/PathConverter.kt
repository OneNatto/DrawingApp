package com.example.drawingapp.utils

import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Path as AndroidPath
import androidx.compose.ui.graphics.Path as ComposePath
import java.lang.StringBuilder
import java.util.regex.Pattern

object PathConverter {
    fun pathToSvg(path: Path): String {
        val pathMeasure = PathMeasure(path, false)
        val pos = FloatArray(2)
        val svgPath = StringBuilder()

        var distance = 0f
        val length = pathMeasure.length

        if (length > 0) {
            pathMeasure.getPosTan(0f, pos, null)
            svgPath.append("M ${pos[0]} ${pos[1]} ") // MoveTo の追加
        }

        while (distance < length) {
            pathMeasure.getPosTan(distance, pos, null)
            svgPath.append("L ${pos[0]} ${pos[1]} ") // LineTo の追加
            distance += 2f // 2pxごとに取得
        }

        return svgPath.toString().trim()
    }

    fun svgToPath(svgPath: String): Path {
        val path = Path()
        val pattern = Pattern.compile("([MLQC])\\s*(-?\\d+(\\.\\d+)?)\\s*(-?\\d+(\\.\\d+)?)\\s*(-?\\d+(\\.\\d+)?)?\\s*(-?\\d+(\\.\\d+)?)?\\s*(-?\\d+(\\.\\d+)?)?\\s*(-?\\d+(\\.\\d+)?)?")
        val matcher = pattern.matcher(svgPath)

        while (matcher.find()) {
            val command = matcher.group(1) // M, L, Q, C
            val x = matcher.group(2)?.toFloatOrNull() ?: continue
            val y = matcher.group(4)?.toFloatOrNull() ?: continue

            when (command) {
                "M" -> path.moveTo(x, y) // MoveTo
                "L" -> path.lineTo(x, y) // LineTo
                "Q" -> {
                    val x1 = matcher.group(6)?.toFloatOrNull() ?: continue
                    val y1 = matcher.group(8)?.toFloatOrNull() ?: continue
                    path.quadTo(x, y, x1, y1) // 二次ベジェ曲線
                }
                "C" -> {
                    val x1 = matcher.group(6)?.toFloatOrNull() ?: continue
                    val y1 = matcher.group(8)?.toFloatOrNull() ?: continue
                    val x2 = matcher.group(10)?.toFloatOrNull() ?: continue
                    val y2 = matcher.group(12)?.toFloatOrNull() ?: continue
                    path.cubicTo(x, y, x1, y1, x2, y2) // 三次ベジェ曲線
                }
            }
        }

        return path
    }

    fun androidPathToComposePath(androidPath: AndroidPath): ComposePath {
        val composePath = ComposePath()
        val pathMeasure = PathMeasure(androidPath, false)
        val pos = FloatArray(2)

        var distance = 0f
        val length = pathMeasure.length

        if (length > 0) {
            pathMeasure.getPosTan(0f, pos, null)
            composePath.moveTo(pos[0], pos[1])
        }

        while (distance < length) {
            pathMeasure.getPosTan(distance, pos, null)
            composePath.lineTo(pos[0], pos[1])
            distance += 2f // 2px ごとに取得
        }

        return composePath
    }
}