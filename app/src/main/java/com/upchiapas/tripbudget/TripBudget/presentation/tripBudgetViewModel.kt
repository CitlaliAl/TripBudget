package com.upchiapas.tripbudget.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt
import java.util.Locale
import kotlin.math.abs

class TripBudgetViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TripBudgetUiState())
    val uiState: StateFlow<TripBudgetUiState> = _uiState.asStateFlow()

    // Eventos en los que la UI los llama

    fun onBudgetChange(value: String) = updateInput { it.copy(budgetInput = value) } //it es el estado actual y copy crea una copia con un campo cambiado

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

        // Estado del presupuesto, aqui evita dividir entre cero
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
            statusMessage = statusMessage,
            progress = (usedPercent / 100).coerceIn(0.0, 1.0).toFloat(),
            lodgingText = formatMoney(lodgingTotal),
            foodText = formatMoney(foodTotal),
            transportText = formatMoney(transportCost)
        )
    }

    private fun formatMoney(amount: Double): String =
        String.format(Locale.US, "\$%,.0f", amount)
}