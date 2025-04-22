package com.example.shaker.ui.GameplayStates

data class AdvancementState(
    val map: Map<String, Boolean> = mapOf<String, Boolean>(
        "showSideBar" to false,
        "firstBuy" to false,
        "shaking" to false
    )
) {

    fun getAdvancement(str: String): Boolean {
        return map[str] ?: false
    }

    fun toggleAdvancement(id: String): Map<String, Boolean> {
        return map.toMutableMap().apply {
            this[id] = !this[id]!!
        }
    }
}