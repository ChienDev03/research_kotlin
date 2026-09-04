package com.example.quickbite.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickbite.ui.theme.PrimaryOrange
import com.example.quickbite.ui.theme.PrimaryOrangeGradientEnd
import com.example.quickbite.ui.theme.PrimaryOrangeGradientStart
import com.example.quickbite.ui.theme.SuccessGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StageDemoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val stages = listOf("Stage 1", "Stage 2", "Stage 3", "Stage 4")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demo Stage 1 -> 4") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                stages.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> Stage1Screen()
                    1 -> Stage2Screen()
                    2 -> Stage3Screen()
                    3 -> Stage4Screen()
                }
            }
        }
    }
}

@Composable
private fun Stage1Screen() {
    val categories = listOf("Burgers 🍔", "Pizza 🍕", "Noodles 🍜", "Drinks 🥤", "Dessert 🍰")
    val dishes = listOf(
        "Hue Beef Noodle Soup" to "Traditional spicy beef noodle soup with herbs & lemongrass",
        "Crispy Seafood Pizza" to "Wood-fired crust topped with fresh shrimp, squid & mozzarella",
        "Signature Cheeseburger" to "Double Angus beef patty with melted cheddar & special BBQ sauce",
        "Hanoi Grilled Pork Vermicelli" to "Charcoal-grilled pork patties served with fresh vermicelli & herbs",
        "Japanese Salmon Sushi Set" to "Fresh Atlantic salmon nigiri and sashimi served with wasabi",
        "Bubble Milk Tea" to "Creamy Earl Grey milk tea with chewy brown sugar tapioca boba",
        "Matcha Green Tea Cheesecake" to "Rich Japanese Uji matcha cheesecake with crispy oat base",
        "Spicy Tonkotsu Ramen" to "Rich pork bone broth noodle soup with chashu pork & soft-boiled egg",
        "Grilled Chicken Caesar Salad" to "Tender chicken breast with crisp romaine lettuce & Parmesan cheese",
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = "1. LazyRow & Badged Box Layout",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(categories) { category ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(PrimaryOrange.copy(alpha = 0.12f))
                            .border(
                                1.dp,
                                PrimaryOrange.copy(alpha = 0.4f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = category,
                            color = PrimaryOrange,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2. Nested Column & Row with Modifiers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        items(dishes) { (name, desc) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
//                onClick = { },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryOrange.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrimaryOrange,
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Stage2Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = "Design System Components",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        // Custom Gradient Banner Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PrimaryOrangeGradientStart, PrimaryOrangeGradientEnd),
                    ),
                )
                .padding(20.dp),
        ) {
            Column {
                Text(
                    text = "Special Design Token Banner",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
                Text(
                    text = "Custom gradient with rounded corners & typography tokens",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                )
            }
        }
        // Button Component Hierarchy
        Text(
            text = "Button System",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Filled Primary Button")
        }
        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Outlined Secondary Button")
        }
        // Loading State Component
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.dp,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Processing Order...")
            }
        }
    }
}

@Composable
private fun Stage3Screen() {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isExpressDelivery by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var submittedMessage by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current
    // Validation rules
    val isNameValid = fullName.trim().length >= 2
    val isPhoneValid = phone.matches(Regex("^[0-9]{10}$"))
    val isFormValid = isNameValid && isPhoneValid && agreeToTerms

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Delivery Checkout Form",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name *") },
            modifier = Modifier.fillMaxWidth(),
            isError = fullName.isNotEmpty() && !isNameValid,
            supportingText = {
                if (fullName.isNotEmpty() && !isNameValid) {
                    Text(
                        "Name must be at least 2 characters",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            singleLine = true,
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10) phone = it },
            label = { Text("Phone Number (10 digits) *") },
            modifier = Modifier.fillMaxWidth(),
            isError = phone.isNotEmpty() && !isPhoneValid,
            supportingText = {
                if (phone.isNotEmpty() && !isPhoneValid) {
                    Text(
                        "Phone must be exactly 10 numeric digits",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Express Delivery (15 mins)")
            Switch(checked = isExpressDelivery, onCheckedChange = { isExpressDelivery = it })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { agreeToTerms = !agreeToTerms },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = agreeToTerms, onCheckedChange = { agreeToTerms = it })
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "I agree to QuickBite terms and conditions",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Button(
            onClick = {
                submittedMessage = "Order submitted for $fullName ($phone)!"
                focusManager.clearFocus()
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
        ) {
            Text("Confirm Order")
        }
        submittedMessage?.let { msg ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SuccessGreen.copy(alpha = 0.15f))
                    .padding(12.dp),
            ) {
                Text(msg, color = SuccessGreen, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun Stage4Screen() {
    var isLiked by remember { mutableStateOf(true) }
    var isExpanded by remember { mutableStateOf(false) }
    var tapCounter by remember { mutableIntStateOf(0) }
    // Heart Icon Animation States
    val iconColor by animateColorAsState(
        targetValue = if (isLiked) Color.Red else Color.Gray,
        label = "HeartColorAnimation",
    )
    val iconSize by animateDpAsState(
        targetValue = if (isLiked) 36.dp else 24.dp,
        animationSpec = spring(dampingRatio = 0.4f),
        label = "HeartSizeAnimation",

                )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Micro-interactions & Gestures",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        // 1. Spring Animation on Click
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text("Spring Heart Animation", fontWeight = FontWeight.Bold)
                    Text(
                        "Tap heart to trigger animate*AsState",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = iconColor,
                        modifier = Modifier
                            .size(iconSize)
                    )
                }
            }
        }
        // 2. AnimatedVisibility Expand/Collapse Card
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { isExpanded = !isExpanded },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Expandable Recipe Details", fontWeight = FontWeight.Bold)
                    Text(
                        if (isExpanded) "▲ Hide" else "▼ Show",
                        color = PrimaryOrange,
                        fontSize = 12.sp
                    )
                }
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            "• 200g Fresh Beef Steak\n• 1 tbsp Olive Oil\n• Secret QuickBite BBQ Sauce\n• Roasted Veggies",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        // 3. PointerInput Gesture Detector (Double Tap & Long Press)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(PrimaryOrange.copy(alpha = 0.1f))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { tapCounter += 2 },
                        onTap = { tapCounter += 1 },
                        onLongPress = { tapCounter = 0 },
                    )
                }
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Gesture Area (Single / Double Tap / Long Press)",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Score: $tapCounter (Long press to reset)",
                    color = PrimaryOrange,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                )
            }
        }
    }
}