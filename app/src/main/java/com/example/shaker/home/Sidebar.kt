package com.example.shaker.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MovingSideBar(
    viewModel: MainViewModel,
    gameState: GameplayViewModel,
    pagerState_H: PagerState,
    pagerState_V: PagerState,
    modifier: Modifier
) {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var sidebarWidthPx by remember { mutableIntStateOf(0) }
    val sidebarWidthDp = with(density) { sidebarWidthPx.toDp() }
    val cutCorner = remember { Animatable(0f) }
    val advancement = gameState.advancementState.collectAsState()

    val offset = if (!advancement.value.getAdvancement("showSideBar")) sidebarWidthDp else
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
            val initial = viewModel.selectedRecipeId.value
            val isFirst = advancement.value.getAdvancement("firstBuy")
            viewModel.selectRecipe(recipeId)
            coroutineScope.launch {
                if (!advancement.value.getAdvancement("shaking")) {
                    if (pagerState_H.currentPage == 0) {
                        pagerState_V.scrollToPage(recipeId)
                        animateHScroll(pagerState_H, isFirst, 1)
                    } else {
                        pagerState_V.animateScrollToPage(recipeId)
                        //We go back to the shaker page if we clicked "back" on the currently selected recipe
                        if (initial == recipeId) {
                            animateHScroll(pagerState_H, isFirst, 0)
                        }
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth(.2f)
            .onGloballyPositioned { coordinates ->
                sidebarWidthPx = coordinates.size.width
            }
            .offset { IntOffset(offset.roundToPx(), 0) }
            .clip(
                SidebarShape(
                    cutLength,
                    cornerRadius,
                    pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction
                )
            )
            .border(
                5.dp,
                MaterialTheme.colorScheme.outline,
                SidebarShape(
                    cutLength,
                    cornerRadius,
                    pagerState_H.currentPage + pagerState_H.currentPageOffsetFraction,
                    false
                )
            )
    )

    BackHandler {
        coroutineScope.launch {
            animateHScroll(pagerState_H, advancement.value.getAdvancement("firstBuy"), 0)
        }
    }
}


suspend fun animateHScroll(
    pagerState: PagerState,
    faster: Boolean,
    page: Int
) {
    pagerState.animateScrollToPage(
        page = page,
        animationSpec = tween(
            durationMillis = if (faster) 150 else 250,
            easing = LinearEasing
        )
    )
}

@Composable
fun Sidebar(
    selectedRecipeId: Int,
    gameState: GameplayViewModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sidebarBg = MaterialTheme.colorScheme.surfaceDim
    val fontOnSidebarBg = MaterialTheme.colorScheme.onSurface
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(sidebarBg),
            contentAlignment = Alignment.Center
        ) {
            val recipeState by gameState.recipes.collectAsState() // To ensure recomposition on the recipes changes
            LazyColumn(
                userScrollEnabled = true,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .background(sidebarBg)
                    .fillMaxWidth(0.95f)
            ) {
                items(allRecipes) { recipe ->
                    val isSelected = recipe.id == selectedRecipeId
                    val bgColor =
                        if (isSelected) MaterialTheme.colorScheme.secondary else sidebarBg
                    val textColor =
                        if (isSelected) MaterialTheme.colorScheme.onSecondary else fontOnSidebarBg
                    RecipeItem(
                        recipe = recipe,
                        gameState = gameState,
                        onRecipeClick = onRecipeClick,
                        modifier = Modifier
                            .padding(vertical = 1.dp),
                        true,
                        textColor = textColor,
                        bgColor = bgColor,
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    gameState: GameplayViewModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showName: Boolean = true,
    bgColor: Color,
    textColor: Color
) {

    Column(
        modifier = modifier
            .clickable { onRecipeClick(recipe.id) }
            .background(bgColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecipeInfo(recipe, gameState, 12.sp, showName, true, Modifier, textColor, bgColor) {
            CenteredImage(
                if (recipe.isDiscovered) recipe.imageResourceId else R.drawable.placeholder_0,
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
    ): Outline {
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
    val sz = Size(2 * cornerRadius, 2 * cornerRadius)
    val Va = 1 - aV
    return Path().apply {
        reset()
        if (drawAll) {
            lineTo(x = size.width, y = 0f)
        } else {
            moveTo(x = size.width, y = 0f)
        }
        if (drawAll) {
            lineTo(size.width, size.height - cutLength * aV)
        } else {
            moveTo(size.width, (size.height - cutLength * aV) * Va)
            lineTo(size.width, size.height - cutLength * aV)
        }
        if (cornerRadius != 0.0f) {
            arcTo(
                rect = Rect(
                    offset = Offset(
                        x = (cutLength - cornerRadius) + aV * (size.width - (cutLength - cornerRadius) - 2 * cornerRadius),
                        y = size.height - cutLength * aV - 2 * cornerRadius * Va
                    ),
                    size = sz
                ),
                startAngleDegrees = bot - 90f * aV,
                sweepAngleDegrees = +45f,
                forceMoveTo = false
            )
        }
        if (cornerRadius != 0.0f) {
            arcTo(
                rect = Rect(
                    offset = Offset(
                        x = 0f + cornerRadius * aV,
                        y = size.height - cutLength * Va - 2 * cornerRadius * aV
                    ),
                    size = sz
                ),
                startAngleDegrees = sid - 90f * aV,
                sweepAngleDegrees = +45f,
                forceMoveTo = false
            )
        }
        lineTo(0f, size.height - cutLength * Va)
        if (drawAll) {
            close()
        } else {
            lineTo(0f, size.height * aV)
        }
    }
}