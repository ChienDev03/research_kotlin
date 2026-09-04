package com.example.quickbite.presentation.order

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.Order
import com.example.quickbite.data.model.OrderStatus
import com.example.quickbite.ui.theme.AccentAmber
import com.example.quickbite.ui.theme.AccentYellowLight
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart
import com.example.quickbite.ui.theme.SuccessGreen
import com.example.quickbite.ui.theme.SuccessGreenLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    order: Order? = null,
    onBack: () -> Unit,
) {
    val orderId = order?.id ?: "#QB-1024"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Track Order",
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = orderId,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
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
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier.padding(end = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = SuccessGreenLight,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ON TIME",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
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
            // 1. Estimated Arrival Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        PrimaryOrangeGradientStart,
                                        PrimaryOrangeGradientEnd,
                                    ),
                                ),
                            )
                            .padding(20.dp),
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column {
                                    Text(
                                        text = "ESTIMATED ARRIVAL",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "20 - 25 min",
                                        color = Color.White,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = "🛵", fontSize = 28.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            LinearProgressIndicator(
                                progress = { 0.65f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.3f),
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Driver is on the way to pick up your order",
                                color = Color.White.copy(alpha = 0.95f),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }

            // 2. Driver Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(PrimaryOrange.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = "👨🏻‍🍳", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Alex Nguyen",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AccentAmber,
                                    modifier = Modifier.size(14.dp),
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "4.9 (520 deliveries)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                text = "Honda Wave • 59-F1 888.88",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimaryOrange,
                                fontWeight = FontWeight.Medium,
                            )
                        }

                        // Call & Chat buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreenLight)
                                    .clickable { /* Call driver */ },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call",
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp),
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryOrange.copy(alpha = 0.12f))
                                    .clickable { /* Chat driver */ },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "Chat",
                                    tint = PrimaryOrange,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }
                }
            }

            // 3. Order Progress Step Timeline
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Order Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        TimelineStep(
                            icon = Icons.Default.ReceiptLong,
                            title = "Order Confirmed",
                            subtitle = "We have received your order",
                            time = "12:15 PM",
                            isCompleted = true,
                            isCurrent = false,
                            isLast = false,
                        )

                        TimelineStep(
                            icon = Icons.Default.Fastfood,
                            title = "Restaurant Preparing",
                            subtitle = "Kitchen is cooking your meals",
                            time = "12:20 PM",
                            isCompleted = true,
                            isCurrent = false,
                            isLast = false,
                        )

                        TimelineStep(
                            icon = Icons.Default.DeliveryDining,
                            title = "Driver Picking Up",
                            subtitle = "Driver has arrived at the restaurant",
                            time = "12:30 PM",
                            isCompleted = true,
                            isCurrent = true,
                            isLast = false,
                        )

                        TimelineStep(
                            icon = Icons.Default.DeliveryDining,
                            title = "On the Way",
                            subtitle = "Your order is headed to your address",
                            time = "Est. 12:40 PM",
                            isCompleted = false,
                            isCurrent = false,
                            isLast = false,
                        )

                        TimelineStep(
                            icon = Icons.Default.Home,
                            title = "Delivered",
                            subtitle = "Enjoy your meal!",
                            time = "Est. 12:45 PM",
                            isCompleted = false,
                            isCurrent = false,
                            isLast = true,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimelineStep(
    icon: ImageVector,
    title: String,
    subtitle: String,
    time: String,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Left Column: Dot & Connector Line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> PrimaryOrange
                            isCompleted -> SuccessGreen
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted && !isCurrent) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                } else if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(38.dp)
                        .background(
                            if (isCompleted && !isCurrent) SuccessGreen else MaterialTheme.colorScheme.outline.copy(
                                alpha = 0.3f
                            ),
                        ),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Step Info
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCurrent) PrimaryOrange else MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrent) PrimaryOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                )
            }

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            if (!isLast) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
