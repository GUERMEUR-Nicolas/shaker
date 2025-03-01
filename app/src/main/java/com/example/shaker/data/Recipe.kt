package com.example.shaker.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shaker.R

class Recipe(
	@DrawableRes val imageResourceId: Int,
	@StringRes val name: Int,
	val id: Int,
	var amount: ScalingInt = ScalingInt(0),
	val basePrice: ScalingInt = ScalingInt(1),
	val scalingFactor: Double = 1.15
)
//TODO move this to the viewmodel
val recipes = listOf(
	Recipe(imageResourceId = R.drawable.placeholder_0, basePrice = ScalingInt(1),amount = ScalingInt(1), name =R.string.recipe_name, id =0),
	Recipe(imageResourceId = R.drawable.placeholder_1, basePrice = ScalingInt(5),amount = ScalingInt(3),name= R.string.recipe_name, id = 1),
	Recipe(imageResourceId = R.drawable.placeholder_2, basePrice = ScalingInt(100),amount = ScalingInt(12),name= R.string.recipe_name, id = 2),
	Recipe(imageResourceId = R.drawable.placeholder_3, basePrice = ScalingInt(100),amount = ScalingInt(60),name= R.string.recipe_name, id = 3),
	Recipe(imageResourceId = R.drawable.placeholder_4, basePrice = ScalingInt(100),amount = ScalingInt(360),name= R.string.recipe_name, id = 4),
	Recipe(imageResourceId = R.drawable.placeholder_0, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 5),
	Recipe(imageResourceId = R.drawable.placeholder_1, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 6),
	Recipe(imageResourceId = R.drawable.placeholder_2, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 7),
	Recipe(imageResourceId = R.drawable.placeholder_3, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 8),
	Recipe(imageResourceId = R.drawable.placeholder_4, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 9),
	Recipe(imageResourceId = R.drawable.placeholder_0, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 10),
	Recipe(imageResourceId = R.drawable.placeholder_1, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 11),
	Recipe(imageResourceId = R.drawable.placeholder_2, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 12),
	Recipe(imageResourceId = R.drawable.placeholder_3, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id = 13),
	Recipe(imageResourceId = R.drawable.placeholder_4, basePrice = ScalingInt(100),amount = ScalingInt(1000),name= R.string.recipe_name, id =14)
)