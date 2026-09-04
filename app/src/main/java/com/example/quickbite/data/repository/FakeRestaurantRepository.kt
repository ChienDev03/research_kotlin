package com.example.quickbite.data.repository

import com.example.quickbite.data.model.Food
import com.example.quickbite.data.model.Restaurant

class FakeRestaurantRepository : RestaurantRepository {

    private val restaurants = listOf(
        Restaurant(
            id = 1,
            name = "Burger House 🍔",
            description = "Artisanal smash burgers, crispy sides & shakes",
            rating = 4.9,
            deliveryTime = "15-20 min",
            category = "Burger",
        ),
        Restaurant(
            id = 2,
            name = "Pizza Corner 🍕",
            description = "Authentic wood-fired Neapolitan pizza",
            rating = 4.8,
            deliveryTime = "25-30 min",
            category = "Pizza",
        ),
        Restaurant(
            id = 3,
            name = "Noodle Station 🍜",
            description = "Handmade ramen & traditional Asian noodle bowls",
            rating = 4.7,
            deliveryTime = "20-25 min",
            category = "Noodle",
        ),
        Restaurant(
            id = 4,
            name = "Matcha & Boba Oasis 🧋",
            description = "Premium organic milk teas, boba & matcha latte",
            rating = 4.9,
            deliveryTime = "10-15 min",
            category = "Drink",
        ),
        Restaurant(
            id = 5,
            name = "Sweet Bakery & Dessert 🍰",
            description = "Freshly baked croissants, waffles & lava cakes",
            rating = 4.8,
            deliveryTime = "15-20 min",
            category = "Dessert",
        ),
        Restaurant(
            id = 6,
            name = "Sakura Sushi 🍣",
            description = "Fresh sashimi, creative rolls & premium Japanese sake",
            rating = 4.8,
            deliveryTime = "30-35 min",
            category = "Japanese",
        ),
        Restaurant(
            id = 7,
            name = "Taco Fiesta 🌮",
            description = "Authentic street tacos, burritos & fresh homemade guacamole",
            rating = 4.6,
            deliveryTime = "20-25 min",
            category = "Mexican",
        ),
        Restaurant(
            id = 8,
            name = "Green Bowl 🥗",
            description = "Organic salads, cold-pressed juices & healthy grain bowls",
            rating = 4.7,
            deliveryTime = "15-20 min",
            category = "Salad",
        ),
        Restaurant(
            id = 9,
            name = "Fried Chicken Bliss 🍗",
            description = "Extra crispy southern fried chicken and comfort sides",
            rating = 4.8,
            deliveryTime = "20-25 min",
            category = "Chicken",
        ),
        Restaurant(
            id = 10,
            name = "Prime Cut Steakhouse 🥩",
            description = "Premium dry-aged steaks, grilled to perfection",
            rating = 4.9,
            deliveryTime = "35-45 min",
            category = "Steak",
        ),
        Restaurant(
            id = 11,
            name = "Ocean's Catch 🦞",
            description = "Fresh seafood, lobster rolls & grilled catch of the day",
            rating = 4.7,
            deliveryTime = "25-35 min",
            category = "Seafood",
        ),
        Restaurant(
            id = 12,
            name = "Seoul BBQ 🍖",
            description = "Traditional Korean BBQ, premium meats & classic sides",
            rating = 4.8,
            deliveryTime = "30-40 min",
            category = "Japanese",
        ),
        Restaurant(
            id = 13,
            name = "The Coffee House ☕",
            description = "Artisan coffee, cold brews & freshly baked pastries",
            rating = 4.7,
            deliveryTime = "10-15 min",
            category = "Drink",
        ),
        Restaurant(
            id = 14,
            name = "Wing Stop 🐤",
            description = "World famous wings, hand-tossed in signature sauces",
            rating = 4.5,
            deliveryTime = "15-20 min",
            category = "Chicken",
        ),
    )

    private val foods = listOf(
        // Burger House
        Food(
            id = 1,
            restaurantId = 1,
            name = "Classic Double Cheeseburger 🍔",
            description = "Double Angus beef patty, cheddar cheese, caramelized onions & secret sauce",
            price = 7.99,
            category = "Burger",
        ),
        Food(
            id = 2,
            restaurantId = 1,
            name = "Crispy Bacon Burger 🥓",
            description = "Smoked bacon, melted Monterey jack, crisp lettuce & BBQ glaze",
            price = 8.99,
            category = "Burger",
        ),
        Food(
            id = 10,
            restaurantId = 1,
            name = "Grilled Chicken Burger 🍗",
            description = "Marinated chicken breast, avocado, sprout & garlic aioli",
            price = 7.49,
            category = "Burger",
        ),
        Food(
            id = 3,
            restaurantId = 1,
            name = "Truffle Parmesan Fries 🍟",
            description = "Golden potato fries tossed with truffle oil & grated parmesan",
            price = 4.49,
            category = "Side",
        ),
        Food(
            id = 11,
            restaurantId = 1,
            name = "Beer Battered Onion Rings 🧅",
            description = "Thick cut onion rings served with spicy ranch dipping sauce",
            price = 3.99,
            category = "Side",
        ),

        // Pizza Corner
        Food(
            id = 4,
            restaurantId = 2,
            name = "Pepperoni Supreme Pizza 🍕",
            description = "Loaded beef pepperoni, mozzarella cheese & San Marzano tomato sauce",
            price = 11.99,
            category = "Pizza",
        ),
        Food(
            id = 5,
            restaurantId = 2,
            name = "Four Cheese Margherita 🧀",
            description = "Fresh mozzarella, parmesan, gorgonzola & fragrant sweet basil",
            price = 10.49,
            category = "Pizza",
        ),
        Food(
            id = 12,
            restaurantId = 2,
            name = "Hawaiian Paradise Pizza 🍍",
            description = "Sweet pineapple, honey glazed ham & extra mozzarella",
            price = 11.49,
            category = "Pizza",
        ),
        Food(
            id = 13,
            restaurantId = 2,
            name = "Cheesy Garlic Bread 🧄",
            description = "Freshly baked baguette with garlic butter and melted cheese",
            price = 4.99,
            category = "Side",
        ),

        // Noodle Station
        Food(
            id = 6,
            restaurantId = 3,
            name = "Tokyo Tonkotsu Ramen 🍜",
            description = "Rich 12-hour pork broth, tender chashu, ajitsuke tamago & scallions",
            price = 9.99,
            category = "Noodle",
        ),
        Food(
            id = 7,
            restaurantId = 3,
            name = "Spicy Dan Dan Noodles 🌶️",
            description = "Springy wheat noodles in savory chili sesame broth with minced pork",
            price = 8.99,
            category = "Noodle",
        ),
        Food(
            id = 14,
            restaurantId = 3,
            name = "Classic Shoyu Ramen 🥢",
            description = "Clear soy sauce broth with bamboo shoots, seaweed & pork belly",
            price = 9.49,
            category = "Noodle",
        ),
        Food(
            id = 15,
            restaurantId = 3,
            name = "Crispy Veggie Gyoza 🥟",
            description = "Pan-seared vegetable dumplings served with soy vinegar dip",
            price = 5.99,
            category = "Appetizer",
        ),

        // Matcha & Boba Oasis
        Food(
            id = 8,
            restaurantId = 4,
            name = "Brown Sugar Boba Milk 🧋",
            description = "Fresh milk with warm tiger brown sugar pearls & creamy cheese foam",
            price = 4.99,
            category = "Drink",
        ),
        Food(
            id = 16,
            restaurantId = 4,
            name = "Ceremonial Matcha Latte 🍵",
            description = "Whisked premium Uji matcha with oat milk and light honey",
            price = 4.49,
            category = "Drink",
        ),
        Food(
            id = 17,
            restaurantId = 4,
            name = "Taro Royal Milk Tea 🥤",
            description = "Creamy taro root blend with classic black tea and crystal boba",
            price = 4.99,
            category = "Drink",
        ),

        // Sweet Bakery & Dessert
        Food(
            id = 9,
            restaurantId = 5,
            name = "Molten Chocolate Lava Cake 🍫",
            description = "Warm chocolate fondant with molten center served with vanilla bean",
            price = 5.99,
            category = "Dessert",
        ),
        Food(
            id = 18,
            restaurantId = 5,
            name = "Golden Butter Croissant 🥐",
            description = "Flaky, multi-layered French pastry baked fresh daily",
            price = 3.49,
            category = "Bakery",
        ),
        Food(
            id = 19,
            restaurantId = 5,
            name = "Wild Strawberry Waffle 🍓",
            description = "Belgian waffle topped with fresh strawberries & maple syrup",
            price = 6.99,
            category = "Dessert",
        ),

        // Sakura Sushi
        Food(
            id = 20,
            restaurantId = 6,
            name = "Premium Salmon Nigiri 🍣",
            description = "Freshly sliced Atlantic salmon over seasoned sushi rice (5 pcs)",
            price = 6.99,
            category = "Sushi",
        ),
        Food(
            id = 21,
            restaurantId = 6,
            name = "Signature California Roll 🍱",
            description = "Crab stick, avocado, cucumber & flying fish roe (8 pcs)",
            price = 8.99,
            category = "Sushi",
        ),
        Food(
            id = 22,
            restaurantId = 6,
            name = "Classic Miso Soup 🥣",
            description = "Traditional dashi broth with tofu, seaweed & green onions",
            price = 2.99,
            category = "Soup",
        ),

        // Taco Fiesta
        Food(
            id = 23,
            restaurantId = 7,
            name = "Beef Barbacoa Tacos 🌮",
            description = "Slow-cooked beef, white onions, cilantro & lime (3 pcs)",
            price = 9.99,
            category = "Mexican",
        ),
        Food(
            id = 24,
            restaurantId = 7,
            name = "Pulled Chicken Burrito 🌯",
            description = "Cilantro-lime rice, black beans, salsa & grilled chicken",
            price = 8.99,
            category = "Mexican",
        ),
        Food(
            id = 25,
            restaurantId = 7,
            name = "Nachos Grande 🧀",
            description = "Tortilla chips with melted cheese, jalapeños & sour cream",
            price = 7.49,
            category = "Appetizer",
        ),

        // Green Bowl
        Food(
            id = 26,
            restaurantId = 8,
            name = "Quinoa Power Salad 🥗",
            description = "Tri-color quinoa, cherry tomatoes, kale & lemon tahini dressing",
            price = 8.49,
            category = "Salad",
        ),
        Food(
            id = 27,
            restaurantId = 8,
            name = "Avocado Grain Bowl 🥑",
            description = "Brown rice, roasted sweet potato, chickpea & fresh avocado",
            price = 9.99,
            category = "Salad",
        ),
        Food(
            id = 28,
            restaurantId = 8,
            name = "Tropical Kale Smoothie 🥤",
            description = "Blend of kale, pineapple, mango & coconut water",
            price = 5.49,
            category = "Drink",
        ),

        // Fried Chicken Bliss
        Food(
            id = 29,
            restaurantId = 9,
            name = "6pc Crispy Wings 🍗",
            description = "Jumbo wings tossed in your choice of spicy buffalo or honey garlic",
            price = 8.99,
            category = "Chicken",
        ),
        Food(
            id = 30,
            restaurantId = 9,
            name = "Southern Fried Bucket 🧺",
            description = "8 pieces of bone-in chicken with 2 large sides and biscuits",
            price = 19.99,
            category = "Chicken",
        ),
        Food(
            id = 31,
            restaurantId = 9,
            name = "Creamy Mac & Cheese 🧀",
            description = "Homestyle baked macaroni with four types of melted cheese",
            price = 4.49,
            category = "Side",
        ),

        // Prime Cut Steakhouse
        Food(
            id = 32,
            restaurantId = 10,
            name = "Ribeye Steak 🥩",
            description = "12oz dry-aged ribeye served with garlic herb butter",
            price = 24.99,
            category = "Steak",
        ),
        Food(
            id = 33,
            restaurantId = 10,
            name = "Filet Mignon 🔪",
            description = "8oz tenderloin wrapped in bacon with red wine reduction",
            price = 29.99,
            category = "Steak",
        ),
        Food(
            id = 34,
            restaurantId = 10,
            name = "Loaded Baked Potato 🥔",
            description = "Baked potato with bacon bits, chives, sour cream & cheddar",
            price = 5.99,
            category = "Side",
        ),

        // Ocean's Catch
        Food(
            id = 35,
            restaurantId = 11,
            name = "Buttery Lobster Roll 🦞",
            description = "Fresh lobster meat in a toasted brioche bun with lemon butter",
            price = 18.99,
            category = "Seafood",
        ),
        Food(
            id = 36,
            restaurantId = 11,
            name = "Grilled Atlantic Salmon 🐟",
            description = "Wild-caught salmon with asparagus and citrus glaze",
            price = 16.99,
            category = "Seafood",
        ),
        Food(
            id = 37,
            restaurantId = 11,
            name = "Garlic Butter Shrimp 🍤",
            description = "Sautéed jumbo shrimp with white wine and parsley",
            price = 14.99,
            category = "Seafood",
        ),

        // Seoul BBQ
        Food(
            id = 38,
            restaurantId = 12,
            name = "Bulgogi Platter 🍖",
            description = "Thinly sliced marinated beef grilled with onions and mushrooms",
            price = 15.99,
            category = "Japanese",
        ),
        Food(
            id = 39,
            restaurantId = 12,
            name = "Bibimbap Bowl 🍱",
            description = "Mixed rice with vegetables, beef, fried egg and spicy sauce",
            price = 12.99,
            category = "Japanese",
        ),

        // The Coffee House
        Food(
            id = 40,
            restaurantId = 13,
            name = "Caramel Macchiato ☕",
            description = "Freshly pulled espresso with steamed milk and caramel drizzle",
            price = 4.99,
            category = "Drink",
        ),
        Food(
            id = 41,
            restaurantId = 13,
            name = "Cold Brew Coffee 🧊",
            description = "Slow-steeped for 12 hours for a smooth, bold flavor",
            price = 4.49,
            category = "Drink",
        ),

        // Wing Stop
        Food(
            id = 42,
            restaurantId = 14,
            name = "Lemon Pepper Wings 🍗",
            description = "Crispy wings tossed in zesty lemon pepper seasoning",
            price = 9.99,
            category = "Chicken",
        ),
        Food(
            id = 43,
            restaurantId = 14,
            name = "Hickory Smoked BBQ Wings 🍗",
            description = "Classic BBQ flavor with a hint of smoky hickory",
            price = 9.99,
            category = "Chicken",
        ),
    )

    override fun getRestaurants(): List<Restaurant> {
        return restaurants
    }

    override fun getRestaurantById(id: Int): Restaurant? {
        return restaurants.find { it.id == id }
    }

    override fun getFoodsByRestaurantId(restaurantId: Int): List<Food> {
        return foods.filter { it.restaurantId == restaurantId }
    }

    override fun getFoodById(id: Int): Food? {
        return foods.find { it.id == id }
    }
}

