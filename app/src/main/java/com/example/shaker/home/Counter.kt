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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.zIndex
import com.example.compose.AppTheme
import com.example.shaker.R
import com.example.shaker.ui.GameplayStates.MoneyState
import com.example.ui.theme.bodyFontFamily
import com.example.ui.theme.displayFontFamily
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToLong

@Composable
fun FlipClockCounter(state: State<MoneyState>, modifier: Modifier = Modifier) {
	val newNumber = state.value.current.toLong()
	val oldNumber = state.value.previous.toLong()
	val exponent = getExponent(newNumber)
	val formattedNewNumber = String.format("%03d", shiftNumber(newNumber, exponent.roundToLong()))
	val formattedOldNumber = String.format("%03d", shiftNumber(oldNumber, getExponent(oldNumber).roundToLong()))

	val numberName = conwayGuyName(exponent.roundToLong())

	Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
		Box(modifier = Modifier.border(5.dp, Color(0xFF444444), CutCornerShape(0.dp))) {
			Row(modifier = Modifier.fillMaxSize()) {
				formattedNewNumber.forEachIndexed { index, newDigitChar ->
					val oldDigit = formattedOldNumber[index]
					FlipDigit(
						oldDigit = oldDigit,
						newDigit = newDigitChar
					)
				}
			}
		}
		Box(modifier = Modifier.border(5.dp, Color(0xFF444444), CutCornerShape(0.dp))) {
			Text(
				text = numberName,
				style = TextStyle(
					fontSize = 30.sp,
					color = Color.White,
					fontFamily = bodyFontFamily,
					background = if (exponent.roundToLong() >= 3) Color.Black else Color.Transparent
				),
				modifier = Modifier.fillMaxSize()
			)
		}
	}
}

@Composable
fun FlipDigit(
	oldDigit: Char,
	newDigit: Char,
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
	val sh = object: Shape {
		override fun createOutline(
			size: Size,
			layoutDirection: androidx.compose.ui.unit.LayoutDirection,
			density: Density
		): androidx.compose.ui.graphics.Outline {
			return Outline.Generic(
				path = Path().apply{
					reset()
					moveTo(0f, size.height/2f)
					lineTo(size.width, size.height/2f)
					lineTo(size.width, size.height/2f + 10f)
					lineTo(0f, size.height/2f + 10f)
				}
			)
		}
	}

	Column(modifier = modifier
		.onGloballyPositioned {
			size = it.size
		}
		.background(Color.Black)
		.clipToBounds()
	) {
		Box(modifier = Modifier
			.height(70.dp)
			.border(12.dp, Color.Black, sh)
		) {
			Digit(newDigit, size, rotationAnimatable, true,  0f)
			Digit(oldDigit, size, rotationAnimatable, true,  1f)
			Digit(oldDigit, size, rotationAnimatable, false, 0f)
			Digit(newDigit, size, rotationAnimatable, false, 1f)
		}
	}
}

@Composable
fun Digit(
	digit: Char,
	sz: IntSize,
	rotationAnimatable: Animatable<Float, AnimationVector1D>,
	isTop: Boolean,
	z: Float
){
	val h = 70.sp
	val tmp = sz.height.toFloat()
	var grad = listOf(Color(0xFF000000), Color(0xFF333333))
	var tp = 0.5f * tmp // TODO: remove tp / bt
	var bt = tmp * max(0.5f, rotationAnimatable.value)
	var rt = 0f
	if (isTop) {
		grad = grad.reversed()
		if(z == 1f) {
			rt = 180 * min(0.5f, rotationAnimatable.value)
			tp = tmp * min(0.5f, rotationAnimatable.value)
		}else{
			tp = 0f
		}
		bt = 0.5f * tmp
	} else if(z != 1f) {
		bt = tmp
		rt = 180f
	} else {
		rt = -180 * max(0.5f, rotationAnimatable.value)
	}
	Text(
		text = digit.toString(),
		style = TextStyle(fontSize = h, color = Color.White, fontFamily = displayFontFamily),
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

fun getExponent(number: Long): Double {
	val l = 1/log2(10.0)
	val t = ((log2(number.toDouble()) * l))
	return t // Fixme: NaN
}

fun shiftNumber(number: Long, exponent: Long): Long {
	var t = exponent
	var i = 0
	while(t >= 3){
		i += 1
		t -= 3
	}
	//println("Init: $number, Number: ${number/10.0.pow(i*6)}, i: $i, t: $t")
	return (number/10.0.pow(i*3)).toLong()
}

fun conwayGuyName(exponent: Long): String {
	// TODO: add long scale
	val firstNames = arrayOf(
		"M", "B", "Tr", "Quadr", "Quint", "Sext", "Sept", "Oct", "Non", "Dec"
	)
	val genericNames = arrayOf(
		arrayOf("Un", "Duo", "Tre", "Quattuor", "Quinqua", "Se", "Septe", "Octo", "Nove"),
		arrayOf("Deci", "Viginti", "Triginta", "Quadraginta", "Quinquaginta", "Sexaginta", "Octoginta", "Nonaginta"),
		arrayOf("Centi", "Ducenti", "Trecenti", "Quadringenti", "Quingenti", "Sescenti", "Septigenti", "Octingenti", "Nongenti")
	)
	if(exponent > 66){
		val strponent = (exponent/3.0 -1).roundToLong().toString()
		var rep: String = ""
		strponent.forEachIndexed { index, c -> rep = genericNames[2-index][c.code-'0'.code] + rep}
		return rep
	} else if(exponent >= 6) {
		return firstNames[((exponent/3).toInt() - 2)] + "illion"
	} else if(exponent >= 3) {
		return "Thousand"
	}
	return ""
}


@Preview
@Composable
fun FlipDigit_P(){
	AppTheme(darkTheme = false) {
		FlipDigit('1', '1')
	}
}