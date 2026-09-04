package com.example.quickbite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quickbite.demo.StageDemoScreen
import com.example.quickbite.presentation.cart.CartScreen
import com.example.quickbite.presentation.cart.CartViewModel
import com.example.quickbite.presentation.checkout.CheckoutScreen
import com.example.quickbite.presentation.checkout.CheckoutViewModel
import com.example.quickbite.presentation.food.FoodDetailScreen
import com.example.quickbite.presentation.food.FoodDetailViewModel
import com.example.quickbite.presentation.home.HomeScreen
import com.example.quickbite.presentation.home.HomeViewModel
import com.example.quickbite.presentation.order.OrderSuccessScreen
import com.example.quickbite.presentation.order.OrderTrackingScreen
import com.example.quickbite.presentation.restaurant.RestaurantScreen
import com.example.quickbite.presentation.restaurant.RestaurantViewModel
import com.example.quickbite.ui.theme.QuickBiteTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuickBiteTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()
                val cartItems by cartViewModel.items.collectAsState()
                val cartItemCount = cartItems.sumOf { it.quantity }

                val checkoutViewModel: CheckoutViewModel = viewModel()
                val lastOrder by checkoutViewModel.lastOrder.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    // -------------------------
                    // HOME
                    // -------------------------
                    composable("home") {
                        val homeViewModel: HomeViewModel = viewModel()

                        HomeScreen(
                            viewModel = homeViewModel,
                            cartItemCount = cartItemCount,
                            onRestaurantClick = { restaurantId ->
                                navController.navigate("restaurant/$restaurantId")
                            },
                            onCartClick = {
                                navController.navigate("cart")
                            },
                            onLocationClick = {
                                navController.navigate("demo_stage")
                            }
                        )
                    }

                    composable("demo_stage") {
                        StageDemoScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    // -------------------------
                    // RESTAURANT
                    // -------------------------
                    composable(
                        route = "restaurant/{restaurantId}",
                        arguments = listOf(
                            navArgument("restaurantId") {
                                type = NavType.IntType
                            },
                        ),
                    ) { backStackEntry ->
                        val restaurantId = backStackEntry.arguments?.getInt("restaurantId") ?: return@composable
                        val viewModel: RestaurantViewModel = viewModel()

                        RestaurantScreen(
                            restaurantId = restaurantId,
                            viewModel = viewModel,
                            onBack = {
                                navController.popBackStack()
                            },
                            onFoodClick = { foodId ->
                                navController.navigate("food/$foodId")
                            },
                        )
                    }

                    // -------------------------
                    // FOOD DETAIL
                    // -------------------------
                    composable(
                        route = "food/{foodId}",
                        arguments = listOf(
                            navArgument("foodId") {
                                type = NavType.IntType
                            },
                        ),
                    ) { backStackEntry ->
                        val foodId = backStackEntry.arguments?.getInt("foodId") ?: return@composable
                        val viewModel: FoodDetailViewModel = viewModel()

                        FoodDetailScreen(
                            foodId = foodId,
                            viewModel = viewModel,
                            onBack = {
                                navController.popBackStack()
                            },
                            onAddToCart = { item ->
                                cartViewModel.addItem(item)
                                navController.navigate("cart")
                            },
                        )
                    }

                    // -------------------------
                    // CART
                    // -------------------------
                    composable("cart") {
                        CartScreen(
                            viewModel = cartViewModel,
                            onBack = {
                                navController.popBackStack()
                            },
                            onCheckout = {
                                navController.navigate("checkout")
                            },
                        )
                    }

                    // -------------------------
                    // CHECKOUT
                    // -------------------------
                    composable("checkout") {
                        val items = cartViewModel.items.collectAsState().value

                        CheckoutScreen(
                            items = items,
                            viewModel = checkoutViewModel,
                            onBack = {
                                navController.popBackStack()
                            },
                            onOrderSuccess = { _ ->
                                cartViewModel.clear()
                                navController.navigate("order-success") {
                                    popUpTo("home")
                                }
                            },
                        )
                    }

                    // -------------------------
                    // ORDER SUCCESS
                    // -------------------------
                    composable("order-success") {
                        OrderSuccessScreen(
                            order = lastOrder,
                            onTrackOrder = {
                                navController.navigate("order-tracking")
                            },
                            onBackHome = {
                                navController.navigate("home") {
                                    popUpTo("home") {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    }

                    // -------------------------
                    // ORDER TRACKING
                    // -------------------------
                    composable("order-tracking") {
                        OrderTrackingScreen(
                            order = lastOrder,
                            onBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}