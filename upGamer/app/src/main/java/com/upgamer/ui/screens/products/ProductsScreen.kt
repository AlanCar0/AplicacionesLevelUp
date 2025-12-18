package com.upgamer.ui.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.upgamer.data.api.ProductDto
import com.upgamer.data.cart.CartManager
import com.upgamer.data.repository.ProductRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.upgamer.R
import androidx.compose.material.icons.filled.ArrowBack
import com.upgamer.ui.components.CartBadge


fun formatPrice(price: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return formatter.format(price)
}
private fun canAddMore(product: ProductDto): Boolean {
    val quantityInCart = CartManager.items
        .find { it.product.id == product.id }
        ?.quantity ?: 0

    return quantityInCart < product.stock
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onBack: () -> Unit
) {
    val repo = remember { ProductRepository() }
    val scope = rememberCoroutineScope()

    var products by remember { mutableStateOf<List<ProductDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // 🔔 Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        repo.getProducts()
            .onSuccess {
                products = it
                error = null
            }
            .onFailure {
                error = it.message
            }
        isLoading = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Productos",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    CartBadge(
                        count = CartManager.totalItems(),
                        onClick = { onBack() }
                    )
                },
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

            Text(
                text = "Productos",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(error ?: "Error", color = MaterialTheme.colorScheme.error)
                }

                else -> {
                    LazyColumn {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = {
                                    CartManager.addProduct(product)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Producto agregado al carrito 🛒"
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
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
private fun ProductCard(
    product: ProductDto,
    onAddToCart: () -> Unit
) {
    val canAdd = canAddMore(product)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {

            // 🖼️ IMAGEN
            if (!product.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        ),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(
                        id = R.drawable.placeholder_image
                    ),
                    error = painterResource(
                        id = R.drawable.image_error
                    )
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // 🏷️ NOMBRE
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 📦 STOCK
                Text(
                    text = "Stock disponible: ${product.stock}",
                    color = if (canAdd) Color.LightGray else Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 💲 PRECIO
                Text(
                    text = formatPrice(product.price),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF38BDF8)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🛒 BOTÓN
                Button(
                    onClick = {
                        if (canAdd) {
                            onAddToCart()
                        }
                    },
                    enabled = canAdd,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canAdd)
                            Color(0xFF2563EB)
                        else
                            Color.DarkGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (canAdd) "Agregar al carrito" else "Stock agotado",
                        color = Color.White
                    )
                }
            }
        }
    }
}