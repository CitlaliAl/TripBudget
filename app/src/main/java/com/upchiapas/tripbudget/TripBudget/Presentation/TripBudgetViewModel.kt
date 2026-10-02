package com.upchiapas.tripbudget.TripBudget.Presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
enum class BudgetStatus { // Los cuatro estados posibles del presupuesto.
    EMPTY,      // todavía no hay datos suficientes
    ENOUGH,     // Alcanza: usa menos del 90%
    TIGHT,      // Ajustado: entre 90% y 100%
    NOT_ENOUGH  // No alcanza: más del 100%
}
object TripBudgetLimits {
    const val MIN_PEOPLE = 1
    const val MAX_PEOPLE = 10
    const val MIN_DAYS = 1
    const val MAX_DAYS = 30
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
    val totalCostText: String = "$0",
    val differenceLabel: String = "Dinero restante",
    val differenceText: String = "$0",
    val percentText: String = "0%",
    val statusMessage: String = "Ingresa tus datos para calcular"
)

class TripBudgetViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TripBudgetUiState())
    val uiState: StateFlow<TripBudgetUiState> = _uiState.asStateFlow()

    // Eventos en los que la UI los llama

    fun onBudgetChange(value: String) {
        _uiState.update { it.copy(budgetInput = value) }
    }

    fun onPeopleChange(value: Float) {
        _uiState.update { it.copy(people = value.roundToInt()) }
    }

    fun onDaysChange(value: Float) {
        _uiState.update { it.copy(days = value.roundToInt()) }
    }

    fun onLodgingChange(value: String) {
        _uiState.update { it.copy(lodgingInput = value) }
    }

    fun onFoodChange(value: String) {
        _uiState.update { it.copy(foodInput = value) }
    }

    fun onIncludeTransportChange(value: Boolean) {
        _uiState.update { it.copy(includeTransport = value) }
    }

    fun onTransportChange(value: String) {
        _uiState.update { it.copy(transportInput = value) }
    }
}