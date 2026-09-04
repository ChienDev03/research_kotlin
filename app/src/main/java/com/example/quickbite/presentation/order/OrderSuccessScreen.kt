package com.example.quickbite.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.Order
import com.example.quickbite.ui.components.PrimaryButton
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.SuccessGreen
import com.example.quickbite.ui.theme.SuccessGreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSuccessScreen(
    order: Order? = null,
    onTrackOrder: () -> Unit,
    onBackHome: () -> Unit,
) {
    val orderId = order?.id ?: "#QB-1024"
    val totalAmount = order?.total ?: 15.99
    val paymentName = order?.paymentMethod ?: "Cash on Delivery"
    val itemCount = order?.items?.sumOf { it.quantity } ?: 1

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Glowing Celebratory Checkmark Icon
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(SuccessGreenLight),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                        .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = SuccessGreen, spotColor = SuccessGreen),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Order Placed Successfully! 🎉",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your $itemCount items are being freshly prepared and will be delivered shortly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Order Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "ORDER CODE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryOrange.copy(alpha = 0.12f),
                        ) {
                            Text(
                                text = orderId,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = PrimaryOrange,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            tint = PrimaryOrange,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Paid Amount & Method",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "$${"%.2f".format(totalAmount)} • $paymentName",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = PrimaryOrange,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Estimated Delivery Time",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "20 - 30 minutes (Around 12:45 PM)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrimaryOrange,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Delivery Address",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "123 Nguyen Trai, District 1, HCMC",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            PrimaryButton(
                text = "Track My Order",
                leadingIcon = Icons.Default.DeliveryDining,
                onClick = onTrackOrder,
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Back to Home",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
