package com.upchiapas.tripbudget.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private val GreenOk = Color(0xFF2E7D32)
private val AmberTight = Color(0xFFEF8F00)
private val RedBad = Color(0xFFC62828)
private val GrayEmpty = Color(0xFF757575)

fun statusColor(status: BudgetStatus): Color = when (status) {
    BudgetStatus.EMPTY -> GrayEmpty
    BudgetStatus.ENOUGH -> GreenOk
    BudgetStatus.TIGHT -> AmberTight
    BudgetStatus.NOT_ENOUGH -> RedBad
}

fun statusIcon(status: BudgetStatus): ImageVector = when (status) {
    BudgetStatus.EMPTY -> Icons.Filled.Info
    BudgetStatus.ENOUGH -> Icons.Filled.CheckCircle
    BudgetStatus.TIGHT -> Icons.Filled.Warning
    BudgetStatus.NOT_ENOUGH -> Icons.Filled.Close
}