package com.upchiapas.tripbudget.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.upchiapas.tripbudget.R

// Logo de la app, cargado desde res/drawable
@Composable
fun TripBudgetLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo_tripbudget),
        contentDescription = "Logo de TripBudget",
        modifier = modifier.height(130.dp)
    )
}