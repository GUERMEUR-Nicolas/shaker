package com.example.shaker.ui.GameplayStates

data class UpgradeState (
    val map : Map<Pair<Int, Int>, Int> = mapOf<Pair<Int, Int>, Int>()
) {
    fun getUpgradeLevel(recipeId: Int, upgradeId: Int): Int {
        return map[Pair(recipeId, upgradeId)] ?: 0
    }

    public fun UpdatedMap(id: Pair<Int, Int>, amount: Int): Map<Pair<Int, Int>, Int> {
        return map.toMutableMap().apply {
            this[id] = amount
        }
    }

    public fun IncrementedMap(id: Pair<Int, Int>, amount: Int): Map<Pair<Int, Int>, Int> {
        return UpdatedMap(id, (map[id]?: 0) + amount)
    }
}