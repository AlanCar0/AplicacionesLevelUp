package com.upgamer.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.upgamer.data.repository.AuthRepository
import com.upgamer.data.session.SessionManager
import com.upgamer.ui.screens.ScreenBase
import com.upgamer.ui.screens.fieldColors
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository() }
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    ScreenBase(
        title = "Iniciar Sesión",
        actionText = "¿No tienes cuenta? Regístrate",
        onActionClick = onGoToRegister
    ) {

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        successMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errorMessage = null
                successMessage = null

                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Completa todos los campos"
                    return@Button
                }

                scope.launch {
                    isLoading = true
                    val result = authRepository.login(email, password)
                    isLoading = false

                    result
                        .onSuccess { response ->
                            sessionManager.saveSession(
                                token = response.token,
                                userId = response.userId
                            )
                            sessionManager.saveRole(response.role)
                            successMessage = "Inicio de sesión exitoso"
                            onLoginSuccess()
                        }
                        .onFailure { error ->
                            errorMessage = error.message ?: "Error al iniciar sesión"
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("INICIAR SESIÓN")
            }
        }
    }
}