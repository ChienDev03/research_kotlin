package com.example.quickbite.presentation.food

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.CartItem
import com.example.quickbite.data.model.Food
import com.example.quickbite.util.UiState
import com.example.quickbite.ui.components.PrimaryButton
import com.example.quickbite.ui.components.QuantityControl
import com.example.quickbite.ui.theme.AccentAmber
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart
import com.example.quickbite.ui.theme.QuickBiteTheme

@Composable
fun FoodDetailScreen(
    foodId: Int,
    viewModel: FoodDetailViewModel,
    onBack: () -> Unit,
    onAddToCart: (CartItem) -> Unit,
) {
    LaunchedEffect(foodId) {
        viewModel.loadFood(foodId)
    }

    val foodState by viewModel.foodState.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val extraCheese by viewModel.extraCheese.collectAsState()
    val selectedSize by viewModel.selectedSize.collectAsState()
    val totalPrice = viewModel.calculateTotalPrice()

    when (val state = foodState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            }
        }
        is UiState.Success -> {
            FoodDetailContent(
                food = state.data,
                quantity = quantity,
                extraCheese = extraCheese,
                selectedSize = selectedSize,
                totalPrice = totalPrice,
                onBack = onBack,
                onAddToCart = {
                    viewModel.createCartItem()?.let(onAddToCart)
                },
                onSetSize = viewModel::setSize,
                onToggleExtraCheese = viewModel::toggleExtraCheese,
                onIncreaseQuantity = viewModel::increaseQuantity,
                onDecreaseQuantity = viewModel::decreaseQuantity
            )
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodDetailContent(
    food: Food?,
    quantity: Int,
    extraCheese: Boolean,
    selectedSize: String,
    totalPrice: Double,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    onSetSize: (String) -> Unit,
    onToggleExtraCheese: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
) {
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = {
            food?.let {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = "Total Price",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = totalPrice.toCurrencyString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = PrimaryOrange,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        PrimaryButton(
                            text = "Add to Cart",
                            leadingIcon = Icons.Default.ShoppingCart,
                            onClick = onAddToCart,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        food?.let { currentFood ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            ) {
                // Hero Image Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    PrimaryOrangeGradientStart,
                                    PrimaryOrangeGradientEnd,
                                ),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = currentFood.getEmoji(),
                            fontSize = 64.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Title & Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentFood.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryOrange.copy(alpha = 0.12f),
                            ) {
                                Text(
                                    text = currentFood.category,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PrimaryOrange,
                                    fontWeight = FontWeight.Bold,
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AccentAmber,
                                    modifier = Modifier.size(14.dp),
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "4.9 (120+)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }

                    Text(
                        text = currentFood.price.toCurrencyString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = PrimaryOrange,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentFood.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Size Selector
                Text(
                    text = "Select Portion Size",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    listOf("Small", "Regular", "Large").forEach { size ->
                        val isSelected = selectedSize == size
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSetSize(size) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) PrimaryOrange else MaterialTheme.colorScheme.surface,
                            tonalElevation = if (isSelected) 4.dp else 1.dp,
                            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            ),
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = size,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,

                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = when (size) {
                                        "Small" -> "-$1.00"
                                        "Large" -> "+$2.00"
                                        else -> "Standard"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                // Add-ons Section (Burger / Pizza OOP Domain Integration)
                if (currentFood belongsTo "Burger" || currentFood belongsTo "Pizza") {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Customize Add-ons",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onToggleExtraCheese() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (extraCheese) PrimaryOrange.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
                        ),
                        border = if (extraCheese) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryOrange) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🧀", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Extra Melted Cheese",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(
                                        text = if (currentFood.category == "Burger") "+$1.00 via Burger Domain" else "+$1.50 via Pizza Domain",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PrimaryOrange,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            }

                            Checkbox(
                                checked = extraCheese,
                                onCheckedChange = { onToggleExtraCheese() },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = PrimaryOrange,
                                ),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quantity Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Quantity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    QuantityControl(
                        quantity = quantity,
                        onIncrease = onIncreaseQuantity,
                        onDecrease = onDecreaseQuantity,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodDetailPreview() {
    QuickBiteTheme {
        FoodDetailContent(
            food = Food(
                id = 1,
                restaurantId = 1,
                name = "Classic Double Cheeseburger 🍔",
                description = "Double Angus beef patty, cheddar cheese, caramelized onions & secret sauce. Served with a side of crispy fries and a drink.",
                price = 7.99,
                category = "Burger",
            ),
            quantity = 1,
            extraCheese = true,
            selectedSize = "Regular",
            totalPrice = 8.99,
            onBack = {},
            onAddToCart = {},
            onSetSize = {},
            onToggleExtraCheese = {},
            onIncreaseQuantity = {},
            onDecreaseQuantity = {}
        )
    }
}

/**
 * Extension and Infix functions for UI and formatting
 */
infix fun Food.belongsTo(categoryName: String): Boolean = category == categoryName

fun Food.getEmoji(): String {
    return when (category) {
        "Burger" -> "🍔"
        "Pizza" -> "🍕"
        "Noodle" -> "🍜"
        "Side" -> "🍟"
        "Drink" -> "🧋"
        "Dessert" -> "🍰"
        else -> "🍲"
    }
}

fun Double.toCurrencyString(): String {
    return "$${"%.2f".format(this)}"
}