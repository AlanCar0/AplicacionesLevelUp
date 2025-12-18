package com.upgamer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.upgamer.data.session.SessionManager
import com.upgamer.ui.screens.admin.AdminScreen
import com.upgamer.ui.screens.cart.CartScreen
import com.upgamer.ui.screens.home.HomeScreen
import com.upgamer.ui.screens.login.LoginScreen
import com.upgamer.ui.screens.orders.OrderDetailScreen
import com.upgamer.ui.screens.orders.OrdersScreen
import com.upgamer.ui.screens.products.ProductsScreen
import com.upgamer.ui.screens.profile.ProfileScreen
import com.upgamer.ui.screens.register.RegisterScreen
import com.upgamer.ui.screens.admin.products.AdminProductsScreen
import com.upgamer.ui.screens.admin.products.AdminCreateProductScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // 🔐 AUTO LOGIN
    LaunchedEffect(Unit) {
        if (sessionManager.getToken() != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                onGoToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onGoToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    sessionManager.clear()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onGoToProducts = {
                    navController.navigate("products")
                },
                onGoToCart = {
                    navController.navigate("cart")
                },
                onGoToProfile = {
                    navController.navigate("profile")
                },
                onGoToAdmin = {
                    navController.navigate("admin")
                }
            )
        }

        composable("products") {
            ProductsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("cart") {
            CartScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                onOrdersClick = {
                    navController.navigate("orders")
                },
                onLogout = {
                    sessionManager.clear()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("orders") {
            OrdersScreen(
                onBack = { navController.popBackStack() },
                onOrderClick = { orderId ->
                    navController.navigate("order_detail/$orderId")
                }
            )
        }

        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val orderId =
                backStackEntry.arguments?.getLong("orderId")
                    ?: return@composable

            OrderDetailScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("admin") {
            AdminScreen(
                onGoToProductsAdmin = {
                    navController.navigate("admin_products")
                },
                onGoToOrdersAdmin = {
                    navController.navigate("admin_orders")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("admin_products") {
            AdminProductsScreen(
                onCreateProduct = {
                    navController.navigate("admin_products_create")
                },
                onEditProduct = { productId ->
                    navController.navigate("admin_products_edit/$productId")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("admin_products_create") {
            AdminCreateProductScreen(
                onProductCreated = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}