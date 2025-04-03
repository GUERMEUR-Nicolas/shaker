package com.example.shaker.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovingSideBar(
    viewModel: MainViewModel,
    gameState : GameplayViewModel,
    pagerState_H: PagerState,
    pagerState_V: PagerState,
    modifier: Modifier
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var sidebarWidthPx by remember { mutableStateOf(0) }
    val sidebarWidthDp = with(density) { sidebarWidthPx.toDp() }
	val cutCorner = remember { Animatable(0f) }

    val offset =
        -(configuration.screenWidthDp.dp - sidebarWidthDp) * (pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction)

    val coroutineScope = rememberCoroutineScope() // scroll to page
    val selectedRecipeId by viewModel.selectedRecipeId.collectAsState()
	val cutLength = 150f
	val cornerRadius = 40f

	LaunchedEffect(pagerState_H.currentPage) {
		cutCorner.animateTo(
			targetValue = pagerState_H.currentPage.toFloat(),
			animationSpec = tween(durationMillis = 400, easing = LinearEasing)
		)
	}

    Sidebar(
        selectedRecipeId = selectedRecipeId,
        gameState,
        onRecipeClick = { recipeId ->
            viewModel.selectRecipe(recipeId)
            coroutineScope.launch {
				if(pagerState_H.currentPage == 0) {
					pagerState_V.scrollToPage(recipeId)
					pagerState_H.animateScrollToPage(1)
				}else {
					pagerState_V.animateScrollToPage(recipeId)
				}
            }
        },
        modifier = modifier
			.fillMaxWidth(.2f)
			.onGloballyPositioned { coordinates ->
				sidebarWidthPx = coordinates.size.width
			}
			.offset { IntOffset(offset.roundToPx(), 0) }
			//.background(Color(0xFF454078))
			.clip(
				SidebarShape(
					cutLength,
					cornerRadius,
					pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction
				)
			)
			.border(
				5.dp,
				Color(0xFF8AF4E9),
				SidebarShape(
					cutLength,
					cornerRadius,
					pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction,
					false
				)
			)
    )
}

@Composable
fun Sidebar(
	selectedRecipeId: Int,
	gameState : GameplayViewModel,
	onRecipeClick: (Int) -> Unit,
	modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        //SidebarHeader(Modifier)
        Box(
			Modifier
				.fillMaxSize()
		) {
            val recipeState by gameState.recipes.collectAsState()//To ensure recomposition on the recipes changes
            LazyColumn(
                userScrollEnabled = true,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                items(allRecipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        gameState = gameState,
                        isSelected = recipe.id == selectedRecipeId,
                        onRecipeClick = onRecipeClick,
                        modifier = Modifier
							.padding(vertical = 1.dp)
							.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SidebarHeader(modifier: Modifier = Modifier) {
    Surface(
        modifier
			.fillMaxWidth()
			.background(Color.Red)
    ) {
        Text(
            text = (stringResource(R.string.recipe_name) + "s"),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = modifier
                .padding(horizontal = 5.dp)
        )
    }
}

@Composable
fun RecipeItem(
	recipe: Recipe,
	isSelected: Boolean,
	gameState: GameplayViewModel,
	onRecipeClick: (Int) -> Unit,
	modifier: Modifier = Modifier,
	showName: Boolean = true
) {
    Column(
        modifier = modifier
			.clickable { onRecipeClick(recipe.id) }
			.background(if (isSelected) Color(0xFFF6C800) else Color(0xFFF0F3D8)),
        verticalArrangement = Arrangement.Top,
    ) {
        RecipeInfo(recipe, gameState,12.sp,showName, true, modifier){
			CenteredImage(
				recipe.imageResourceId,
				5.dp,
			)
		}
    }
}

class SidebarShape(
	private val cutLength: Float,
	private val cornerRadius: Float,
	private val animVal: Float,
	private val drawAll: Boolean = true
) : Shape {
	override fun createOutline(
		size: Size,
		layoutDirection: androidx.compose.ui.unit.LayoutDirection,
		density: Density
	): androidx.compose.ui.graphics.Outline {
		return Outline.Generic(
			path = drawSidebarPath(
				size = size,
				cutLength = cutLength,
				cornerRadius = cornerRadius,
				aV = animVal,
				drawAll = drawAll
			)
		)
	}
}

fun drawSidebarPath(
	size: Size,
	cutLength: Float,
	cornerRadius: Float,
	aV: Float,
	drawAll: Boolean
): Path {
	val bot = 90f
	val sid = 135f
	val sz = Size(2*cornerRadius, 2*cornerRadius)
	val Va = 1-aV
	return Path().apply {
		reset()
		if(drawAll) {
			lineTo(x = size.width, y = 0f)
		}else{
			moveTo(x = size.width, y = 0f)
		}
		if(drawAll){
			lineTo(size.width, size.height - cutLength*aV)
		}else{
			moveTo(size.width, (size.height - cutLength*aV)*Va)
			lineTo(size.width, size.height - cutLength*aV)
		}
		if(cornerRadius != 0.0f) {
			arcTo(
				rect = Rect(
					offset = Offset(
						x = (cutLength - cornerRadius)+aV*(size.width-(cutLength - cornerRadius)-2*cornerRadius),
						y = size.height - cutLength*aV - 2*cornerRadius*Va
					),
					size = sz
				),
				startAngleDegrees = bot - 90f*aV,
				sweepAngleDegrees = +45f,
				forceMoveTo = false
			)
		}
		if(cornerRadius != 0.0f) {
			arcTo(
				rect = Rect(
					offset = Offset(
						x = 0f + cornerRadius*aV,
						y = size.height - cutLength*Va - 2*cornerRadius*aV
					),
					size = sz
				),
				startAngleDegrees = sid - 90f*aV,
				sweepAngleDegrees = +45f,
				forceMoveTo = false
			)
		}
		lineTo(0f, size.height-cutLength*Va)
		if(drawAll){
			close()
		}else{
			lineTo(0f, size.height*aV)
		}
	}
}

/*@Preview(widthDp = 70)
@Composable
private fun SidebarPreview() {
	Sidebar({}, 0, Modifier.fillMaxHeight())
}*/