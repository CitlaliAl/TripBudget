package com.upchiapas.tripbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.upchiapas.tripbudget.presentation.TripBudgetVmPage
import com.upchiapas.tripbudget.ui.theme.TripBudgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripBudgetTheme {
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    TripBudgetVmPage()
                }
            }
        }
    }
}