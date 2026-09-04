package com.example.quickbite.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.CartItem
import com.example.quickbite.ui.components.PrimaryButton
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.SuccessGreen
import com.example.quickbite.ui.theme.SuccessGreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    items: List<CartItem>,
    viewModel: CheckoutViewModel,
    onBack: () -> Unit,
    onOrderSuccess: (com.example.quickbite.data.model.Order) -> Unit,
) {
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val discountCalculator by viewModel.discountCalculator.collectAsState()

    val subtotal = items.sumOf { it.calculatePrice() }
    val deliveryFee = if (items.isEmpty()) 0.0 else 2.0
    val discount = discountCalculator.calculateDiscount(subtotal)
    val total = (subtotal + deliveryFee - discount).coerceAtLeast(0.0)

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
        bottomBar = {
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
                        text = "Place Order • $${"%.2f".format(total)}",
                        leadingIcon = Icons.Default.Lock,
                        onClick = {
                            val order = viewModel.placeOrder(items)
                            if (order != null) {
                                onOrderSuccess(order)
                            }
                        },
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 1. Delivery Address Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Delivery Address",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = PrimaryOrange,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { /* Edit address */ },
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryOrange.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(24.dp),
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Home • 123 Nguyen Trai",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Ben Thanh Ward, District 1, Ho Chi Minh City",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "Alex Nguyen • (+84) 901 234 567",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PrimaryOrange,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }

            // 2. Payment Method Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Payment Method",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        PaymentOptionCard(
                            title = "Cash on Delivery",
                            subtitle = "Pay with cash upon arrival",
                            icon = Icons.Default.LocalAtm,
                            selected = paymentMethod.displayName() == "Cash",
                            onClick = viewModel::selectCash,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentOptionCard(
                            title = "Credit / Debit Card",
                            subtitle = "Visa, Mastercard, JCB (**** 4242)",
                            icon = Icons.Default.CreditCard,
                            selected = paymentMethod.displayName() == "Credit Card",
                            onClick = viewModel::selectCard,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentOptionCard(
                            title = "E-Wallet",
                            subtitle = "MoMo, ZaloPay, ShopeePay",
                            icon = Icons.Default.AccountBalanceWallet,
                            selected = paymentMethod.displayName() == "E-Wallet",
                            onClick = viewModel::selectWallet,
                        )
                    }
                }
            }

            // 3. Discount Offers Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Apply Promo & Discounts",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = PrimaryOrange,
                                modifier = Modifier.size(20.dp),
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val currentDesc = discountCalculator.getDescription()

                        DiscountOptionChip(
                            title = "Normal Discount (10% OFF)",
                            badge = "10% OFF",
                            selected = currentDesc.contains("10") || currentDesc.contains("Normal"),
                            onClick = viewModel::selectNormalDiscount,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DiscountOptionChip(
                            title = "Premium Member (20% OFF)",
                            badge = "20% OFF",
                            selected = currentDesc.contains("20") || currentDesc.contains("Premium"),
                            onClick = viewModel::selectPremiumDiscount,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DiscountOptionChip(
                            title = "Flash Sale Deal (30% OFF)",
                            badge = "🔥 30% OFF",
                            selected = currentDesc.contains("30") || currentDesc.contains("Flash"),
                            onClick = viewModel::selectFlashSale,
                        )
                    }
                }
            }

            // 4. Detailed Order Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Payment Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        SummaryRow(
                            label = "Subtotal (${items.sumOf { it.quantity }} items)",
                            amount = "$${"%.2f".format(subtotal)}"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SummaryRow(label = "Delivery Fee", amount = "$${"%.2f".format(deliveryFee)}")

                        if (discount > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            SummaryRow(
                                label = "Discount (${discountCalculator.getDescription()})",
                                amount = "-$${"%.2f".format(discount)}",
                                isDiscount = true,
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Total Payable",
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

@Composable
private fun PaymentOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) PrimaryOrange.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, PrimaryOrange) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (selected) PrimaryOrange else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = PrimaryOrange),
            )
        }
    }
}

@Composable
private fun DiscountOptionChip(
    title: String,
    badge: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) PrimaryOrange.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = if (selected) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryOrange) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) PrimaryOrange else MaterialTheme.colorScheme.onSurface,
            )

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (selected) PrimaryOrange else SuccessGreenLight,
            ) {
                Text(
                    text = badge,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) Color.White else SuccessGreen,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: String,
    isDiscount: Boolean = false,
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
            color = if (isDiscount) SuccessGreen else MaterialTheme.colorScheme.onSurface,
        )
    }
}
