package com.example.quickbite.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.CartItem
import com.example.quickbite.data.model.Food
import com.example.quickbite.ui.components.EmptyStateView
import com.example.quickbite.ui.components.PrimaryButton
import com.example.quickbite.ui.components.QuantityControl
import com.example.quickbite.ui.theme.ErrorRed
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.QuickBiteTheme
import com.example.quickbite.ui.theme.SuccessGreen

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
) {
    val items by viewModel.items.collectAsState()

    CartScreenContent(
        items = items,
        onIncrease = { viewModel.increase(it) },
        onDecrease = { viewModel.decrease(it) },
        onClear = { viewModel.clear() },
        onBack = onBack,
        onCheckout = onCheckout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenContent(
    items: List<CartItem>,
    onIncrease: (CartItem) -> Unit,
    onDecrease: (CartItem) -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var promoCode by remember { mutableStateOf("") }
    var isPromoApplied by remember { mutableStateOf(false) }

    val subtotal = items.sumOf { it.calculatePrice() }
    val deliveryFee = if (items.isEmpty()) 0.0 else 2.0
    val total = subtotal + deliveryFee

    Scaffold(
        modifier = modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "My Cart",
                            fontWeight = FontWeight.Bold,
                        )
                        if (items.isNotEmpty()) {
                            Text(
                                text = "${items.sumOf { it.quantity }} items",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
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
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                actions = {
                    if (items.isNotEmpty()) {
                        IconButton(onClick = onClear) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear Cart",
                                tint = ErrorRed,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    shadowElevation = 12.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                    ) {
                        PrimaryButton(
                            text = "Proceed to Checkout • $${"%.2f".format(total)}",
                            leadingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                            onClick = onCheckout,
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        if (items.isEmpty()) {
            EmptyStateView(
                emoji = "🛒",
                title = "Your Cart is Empty",
                description = "Looks like you haven't added any delicious food yet. Explore restaurants and satisfy your cravings!",
                buttonText = "Explore Food",
                onButtonClick = onBack,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Cart Items List
                items(items, key = { it.food.id }) { item ->
                    CartItemCard(
                        item = item,
                        onIncrease = { onIncrease(item) },
                        onDecrease = { onDecrease(item) },
                    )
                }

                // Promo Code Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedTextField(
                                value = promoCode,
                                onValueChange = { promoCode = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text(
                                        text = "Enter promo code",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocalOffer,
                                        contentDescription = "Promo",
                                        tint = PrimaryOrange,
                                        modifier = Modifier.size(20.dp),
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryOrange,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                ),
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            TextButton(
                                onClick = { isPromoApplied = true },
                                enabled = promoCode.isNotBlank(),
                            ) {
                                Text(
                                    text = if (isPromoApplied) "Applied" else "Apply",
                                    color = if (isPromoApplied) SuccessGreen else PrimaryOrange,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                // Bill Summary Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            BillRow(label = "Subtotal", amount = "$${"%.2f".format(subtotal)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            BillRow(label = "Delivery Fee", amount = "$${"%.2f".format(deliveryFee)}")

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Total Amount",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )

                                Text(
                                    text = "$${"%.2f".format(total)}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = PrimaryOrange,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    val foodEmoji = when (item.food.category) {
        "Burger" -> "🍔"
        "Pizza" -> "🍕"
        "Noodle" -> "🍜"
        "Side" -> "🍟"
        "Drink" -> "🧋"
        "Dessert" -> "🍰"
        else -> "🍲"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Food Thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = foodEmoji,
                    fontSize = 32.sp,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Food Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.food.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$${"%.2f".format(item.food.price)} each",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$${"%.2f".format(item.calculatePrice())}",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryOrange,
                    fontWeight = FontWeight.Bold,
                )
            }

            // Quantity Control
            QuantityControl(
                quantity = item.quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease,
            )
        }
    }
}

@Composable
private fun BillRow(
    label: String,
    amount: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    QuickBiteTheme {
        CartScreenContent(
            items = listOf(
                CartItem(
                    food = Food(
                        id = 1,
                        restaurantId = 1,
                        name = "Special Cheese Burger",
                        description = "Juicy beef patty with cheese",
                        price = 8.99,
                        category = "Burger"
                    ),
                    quantity = 2
                ),
                CartItem(
                    food = Food(
                        id = 2,
                        restaurantId = 1,
                        name = "Pepperoni Pizza",
                        description = "Classic pepperoni pizza",
                        price = 12.50,
                        category = "Pizza"
                    ),
                    quantity = 1
                )
            ),
            onIncrease = {},
            onDecrease = {},
            onClear = {},
            onBack = {},
            onCheckout = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenEmptyPreview() {
    QuickBiteTheme {
        CartScreenContent(
            items = emptyList(),
            onIncrease = {},
            onDecrease = {},
            onClear = {},
            onBack = {},
            onCheckout = {}
        )
    }
}
