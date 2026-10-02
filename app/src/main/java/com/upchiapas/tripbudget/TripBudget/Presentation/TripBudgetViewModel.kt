package com.upchiapas.tripbudget.TripBudget.Presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import java.util.Locale
import kotlin.math.abs
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
object TripBudgetRules {
    const val TIGHT_THRESHOLD = 90.0   // desde aquí el estado es AJUSTADO
    const val FULL_THRESHOLD = 100.0   // arriba de aquí NO ALCANZA
}

object TripBudgetTexts {
    const val EMPTY = "Ingresa tus datos para calcular"
    const val ENOUGH = "🟢 ALCANZA: tu presupuesto es suficiente"
    const val TIGHT = "🟡 AJUSTADO: casi no te sobra dinero"
    const val NOT_ENOUGH = "🔴 NO ALCANZA"
    const val REMAINING = "Dinero restante"
    const val MISSING = "Dinero faltante"
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

    fun onBudgetChange(value: String) = updateInput { it.copy(budgetInput = value) }

    fun onPeopleChange(value: Float) = updateInput { it.copy(people = value.roundToInt()) }

    fun onDaysChange(value: Float) = updateInput { it.copy(days = value.roundToInt()) }

    fun onLodgingChange(value: String) = updateInput { it.copy(lodgingInput = value) }

    fun onFoodChange(value: String) = updateInput { it.copy(foodInput = value) }

    fun onIncludeTransportChange(value: Boolean) =
        updateInput { it.copy(includeTransport = value) }

    fun onTransportChange(value: String) = updateInput { it.copy(transportInput = value) }

    // Inicia la parte de la lógica se encarga de aplicar el cambio de la entrada, recalcular, emitir el nuevo estado
    private fun updateInput(change: (TripBudgetUiState) -> TripBudgetUiState) {
        _uiState.update { current -> calculate(change(current)) }
    }

    private fun calculate(state: TripBudgetUiState): TripBudgetUiState {
        val budget = state.budgetInput.toDoubleOrNull() ?: 0.0
        val lodgingPerNight = state.lodgingInput.toDoubleOrNull() ?: 0.0
        val foodPerPersonDay = state.foodInput.toDoubleOrNull() ?: 0.0
        val transportCost = if (state.includeTransport) {
            state.transportInput.toDoubleOrNull() ?: 0.0
        } else {
            0.0
        }

        // Fórmulas
        val lodgingTotal = state.days * lodgingPerNight
        val foodTotal = state.people * state.days * foodPerPersonDay
        val totalCost = lodgingTotal + foodTotal + transportCost
        val difference = budget - totalCost
        val usedPercent = if (budget > 0) totalCost / budget * 100 else 0.0

        // Estado del presupuesto
        val status = when {
            budget <= 0.0 -> BudgetStatus.EMPTY
            usedPercent > TripBudgetRules.FULL_THRESHOLD -> BudgetStatus.NOT_ENOUGH
            usedPercent >= TripBudgetRules.TIGHT_THRESHOLD -> BudgetStatus.TIGHT
            else -> BudgetStatus.ENOUGH
        }

        // Textos ya formateados
        val statusMessage = when (status) {
            BudgetStatus.EMPTY -> TripBudgetTexts.EMPTY
            BudgetStatus.ENOUGH -> TripBudgetTexts.ENOUGH
            BudgetStatus.TIGHT -> TripBudgetTexts.TIGHT
            BudgetStatus.NOT_ENOUGH ->
                "${TripBudgetTexts.NOT_ENOUGH}: te faltan ${formatMoney(abs(difference))}"
        }

        return state.copy(
            lodgingTotal = lodgingTotal,
            foodTotal = foodTotal,
            transportTotal = transportCost,
            totalCost = totalCost,
            difference = difference,
            usedPercent = usedPercent,
            status = status,
            totalCostText = formatMoney(totalCost),
            differenceLabel = if (difference >= 0) TripBudgetTexts.REMAINING else TripBudgetTexts.MISSING,
            differenceText = formatMoney(abs(difference)),
            percentText = "${usedPercent.roundToInt()}%",
            statusMessage = statusMessage
        )
    }

    private fun formatMoney(amount: Double): String =
        String.format(Locale.US, "\$%,.0f", amount)
}