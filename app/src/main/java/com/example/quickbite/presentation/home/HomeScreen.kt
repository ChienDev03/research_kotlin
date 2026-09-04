package com.example.quickbite.presentation.home

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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.data.model.Restaurant
import com.example.quickbite.ui.components.CategoryChipList
import com.example.quickbite.ui.components.DeliveryTimeBadge
import com.example.quickbite.ui.components.EmptyStateView
import com.example.quickbite.ui.components.FreeDeliveryBadge
import com.example.quickbite.ui.components.PromoBannerCard
import com.example.quickbite.ui.components.QuickBiteHeader
import com.example.quickbite.ui.components.QuickBiteSearchBar
import com.example.quickbite.ui.components.RatingBadge
import com.example.quickbite.ui.components.SectionHeader
import com.example.quickbite.ui.theme.QuickBiteTheme
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    cartItemCount: Int = 0,
    onRestaurantClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onLocationClick: () -> Unit,
) {
    val restaurants by viewModel.restaurants.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    HomeScreenContent(
        restaurants = restaurants,
        searchQuery = searchQuery,
        selectedCategory = selectedCategory,
        cartItemCount = cartItemCount,
        onRestaurantClick = onRestaurantClick,
        onCartClick = onCartClick,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onCategorySelected = viewModel::onCategorySelected,
        onLocationClick = onLocationClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    restaurants: List<Restaurant>,
    searchQuery: String,
    selectedCategory: String,
    cartItemCount: Int,
    onRestaurantClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onLocationClick: () -> Unit,
) {
    Scaffold(modifier = Modifier.safeDrawingPadding(),
        topBar = {
            QuickBiteHeader(
                cartItemCount = cartItemCount,
                onCartClick = onCartClick,
                onLocationClick = onLocationClick,
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
                QuickBiteSearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChanged,
                    placeholder = "Search food, restaurants, dishes...",
                    onFilterClick = { /* Optional Filter Dialog */ },
                )
            }

            if (searchQuery.isBlank()) {
                item {
                    PromoBannerCard(
                        title = "Flash Sale 30% OFF",
                        subtitle = "Limited time offer on all orders over $15",
                        emoji = "🍔",
                        onClick = { /* Promo Action */ },
                    )
                }
            }

            item {
                Column {
                    SectionHeader(
                        title = "Categories",
                        actionText = if (selectedCategory.isNotEmpty()) "Clear" else null,
                        onActionClick = { onCategorySelected("") },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CategoryChipList(
                        selectedCategory = selectedCategory,
                        onCategorySelected = onCategorySelected,
                    )
                }
            }

            if (restaurants.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = if (selectedCategory.isNotBlank()) "$selectedCategory Places" else "Popular Restaurants",
                        actionText = "${restaurants.size} found",
                    )
                }
            }

            if (restaurants.isEmpty()) {
                item(key = "empty_state") {
                    EmptyStateView(
                        emoji = "🔍",
                        title = "No Restaurants Found",
                        description = "We couldn't find any results matching '$searchQuery'. Try searching something else!",
                        buttonText = "Reset Filters",
                        onButtonClick = {
                            onSearchQueryChanged("")
                            onCategorySelected("")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp)
                    )
                }
            } else {
                items(restaurants, key = { it.id }) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onClick = { onRestaurantClick(restaurant.id) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isFavorite by remember { mutableStateOf(false) }

    val categoryEmoji = when (restaurant.category) {
        "Burger" -> "🍔"
        "Pizza" -> "🍕"
        "Noodle" -> "🍜"
        "Drink" -> "🧋"
        "Dessert" -> "🍰"
        "Japanese" -> "🍣"
        "Mexican" -> "🌮"
        "Salad" -> "🥗"
        "Chicken" -> "🍗"
        "Steak" -> "🥩"
        "Seafood" -> "🦞"
        else -> "🍽️"
    }

    val bannerGradients = when (restaurant.category) {
        "Burger" -> listOf(Color(0xFFFF7A00), Color(0xFFFF4500))
        "Pizza" -> listOf(Color(0xFFFF5252), Color(0xFFD50000))
        "Noodle" -> listOf(Color(0xFFFF9100), Color(0xFFFF6D00))
        "Drink" -> listOf(Color(0xFF00B0FF), Color(0xFF0091EA))
        "Dessert" -> listOf(Color(0xFFFF4081), Color(0xFFC51162))
        "Japanese" -> listOf(Color(0xFFFFCDD2), Color(0xFFE57373))
        "Mexican" -> listOf(Color(0xFFAED581), Color(0xFF689F38))
        "Salad" -> listOf(Color(0xFF81C784), Color(0xFF388E3C))
        "Chicken" -> listOf(Color(0xFFFFB74D), Color(0xFFF57C00))
        "Steak" -> listOf(Color(0xFF90A4AE), Color(0xFF455A64))
        "Seafood" -> listOf(Color(0xFF4FC3F7), Color(0xFF0288D1))
        else -> listOf(PrimaryOrangeGradientStart, PrimaryOrangeGradientEnd)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column {
            // Visual Hero Header Banner for Restaurant
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Brush.horizontalGradient(bannerGradients)),
            ) {
                // Large Food Emoji Visual
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = categoryEmoji,
                        fontSize = 42.sp,
                    )
                }

                // Category Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = restaurant.category.uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp,
                    )
                }

                // Favorite Button
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.85f)),
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.DarkGray,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            // Info Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = restaurant.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    RatingBadge(rating = restaurant.rating)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = restaurant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DeliveryTimeBadge(deliveryTime = restaurant.deliveryTime)
                    FreeDeliveryBadge()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val sampleRestaurants = listOf(
        Restaurant(
            id = 1,
            name = "Burger King",
            description = "Burgers, Fast Food",
            rating = 4.5,
            deliveryTime = "20-30 min",
            category = "Burgers"
        ),
        Restaurant(
            id = 2,
            name = "Pizza Hut",
            description = "Pizza, Italian",
            rating = 4.2,
            deliveryTime = "30-40 min",
            category = "Pizza"
        ),
        Restaurant(
            id = 3,
            name = "Sushi World",
            description = "Sushi, Japanese",
            rating = 4.8,
            deliveryTime = "25-35 min",
            category = "Sushi"
        )
    )

    QuickBiteTheme {
        HomeScreenContent(
            restaurants = sampleRestaurants,
            searchQuery = "",
            selectedCategory = "",
            cartItemCount = 3,
            onRestaurantClick = {},
            onCartClick = {},
            onSearchQueryChanged = {},
            onCategorySelected = {},
            onLocationClick = {},
        )
    }
}
