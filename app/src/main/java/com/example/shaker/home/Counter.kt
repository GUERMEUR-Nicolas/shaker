package com.example.shaker.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.data.ScalingInt
import androidx.compose.runtime.State
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import com.example.compose.AppTheme
import com.example.shaker.R
import com.example.shaker.data.conwayGuyName
import com.example.shaker.ui.GameplayStates.MoneyState
import com.example.ui.theme.bodyFontFamily
import com.example.ui.theme.displayFontFamily
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun FlipClockCounter(
    state: State<MoneyState>, modifier: Modifier = Modifier, backColor: Color, color: Color
) {
    val newNumber = state.value.current
    val oldNumber = state.value.previous
    val exponent = newNumber.getExponent()
    val formattedNewNumber = String.format("%03d", newNumber.shiftNumber())
    val formattedOldNumber = String.format("%03d", oldNumber.shiftNumber())

    val numberName = conwayGuyName(exponent).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier) {
            Row(modifier = Modifier) {
                formattedNewNumber.forEachIndexed { index, newDigitChar ->
                    val oldDigit = formattedOldNumber[index]
                    FlipDigit(
                        oldDigit = oldDigit,
                        newDigit = newDigitChar,
                        backColor = backColor,
                        color = color,
                    )
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ) {
            Text(
                text = numberName,
                style = TextStyle(
                    fontSize = 30.sp,
                    color = color,
                    fontFamily = bodyFontFamily,
                    background = if (exponent >= 3) backColor else Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FlipDigit(
    oldDigit: Char,
    newDigit: Char,
    color: Color,
    backColor: Color,
    modifier: Modifier = Modifier
) {
    val rotationAnimatable = remember { Animatable(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(newDigit) {
        rotationAnimatable.snapTo(0f)
        rotationAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 200, easing = LinearEasing)
        )
    }
    val sh = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: androidx.compose.ui.unit.LayoutDirection,
            density: Density
        ): androidx.compose.ui.graphics.Outline {
            return Outline.Generic(
                path = Path().apply {
                    reset()
                    moveTo(0f, size.height / 2f)
                    lineTo(size.width, size.height / 2f)
                    lineTo(size.width, size.height / 2f + 10f)
                    lineTo(0f, size.height / 2f + 10f)
                }
            )
        }
    }

    Column(modifier = modifier
        .onGloballyPositioned {
            size = it.size
        }
        //.background(backColor)
        .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .height(70.dp)
                .border(12.dp, backColor, sh)
        ) {
            Digit(newDigit, backColor,color, size, rotationAnimatable, true, 0f)
            Digit(oldDigit, backColor,color, size, rotationAnimatable, true, 1f)
            Digit(oldDigit, backColor,color, size, rotationAnimatable, false, 0f)
            Digit(newDigit, backColor,color, size, rotationAnimatable, false, 1f)
        }
    }
}

@Composable
fun Digit(
    digit: Char,
    backColor: Color,
    color: Color,
    sz: IntSize,
    rotationAnimatable: Animatable<Float, AnimationVector1D>,
    isTop: Boolean,
    z: Float
) {
    val h = 70.sp
    val tmp = sz.height.toFloat()
    var grad = listOf(backColor, backColor)
    var tp = 0.5f * tmp // TODO: remove tp / bt
    var bt = tmp * max(0.5f, rotationAnimatable.value)
    var rt = 0f
    if (isTop) {
        grad = grad.reversed()
        if (z == 1f) {
            rt = 180 * min(0.5f, rotationAnimatable.value)
            tp = tmp * min(0.5f, rotationAnimatable.value)
        } else {
            tp = 0f
        }
        bt = 0.5f * tmp
    } else if (z != 1f) {
        bt = tmp
        rt = 180f
    } else {
        rt = -180 * max(0.5f, rotationAnimatable.value)
    }
    Text(
        text = digit.toString(),
        style = TextStyle(fontSize = h, color = color, fontFamily = displayFontFamily),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .drawWithContent {
                clipRect(
                    top = tp,
                    bottom = bt
                ) {
                    this@drawWithContent.drawContent()
                }
            }
            .background(
                Brush.verticalGradient(
                    grad,
                    startY = tp,
                    endY = bt
                )
            )
            .width(h.value.dp)
            .graphicsLayer {
                rotationX = rt - if (!isTop) 180f else 0f
            }
            .zIndex(z)
    )
}


@Preview
@Composable
fun FlipDigit_P() {
    AppTheme(darkTheme = false) {
        FlipDigit('1', '1', MaterialTheme.colorScheme.onSecondaryContainer, MaterialTheme.colorScheme.secondaryContainer)
    }
}
