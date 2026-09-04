package com.example.quickbite.presentation.restaurant

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.Food
import com.example.quickbite.ui.components.DeliveryTimeBadge
import com.example.quickbite.ui.components.FreeDeliveryBadge
import com.example.quickbite.ui.components.RatingBadge
import com.example.quickbite.ui.components.SectionHeader
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(
    restaurantId: Int,
    viewModel: RestaurantViewModel,
    onBack: () -> Unit,
    onFoodClick: (Int) -> Unit,
) {
    LaunchedEffect(restaurantId) {
        viewModel.loadRestaurant(restaurantId)
    }

    val restaurant by viewModel.restaurant.collectAsState()
    val foods by viewModel.foods.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = restaurant?.name ?: "Restaurant",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                restaurant?.let { res ->
                    val categoryEmoji = when (res.category) {
                        "Burger" -> "🍔"
                        "Pizza" -> "🍕"
                        "Noodle" -> "🍜"
                        "Drink" -> "🧋"
                        "Dessert" -> "🍰"
                        else -> "🍽️"
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    ) {
                        Column {
                            // Hero Banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .background(
                                        Brush.horizontalGradient(
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
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = categoryEmoji,
                                        fontSize = 44.sp,
                                    )
                                }
                            }

                            // Details
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = res.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = res.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RatingBadge(rating = res.rating)
                                    DeliveryTimeBadge(deliveryTime = res.deliveryTime)
                                    FreeDeliveryBadge()
                                }
                            }
                        }
                    }
                }
            }

            item {
                SectionHeader(
                    title = "Menu & Dishes",
                    actionText = "${foods.size} items",
                )
            }

            items(foods, key = { it.id }) { food ->
                FoodCard(
                    food = food,
                    onClick = { onFoodClick(food.id) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun FoodCard(
    food: Food,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val foodEmoji = when (food.category) {
        "Burger" -> "🍔"
        "Pizza" -> "🍕"
        "Noodle" -> "🍜"
        "Side" -> "🍟"
        "Drink" -> "🧋"
        "Dessert" -> "🍰"
        else -> "🍲"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
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
            // Food Icon Avatar
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = foodEmoji,
                    fontSize = 36.sp,
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Food Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = food.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$${"%.2f".format(food.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryOrange,
                        fontWeight = FontWeight.Bold,
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PrimaryOrange),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}