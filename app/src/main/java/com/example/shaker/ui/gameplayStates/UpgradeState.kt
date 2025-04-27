package com.example.shaker.ui.gameplayStates

data class UpgradeState(
    val map: Map<Pair<Int, Int>, Int> = mapOf<Pair<Int, Int>, Int>()
) {
    fun getUpgradeLevel(recipeId: Int, upgradeId: Int): Int {
        return map[Pair(recipeId, upgradeId)] ?: 0
    }

    fun updatedMap(id: Pair<Int, Int>, amount: Int): Map<Pair<Int, Int>, Int> {
        return map.toMutableMap().apply {
            this[id] = amount
        }
    }

    fun incrementedMap(id: Pair<Int, Int>, amount: Int): Map<Pair<Int, Int>, Int> {
        return updatedMap(id, (map[id] ?: 0) + amount)
    }
}