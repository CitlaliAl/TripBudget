package com.upchiapas.tripbudget.TripBudget.Presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

//Colors
private val GreenOk = Color(0xFF2E7D32)
private val AmberTight = Color(0xFFEF8F00)
private val RedBad = Color(0xFFC62828)
private val GrayEmpty = Color(0xFF757575)

@Composable
fun TripBudgetVmPage(viewModel: TripBudgetViewModel = viewModel()) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val statusColor = statusColor(state.status)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "✈️ TripBudget",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Datos del viaje
        Text(text = "Datos del viaje", style = MaterialTheme.typography.titleMedium)

        BudgetInputField(
            label = "Presupuesto disponible",
            value = state.budgetInput,
            onValueChange = viewModel::onBudgetChange
        )

        LabeledSlider(
            label = "Personas: ${state.people}",
            value = state.people.toFloat(),
            valueRange = TripBudgetLimits.MIN_PEOPLE.toFloat()..TripBudgetLimits.MAX_PEOPLE.toFloat(),
            onValueChange = viewModel::onPeopleChange
        )

        LabeledSlider(
            label = "Días: ${state.days}",
            value = state.days.toFloat(),
            valueRange = TripBudgetLimits.MIN_DAYS.toFloat()..TripBudgetLimits.MAX_DAYS.toFloat(),
            onValueChange = viewModel::onDaysChange
        )

        BudgetInputField(
            label = "Hospedaje por noche",
            value = state.lodgingInput,
            onValueChange = viewModel::onLodgingChange
        )

        BudgetInputField(
            label = "Comida por persona al día",
            value = state.foodInput,
            onValueChange = viewModel::onFoodChange
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Incluir transporte", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = state.includeTransport,
                onCheckedChange = viewModel::onIncludeTransportChange
            )
        }

        if (state.includeTransport) {
            BudgetInputField(
                label = "Costo del transporte",
                value = state.transportInput,
                onValueChange = viewModel::onTransportChange
            )
        }

        HorizontalDivider()

        //Resultados
        Text(text = "Resultados", style = MaterialTheme.typography.titleMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TripSummaryCard(
                title = "Gasto total",
                value = state.totalCostText,
                modifier = Modifier.weight(1f)
            )
            TripSummaryCard(
                title = state.differenceLabel,
                value = state.differenceText,
                accentColor = statusColor,
                modifier = Modifier.weight(1f)
            )
        }

        BudgetProgress(
            progress = state.progress,
            percentText = state.percentText,
            color = statusColor
        )

        BreakdownCard(
            lodging = state.lodgingText,
            food = state.foodText,
            transport = state.transportText
        )

        BudgetStatusCard(
            message = state.statusMessage,
            icon = statusIcon(state.status),
            color = statusColor
        )
    }
}

private fun statusColor(status: BudgetStatus): Color = when (status) {
    BudgetStatus.EMPTY -> GrayEmpty
    BudgetStatus.ENOUGH -> GreenOk
    BudgetStatus.TIGHT -> AmberTight
    BudgetStatus.NOT_ENOUGH -> RedBad
}

private fun statusIcon(status: BudgetStatus): ImageVector = when (status) {
    BudgetStatus.EMPTY -> Icons.Filled.Info
    BudgetStatus.ENOUGH -> Icons.Filled.CheckCircle
    BudgetStatus.TIGHT -> Icons.Filled.Warning
    BudgetStatus.NOT_ENOUGH -> Icons.Filled.Close
}

// Componentes Reutilizables
@Composable
fun BudgetInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        prefix = { Text("\$") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun LabeledSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = (valueRange.endInclusive - valueRange.start).toInt() - 1
        )
    }
}

// Card pequeña con un título y una cifra
@Composable
fun TripSummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}

// Barra de progreso con el porcentaje utilizado
@Composable
fun BudgetProgress(
    progress: Float,
    percentText: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Presupuesto utilizado", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = percentText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            color = color,
            trackColor = color.copy(alpha = 0.2f),
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        )
    }
}

@Composable
fun BreakdownCard( //el card funciona como un desglose de los datos
    lodging: String,
    food: String,
    transport: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Desglose", style = MaterialTheme.typography.titleSmall)
            BreakdownRow(label = "Hospedaje", value = lodging)
            BreakdownRow(label = "Alimentación", value = food)
            BreakdownRow(label = "Transporte", value = transport)
        }
    }
}

@Composable
fun BreakdownRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

// Función con tarjeta final con ícono, color y mensaje del estado
@Composable
fun BudgetStatusCard(
    message: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
        }
    }
}