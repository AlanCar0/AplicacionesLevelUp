package com.upgamer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CartBadge(
    count: Int,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )

            if (count > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-4).dp)
                        .size(18.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}