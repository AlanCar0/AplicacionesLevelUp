package com.upgamer.ui.screens.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.upgamer.data.cart.CartManager
import com.upgamer.data.repository.OrderRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.upgamer.data.session.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit
) {
    val items = CartManager.items
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val orderRepository = remember { OrderRepository(sessionManager) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Carrito", color = Color.White) },
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

            if (items.isEmpty()) {
                Text(
                    text = "El carrito está vacío",
                    style = MaterialTheme.typography.titleMedium
                )
            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF0F172A)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(item.product.name, color = Color.White)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Cantidad: ${item.quantity}",
                                    color = Color.LightGray
                                )
                                Text(
                                    "Subtotal: $${item.product.price * item.quantity}",
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Total: $${CartManager.total()}",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope.launch {
                            orderRepository.createOrder()
                                .onSuccess {
                                    CartManager.clear()
                                    snackbarHostState.showSnackbar(
                                        "✅ Orden creada correctamente"
                                    )
                                }
                                .onFailure {
                                    snackbarHostState.showSnackbar(
                                        it.message ?: "❌ Error al crear la orden"
                                    )
                                }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E)
                    )
                ) {
                    Text("Realizar pago", color = Color.White)
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