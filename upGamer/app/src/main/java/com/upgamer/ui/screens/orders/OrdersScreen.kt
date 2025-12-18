package com.upgamer.ui.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.upgamer.data.api.OrderResponse
import com.upgamer.data.repository.OrderRepository
import com.upgamer.data.session.SessionManager
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val orderRepository = remember { OrderRepository(sessionManager) }

    var orders by remember { mutableStateOf<List<OrderResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        orderRepository.getMyOrders()
            .onSuccess { result: List<OrderResponse> ->
                orders = result
            }
            .onFailure { throwable: Throwable ->
                error = throwable.message
            }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis órdenes", color = Color.White) },
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
                isLoading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(error ?: "Error", color = Color.Red)
                }

                orders.isEmpty() -> {
                    Text("No tienes órdenes aún")
                }

                else -> {
                    LazyColumn {
                        items(orders) { order ->
                            OrderCard(
                                order = order,
                                onClick = {
                                    onOrderClick(order.id)
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
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

@Composable
private fun OrderCard(
    order: OrderResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Orden #${order.id}", color = Color.White)
            Text("Estado: ${order.status}", color = Color.LightGray)
            Text("Fecha: ${order.createdAt}", color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total: $${order.total}",
                color = Color(0xFF38BDF8),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}