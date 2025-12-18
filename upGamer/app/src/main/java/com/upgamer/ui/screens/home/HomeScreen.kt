package com.upgamer.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.upgamer.data.cart.CartManager
import com.upgamer.data.session.SessionManager
import com.upgamer.ui.components.CartBadge
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onGoToProducts: () -> Unit,
    onGoToCart: () -> Unit,
    onGoToProfile: () -> Unit,
    onGoToAdmin: () -> Unit   // ✅ ADMIN
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val isAdmin = sessionManager.getRole() == "ROLE_ADMIN"
    LaunchedEffect(Unit) {
        println("ROLE GUARDADO EN SESSION: ${sessionManager.getRole()}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // fondo gamer oscuro
            .padding(16.dp)
    ) {

        // 🔥 HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "UpGamer 🎮",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            CartBadge(
                count = CartManager.totalItems(),
                onClick = onGoToCart
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 🧩 BOTONES PRINCIPALES
        HomeButton(text = "🛒 Productos") {
            onGoToProducts()
        }

        Spacer(modifier = Modifier.height(16.dp))

        HomeButton(text = "🧺 Carrito") {
            onGoToCart()
        }

        Spacer(modifier = Modifier.height(16.dp))

        HomeButton(text = "👤 Perfil") {
            onGoToProfile()
        }

        // 🛠️ ADMIN
        if (isAdmin) {
            Spacer(modifier = Modifier.height(16.dp))

            HomeButton(text = "🛠️ Panel Admin") {
                onGoToAdmin()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🚪 LOGOUT
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDC2626)
            )
        ) {
            Text("Cerrar sesión", color = Color.White)
        }
    }
}

@Composable
private fun HomeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB)
        )
    ) {
        Text(text, color = Color.White)
    }
}