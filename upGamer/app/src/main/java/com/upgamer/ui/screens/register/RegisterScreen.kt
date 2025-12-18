package com.upgamer.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.upgamer.data.repository.AuthRepository
import com.upgamer.ui.screens.ScreenBase
import com.upgamer.ui.screens.fieldColors
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidPhone(phone: String): Boolean {
    return phone.length >= 8 && phone.all { it.isDigit() || it == '+' }
}

private fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}

@Composable
fun RegisterScreen(
    onGoToLogin: () -> Unit
) {
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    ScreenBase(
        title = "Crear Cuenta",
        actionText = "¿Ya tienes cuenta? Inicia sesión",
        onActionClick = onGoToLogin
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
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
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

                when {
                    name.isBlank() -> {
                        errorMessage = "Ingresa tu nombre"
                        return@Button
                    }
                    rut.isBlank() -> {
                        errorMessage = "Ingresa tu RUT"
                        return@Button
                    }
                    email.isBlank() -> {
                        errorMessage = "Ingresa tu correo"
                        return@Button
                    }
                    !isValidEmail(email) -> {
                        errorMessage = "Correo inválido"
                        return@Button
                    }
                    phone.isBlank() -> {
                        errorMessage = "Ingresa tu teléfono"
                        return@Button
                    }
                    !isValidPhone(phone) -> {
                        errorMessage = "Teléfono inválido"
                        return@Button
                    }
                    password.isBlank() -> {
                        errorMessage = "Ingresa una contraseña"
                        return@Button
                    }
                    !isValidPassword(password) -> {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        return@Button
                    }
                    password != confirmPassword -> {
                        errorMessage = "Las contraseñas no coinciden"
                        return@Button
                    }
                }

                scope.launch {
                    isLoading = true
                    val result = authRepository.register(
                        nombre = name,
                        rut = rut,
                        email = email,
                        password = password,
                        phone = phone
                    )
                    isLoading = false

                    result
                        .onSuccess {
                            successMessage = "Registro exitoso 🎉"
                            scope.launch {
                                delay(1000)
                                onGoToLogin()
                            }
                        }
                        .onFailure { error ->
                            errorMessage = error.message ?: "Error al registrar"
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
                Text("REGISTRARSE")
            }
        }
    }
}