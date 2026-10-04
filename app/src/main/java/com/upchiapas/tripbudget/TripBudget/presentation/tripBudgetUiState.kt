package com.upchiapas.tripbudget.presentation

enum class BudgetStatus {
    EMPTY,      // todavía no hay datos suficientes
    ENOUGH,     // Alcanza: usa menos del 90%
    TIGHT,      // Ajustado: entre 90% y 100%
    NOT_ENOUGH  // No alcanza: más del 100%
}

data class TripBudgetUiState(
    // Entradas del usuario
    val budgetInput: String = "",
    val people: Int = 1,
    val days: Int = 1,
    val lodgingInput: String = "",
    val foodInput: String = "",
    val includeTransport: Boolean = false,
    val transportInput: String = "",

    // Resultados numéricos
    val lodgingTotal: Double = 0.0,
    val foodTotal: Double = 0.0,
    val transportTotal: Double = 0.0,
    val totalCost: Double = 0.0,
    val difference: Double = 0.0,
    val usedPercent: Double = 0.0,
    val status: BudgetStatus = BudgetStatus.EMPTY,

    // Textos ya formateados para que la UI solo los muestre
    val totalCostText: String = "\$0",
    val differenceLabel: String = TripBudgetTexts.REMAINING,
    val differenceText: String = "\$0",
    val percentText: String = "0%",
    val statusMessage: String = TripBudgetTexts.EMPTY,

    val progress: Float = 0f,
    val lodgingText: String = "\$0",
    val foodText: String = "\$0",
    val transportText: String = "\$0"
)