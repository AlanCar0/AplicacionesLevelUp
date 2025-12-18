package com.upgamer.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onGoToProductsAdmin: () -> Unit,
    onGoToOrdersAdmin: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Admin", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = onGoToProductsAdmin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📦 Gestionar productos")
            }

            Button(
                onClick = onGoToOrdersAdmin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📋 Ver órdenes")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF475569)
                )
            ) {
                Text("Volver")
            }
        }
    }
}