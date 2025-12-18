package com.upgamer.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.upgamer.data.api.OrderItemResponse
import com.upgamer.data.api.OrderResponse
import com.upgamer.data.repository.OrderRepository
import com.upgamer.data.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Long,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val repository = remember { OrderRepository(sessionManager) }

    var order by remember { mutableStateOf<OrderResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(orderId) {
        repository.getOrderDetail(orderId)
            .onSuccess { order = it }
            .onFailure { error = it.message }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de orden", color = Color.White) },
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
                .padding(16.dp)
        ) {

            when {
                isLoading -> CircularProgressIndicator()
                error != null -> Text(error!!, color = Color.Red)
                order == null -> Text("Orden no encontrada")
                else -> {

                    Text(
                        text = "Orden #${order!!.id}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text("Estado: ${order!!.status}")
                    Text("Fecha: ${order!!.createdAt}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Detalle de productos no disponible",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Total: ${order!!.total}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF38BDF8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}