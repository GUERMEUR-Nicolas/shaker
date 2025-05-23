package com.example.shaker.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaker.R
import com.example.shaker.data.Recipe
import com.example.shaker.data.Upgrade
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayViewModel
import com.example.shaker.ui.MainViewModel


@Composable
fun RecipesPage(
	pagerState: PagerState,
	onRecipeChange: (Int) -> Unit,
	gameState: GameplayViewModel,
	modifier: Modifier = Modifier,
	viewModel: MainViewModel
) {
	LaunchedEffect(pagerState.currentPage) { onRecipeChange(pagerState.currentPage) }
	val fling = PagerDefaults.flingBehavior(
		state = pagerState,
		pagerSnapDistance = PagerSnapDistance.atMost(allRecipes.size / 4)
	)
	VerticalPager(
		state = pagerState,
		flingBehavior = fling,
		modifier = modifier
	) { id ->
		CurrentRecipe(recipeID = id, gameState, viewModel = viewModel)
	}
}

@Composable
fun CurrentRecipe(
	recipeID: Int,
	gameState: GameplayViewModel,
	modifier: Modifier = Modifier,
	viewModel: MainViewModel
) {
	val recipe = allRecipes[recipeID]
	val colorBG = MaterialTheme.colorScheme.background
	val colorOnBg = MaterialTheme.colorScheme.onBackground
	val colorBGStacked = MaterialTheme.colorScheme.surfaceDim
	val colorOnBgStacked = MaterialTheme.colorScheme.onSurface
	Box(
		contentAlignment = Alignment.CenterEnd,
		modifier = with(Modifier) {
			fillMaxSize()
				.paint(
					painter = painterResource(R.drawable.wallpaper),
					contentScale = ContentScale.FillBounds
				)
		}
	) {
		val selectedUpgrade = viewModel.selectedUpgrade.collectAsState().value
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = modifier
				.fillMaxWidth(0.8f)
				.fillMaxHeight()
				.align(Alignment.CenterEnd)
		) {
			RecipeInfo(
				recipe,
				gameState = gameState,
				40.sp,
				showName = true,
				inSidebar = false,
				modifier,
				colorOnBg,
				colorBG
			) {
				if (selectedUpgrade is Upgrade) {
					UpgradePanel(
						recipe,
						selectedUpgrade,
						{ viewModel.selectUpgrade(null) },
						modifier
							.fillMaxWidth(0.8f),
						colorBGStacked,
						colorOnBgStacked,
						gameState
					)
				} else {
					CenteredImage(
						if (recipe.isDiscovered) recipe.imageResourceId else R.drawable.placeholder_0,
						50.dp
					)
				}
			}
			Row(
				horizontalArrangement = Arrangement.SpaceAround,
				modifier = modifier.fillMaxWidth()
			) {
				val buttonColor = ButtonDefaults.textButtonColors(
					containerColor = MaterialTheme.colorScheme.secondary,
					contentColor = MaterialTheme.colorScheme.onSecondary,
					disabledContainerColor = MaterialTheme.colorScheme.surface,
					disabledContentColor = MaterialTheme.colorScheme.onSurface,
				)
				RecipeBuyButton(recipe, 1, gameState, buttonColor)
				RecipeBuyButton(recipe, 10, gameState, buttonColor)
			}
			Spacer(
				modifier = Modifier.height(15.dp)
			)
			UpgradeRow(recipe, modifier, 75.dp, gameState, viewModel)
		}

	}
}

@Composable
fun UpgradeRow(
	recipe: Recipe,
	modifier: Modifier,
	dp: Dp,
	gameState: GameplayViewModel,
	viewModel: MainViewModel
) {
	Row(
		horizontalArrangement = Arrangement.SpaceEvenly,
		modifier = Modifier.fillMaxWidth()
	) {
		for (i in recipe.upgrades) {
			UpgradeWithButton(recipe, i, modifier, dp, gameState, viewModel)
		}
	}
}

@Composable
fun UpgradeWithButton(
	recipe: Recipe,
	upgrade: Upgrade,
	modifier: Modifier,
	dp: Dp,
	gameState: GameplayViewModel,
	viewModel: MainViewModel
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween
	) {
		val selectedUpgrade = viewModel.selectedUpgrade.collectAsState().value
		UpgradeIcon(recipe, upgrade, gameState, Modifier
			.size(dp)
			.clickable {
				if (selectedUpgrade != upgrade) {
					viewModel.selectUpgrade(upgrade)
				} else {
					viewModel.selectUpgrade(null)
				}
			}
		)
	}
}

@Composable
fun TextButton(
	onClick: () -> Unit,
	enable: Boolean,
	text: String,
	colors: ButtonColors = ButtonDefaults.textButtonColors(
		containerColor = MaterialTheme.colorScheme.secondary,
		contentColor = MaterialTheme.colorScheme.onSecondary
	)
) {
	TextButton(
		onClick = onClick,
		enabled = enable,
		colors = ButtonDefaults.textButtonColors(
			containerColor = colors.containerColor,
			contentColor = colors.contentColor,
			disabledContainerColor = if (colors.disabledContainerColor == Color.Unspecified) MaterialTheme.colorScheme.tertiaryContainer else colors.disabledContainerColor,
			disabledContentColor = if (colors.disabledContentColor == Color.Unspecified) MaterialTheme.colorScheme.tertiaryContainer else colors.disabledContentColor,
		),
	) {
		Text(
			text
		)
	}
}

@Composable
fun RecipeBuyButton(
	recipe: Recipe,
	amountToBuy: Long,
	gameplayViewModel: GameplayViewModel,
	color: ButtonColors
) {
	val recipes = gameplayViewModel.recipes.collectAsState()
	val money = gameplayViewModel.moneyState.collectAsState()
	val cost = recipes.value.getNextCost(recipe, amountToBuy).valueAsString()
	TextButton(
		onClick = {
			if (!gameplayViewModel.advancementState.value.getAdvancement("firstBuy"))
				gameplayViewModel.toggleAdvancement("firstBuy")
			gameplayViewModel.forceBuy(recipe, amountToBuy)
		},
		enable = recipes.value.canBuy(recipe, amountToBuy, money.value.current),
		text = stringResource(
			R.string.buy_recipee,
			amountToBuy,
			stringResource(R.string.money_value, cost)
		),
		colors = color
	)
}

@Composable
fun RecipeInfo(
	recipe: Recipe,
	gameState: GameplayViewModel,
	spBase: TextUnit,
	showName: Boolean,
	inSidebar: Boolean,
	modifier: Modifier,
	fontColor: Color,
	backColor: Color,
	content: @Composable () -> Unit
) {
	val recipes = gameState.recipes.collectAsState()
	val recipeAmount = recipes.value.getRecipeAmount(recipe)
	var name = stringResource(recipe.name)
	if (inSidebar) {
		name += " ($recipeAmount)"
	}
	if (!recipe.isDiscovered)
		name = "???"
	TitledElement(
		name,
		if (!inSidebar) stringResource(
			R.string.RecipeCountAndTotal, recipeAmount.toString(), stringResource(
				R.string.money_per_cycle,
				recipe.generating * recipeAmount * (1f / integerResource(R.integer.CycleDurationMultiplier))
			)
		) else null,
		fontColor,
		spBase
	) {
		content()
	}
	if (showName && !inSidebar) {
		PerSecondText(
			recipe.generating,
			fontColor,
			spBase * .8f,
			modifier.fillMaxWidth()
		)
	}
}

@Composable
fun TitledImage(
	title: String,
	subTitle: String?,
	textColor: Color,
	titleSize: TextUnit,
	@DrawableRes imageId: Int,
	imagePadding: Dp,
	modifier: Modifier = Modifier,
) {
	TitledElement(title, subTitle, textColor, titleSize, modifier) {
		CenteredImage(imageId, imagePadding)
	}
}

@Composable
fun CenteredImage(imageId: Int, imagePadding: Dp) {
	Image(
		painter = painterResource(imageId),
		alignment = Alignment.Center,
		contentScale = ContentScale.FillWidth,
		modifier = Modifier
			.fillMaxWidth()
			.padding(imagePadding),
		contentDescription = null
	)
}

@Composable
fun TitledElement(
	title: String,
	subTitle: String?,
	textColor: Color,
	titleSize: TextUnit,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Text(
		fontSize = titleSize,
		text = title,
		color = textColor,
		textAlign = TextAlign.Center,
		overflow = TextOverflow.Visible,
		softWrap = true,
		maxLines = 2
	)
	if (subTitle is String && subTitle.isNotEmpty()) {
		Text(
			fontSize = titleSize * .8f,
			text = subTitle,
			color = textColor,
			textAlign = TextAlign.Center,
			modifier = Modifier.fillMaxWidth(),
		)
	}
	content()
}

@Preview(widthDp = 800, heightDp = 2400)
@Composable
fun CurrentRecipe_P() {
	val gameplayViewModel = GameplayViewModel()
	CurrentRecipe(recipeID = 0, gameState = gameplayViewModel, viewModel = MainViewModel())
}