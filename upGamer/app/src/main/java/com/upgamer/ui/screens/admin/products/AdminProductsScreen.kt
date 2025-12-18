package com.upgamer.ui.screens.admin.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.upgamer.data.api.ProductDto
import com.upgamer.data.repository.ProductRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(
    onCreateProduct: () -> Unit,
    onEditProduct: (Long) -> Unit,
    onBack: () -> Unit
) {
    val repository = remember { ProductRepository() }
    val scope = rememberCoroutineScope()
    var productToDelete by remember { mutableStateOf<ProductDto?>(null) }

    var products by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        repository.getProducts()
            .onSuccess { products = it }
            .onFailure { error = it.message }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin · Productos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F172A)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateProduct) {
                Text("+")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            error?.let {
                Text(it, color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(products) { product ->
                    AdminProductCard(
                        product = product,
                        onDelete = { productToDelete = product }
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

    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que deseas eliminar ${product.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productToDelete = null
                        scope.launch {
                            repository.deleteProduct(product.id)
                                .onSuccess {
                                    products = products.filterNot { it.id == product.id }
                                }
                                .onFailure {
                                    error = it.message
                                }
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun AdminProductCard(
    product: ProductDto,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(product.name, color = Color.White)
            Text("Precio: $${product.price}", color = Color(0xFF38BDF8))
            Text("Stock: ${product.stock}", color = Color.LightGray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}