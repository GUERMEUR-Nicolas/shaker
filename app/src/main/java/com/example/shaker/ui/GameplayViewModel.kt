package com.example.shaker.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.shaker.data.Recipe
import com.example.shaker.data.ScalingInt
import com.example.shaker.data.Upgrade
import com.example.shaker.data.doAllUpgradesOfType
import com.example.shaker.data.allRecipes
import com.example.shaker.ui.GameplayStates.MoneyState
import com.example.shaker.ui.GameplayStates.RecipeState
import com.example.shaker.ui.GameplayStates.UpgradeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameplayViewModel : ViewModel() {

    private val _moneyState = MutableStateFlow(MoneyState())
    val moneyState: StateFlow<MoneyState> = _moneyState.asStateFlow()
    private val _recipes = MutableStateFlow(RecipeState())
    val recipes: StateFlow<RecipeState> = _recipes.asStateFlow()
//    private val _upgradeLevels = MutableStateFlow<MutableMap<Pair<Int, Int>, Int>>(mutableMapOf<Pair<Int, Int>, Int>())
//    val upgradeLevels: StateFlow<Map<Pair<Int, Int>, Int>> = _upgradeLevels.asStateFlow()
    private val _upgradeLevels = MutableStateFlow(UpgradeState())
    val upgradeLevels: StateFlow<UpgradeState> = _upgradeLevels.asStateFlow()
    var accumalated: Float = 0f
    private var _showSideBar = MutableStateFlow(false)
    var showSideBar = _showSideBar.asStateFlow()

    public fun Sell(recipe: Recipe, amount: Long) {
        ForceBuy(recipe, -amount)
    }


    public fun ForceBuy(recipe: Recipe, amount: Long) {
        val id = recipe.id
        _moneyState.value = _moneyState.value.copy(
            previous = _moneyState.value.current,
            current = _moneyState.value.current - recipe.GetNextCost(_recipes.value.GetRecipeAmount(recipe), amount)
        )
        //We increment the recipe count after we effectively bought it, other ways, we'd pay one level ahead
        _recipes.value = _recipes.value.copy(
            map = _recipes.value.IncrementedMap(id, amount)
        )
        this.updatePerSecond()
    }
    public fun ForceBuy(recipe: Recipe, upgrade: Upgrade, amount: Long = 1){
        _moneyState.value = _moneyState.value.copy(
            previous = _moneyState.value.current,
            current = _moneyState.value.current - upgrade.GetNextCost(amount)
        )
        _upgradeLevels.value = _upgradeLevels.value.copy(
            map = _upgradeLevels.value.IncrementedMap(Pair(recipe.id, upgrade.id), amount.toInt())
        )
        upgrade.level += amount.toInt()
        this.updatePerSecond()
    }

    public fun Increment(value: ScalingInt) {
        _moneyState.value = _moneyState.value.copy(
            previous = _moneyState.value.current,
            current = _moneyState.value.current + value
        )
        if(!_showSideBar.value && _recipes.value.canBuy(allRecipes[0], 1, _moneyState.value.current))
            _showSideBar.value = true
    }

    public fun Increment(numberOfCycle: Float) {
        val incr = numberOfCycle * _moneyState.value.perSecond.toFloat()
        //If the value is directly above the minimal value, we increment it directly, using the ScalingInt Multiplication
        if(incr>10000f)
            Increment(incr)
        else {
            //Else, mostly in the beggining when incr is less than 1 per update rate, we acucumlate it in a float value
            accumalated += incr;
            if (accumalated > 1f) {
                //When the value is above 1, we increment the whole value cropped (maybe we accumlated from 0.99 to 1929.97 and keep the rest for the next cycle
                val wholeAcc = accumalated.toInt()
                Increment(ScalingInt(wholeAcc))
                accumalated -= wholeAcc;
            }
        }
    }

    var lastShake: Long = 0L
    public fun OnShaked(delay: Long) {
        val current = System.currentTimeMillis()
        if ((current - lastShake) > delay) {
            lastShake = current
            Increment(_moneyState.value.perShake)
        }
    }

    public fun TimesTen() {
        _moneyState.value = _moneyState.value.copy(
            previous = _moneyState.value.current,
            current = _moneyState.value.current * 10
        )
    }

    public fun updatePerSecond(){
        var perSecond = ScalingInt(0)
        for(r in allRecipes)
            perSecond += doAllUpgradesOfType(r.upgrades, r.generating, "generate", _recipes.value.map) * _recipes.value.GetRecipeAmount(r)
        // TODO: add raw cps
        _moneyState.value = _moneyState.value.copy(
            perSecond = perSecond
        )
    }

//    fun getUpgradeLevel(recipeId: Int, upgradeId: Int): Int {
//        return _upgradeLevels.value[Pair(recipeId, upgradeId)] ?: 0
//    }
}
