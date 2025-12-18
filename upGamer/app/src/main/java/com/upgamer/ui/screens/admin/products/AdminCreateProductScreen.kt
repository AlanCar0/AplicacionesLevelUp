package com.upgamer.ui.screens.admin.products

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upgamer.data.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateProductScreen(
    onProductCreated: () -> Unit,
    onBack: () -> Unit
) {
    val repository = remember { ProductRepository() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var image by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear producto") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth()
            )

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank() || stock.isBlank()) {
                        error = "Completa todos los campos"
                        return@Button
                    }

                    val priceValue = price.toDoubleOrNull()
                    val stockValue = stock.toIntOrNull()

                    if (priceValue == null || stockValue == null) {
                        error = "Precio y stock deben ser números válidos"
                        return@Button
                    }

                    if (priceValue < 0 || stockValue < 0) {
                        error = "Precio y stock no pueden ser negativos"
                        return@Button
                    }

                    scope.launch {
                        repository.createProduct(
                            name = name,
                            price = priceValue,
                            stock = stockValue,
                            image = image.ifBlank { null },
                            category = category.ifBlank { null },
                            description = description.ifBlank { null }
                        ).onSuccess {
                            onProductCreated()
                        }.onFailure {
                            error = it.message
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}