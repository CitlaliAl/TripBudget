package com.upchiapas.tripbudget.presentation

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
    const val ENOUGH = "ALCANZA: tu presupuesto es suficiente"
    const val TIGHT = "AJUSTADO: casi no te sobra dinero"
    const val NOT_ENOUGH = "¡No alcanza!"
    const val REMAINING = "Dinero restante"
    const val MISSING = "Dinero faltante"
}