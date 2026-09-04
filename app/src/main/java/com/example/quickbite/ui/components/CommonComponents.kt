package com.example.quickbite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.ui.theme.AccentAmber
import com.example.quickbite.ui.theme.AccentYellowLight
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart
import com.example.quickbite.ui.theme.SuccessGreen
import com.example.quickbite.ui.theme.SuccessGreenLight

@Composable
fun QuickBiteHeader(
    location: String = "18 Le Loi, Hue City, Thua Thien Hue",
    cartItemCount: Int = 0,
    onCartClick: () -> Unit,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onLocationClick)
                .padding(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "DELIVER TO",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change location",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = location,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onCartClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(22.dp),
            )

            if (cartItemCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = 10.dp, y = (-10).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(PrimaryOrange)
                        .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (cartItemCount > 99) "99+" else cartItemCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun QuickBiteSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search delicious food, burgers...",
    onFilterClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp)),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = PrimaryOrange,
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = PrimaryOrange,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            ),
        )

        if (onFilterClick != null) {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryOrange)
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

data class CategoryItem(
    val id: String,
    val name: String,
    val emoji: String,
    val color: Color = PrimaryOrange,
)

val defaultFoodCategories = listOf(
    CategoryItem("all", "All", "✨", Color(0xFF6366F1)),
    CategoryItem("Burger", "Burger", "🍔", Color(0xFFFF9800)),
    CategoryItem("Pizza", "Pizza", "🍕", Color(0xFFE91E63)),
    CategoryItem("Noodle", "Noodle", "🍜", Color(0xFF4CAF50)),
    CategoryItem("Japanese", "Sushi", "🍣", Color(0xFFF44336)),
    CategoryItem("Mexican", "Mexican", "🌮", Color(0xFF8BC34A)),
    CategoryItem("Salad", "Healthy", "🥗", Color(0xFF009688)),
    CategoryItem("Chicken", "Chicken", "🍗", Color(0xFFFF5722)),
    CategoryItem("Steak", "Steak", "🥩", Color(0xFF795548)),
    CategoryItem("Seafood", "Seafood", "🦞", Color(0xFF03A9F4)),
    CategoryItem("Drink", "Drinks", "🧋", Color(0xFF00BCD4)),
    CategoryItem("Dessert", "Desserts", "🍰", Color(0xFF9C27B0)),
)

@Composable
fun CategoryChipList(
    categories: List<CategoryItem> = defaultFoodCategories,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(categories) { category ->
            val isSelected = category.id.equals(selectedCategory, ignoreCase = true) ||
                    (category.id == "all" && selectedCategory.isBlank())

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onCategorySelected(if (category.id == "all") "" else category.id) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) category.color else MaterialTheme.colorScheme.surface,
                tonalElevation = if (isSelected) 4.dp else 1.dp,
                shadowElevation = if (isSelected) 4.dp else 1.dp,
                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                ),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = category.emoji,
                        fontSize = 16.sp,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
fun PromoBannerCard(
    title: String = "Flash Sale 30% OFF",
    subtitle: String = "Use code QUICK30 on first order",
    emoji: String = "🔥",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryOrangeGradientStart,
                            PrimaryOrangeGradientEnd,
                        ),
                    ),
                )
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "SPECIAL PROMO",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                    )
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = emoji,
                        fontSize = 32.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = AccentYellowLight,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = AccentAmber,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "%.1f".format(rating),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF8A5B00),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun DeliveryTimeBadge(
    deliveryTime: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "⚡",
                fontSize = 11.sp,
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = deliveryTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun FreeDeliveryBadge(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = SuccessGreenLight,
    ) {
        Text(
            text = "FREE DELIVERY",
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            color = SuccessGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
        )
    }
}

@Composable
fun QuantityControl(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = onDecrease),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(16.dp),
                )
            }

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(36.dp),
            )

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(PrimaryOrange)
                    .clickable(onClick = onIncrease),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = PrimaryOrange,
                spotColor = PrimaryOrange,
            ),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryOrange,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryOrange,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 6.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
fun EmptyStateView(
    emoji: String = "🍽️",
    title: String,
    description: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PrimaryOrange.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emoji,
                fontSize = 48.sp,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier.width(200.dp),
            )
        }
    }
}
