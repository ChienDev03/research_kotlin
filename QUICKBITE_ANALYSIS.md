# Phân Tích Dự Án QuickBite — State, Lifecycle, Coroutines & Annotations

> **Dự án**: QuickBite (Android Native - Food Delivery Application)  
> **Công nghệ cốt lõi**: Jetpack Compose, Kotlin Coroutines & Flow, Jetpack Lifecycle & ViewModel, Navigation Compose, Material 3.

---

## Mục Lục

1. [Tổng Quan Dự Án](#1-tổng-quan-dự-án)
2. [Quản Lý Trạng Thái (State Management)](#2-quản-lý-trạng-thái-state-management)
3. [Vòng Đời & Side Effects (Lifecycle)](#3-vòng-đời--side-effects-lifecycle)
4. [Coroutines & Reactive Flow](#4-coroutines--reactive-flow)
5. [Giải Thích Chi Tiết Các Annotations](#5-giải-thích-chi-tiết-các-annotations)
6. [Sơ Đồ Kiến Trúc & Luồng Dữ Liệu](#6-sơ-đồ-kiến-trúc--luồng-dữ-liệu)
7. [Đánh Giá & Best Practices](#7-đánh-giá--best-practices)

---

## 1. Tổng Quan Dự Án

**QuickBite** là ứng dụng đặt đồ ăn được xây dựng theo chuẩn **Modern Android Development (MAD)**. 

### Cấu trúc mô hình dự án (MVVM + Clean Architecture rút gọn):
* `data/`:
  * **Model**: [`Restaurant`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/model/Restaurant.kt), [`Food`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/model/Food.kt), [`CartItem`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/model/CartItem.kt), [`Order`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/model/Order.kt).
  * **Repository**: [`RestaurantRepository`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/repository/RestaurantRepository.kt), [`FakeRestaurantRepository`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/data/repository/FakeRestaurantRepository.kt).
* `domain/`:
  * OOP Polymorphism cho dòng sản phẩm ([`FoodProduct`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/domain/food/FoodProduct.kt), [`Burger`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/domain/food/Burger.kt), [`Pizza`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/domain/food/Pizza.kt)).
  * Strategy Pattern cho giảm giá ([`DiscountCalculator`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/domain/discount/DiscountCalculator.kt)) và thanh toán ([`PaymentMethod`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/domain/payment/PaymentMethod.kt)).
* `presentation/`:
  * Màn hình UI ([`HomeScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/home/HomeScreen.kt), [`FoodDetailScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/food/FoodDetailScreen.kt), [`CartScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/cart/CartScreen.kt), [`CheckoutScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/checkout/CheckoutScreen.kt), [`OrderTrackingScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/order/OrderTrackingScreen.kt)) và các ViewModels tương ứng.
* `util/`:
  * Lớp bọc trạng thái giao diện [`UiState<T>`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/util/UiState.kt).

---

## 2. Quản Lý Trạng Thái (State Management)

Dự án áp dụng việc quản lý state theo chuẩn **Unidirectional Data Flow (UDF)** với 3 cấp độ:

### 2.1 Generic Async State Wrapper (`UiState<T>`)
Trong [`UiState.kt`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/util/UiState.kt):
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```
* **Mục đích**: Đại diện cho các trạng thái bất biến của dữ liệu bất đồng bộ.
* **Sử dụng**: Trong [`FoodDetailViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/food/FoodDetailViewModel.kt), `_foodState = MutableStateFlow<UiState<Food>>(UiState.Loading)`. 
* **UI Handling**: Đảm bảo type-safe pattern matching bằng `when (val state = foodState)` trong [`FoodDetailScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/food/FoodDetailScreen.kt#L52).

### 2.2 ViewModel State Encapsulation (StateFlow & MutableStateFlow)
* **Read-only State Exposure**:
  Tất cả ViewModel trong dự án đều giấu biến Mutable và chỉ công khai `StateFlow`:
  ```kotlin
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
  ```
* **Atomic Mutation (`StateFlow.update`)**:
  Trong [`CartViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/cart/CartViewModel.kt):
  ```kotlin
  fun addItem(item: CartItem) {
      _items.update { currentItems ->
          val existing = currentItems.find { it.food.id == item.food.id }
          if (existing == null) currentItems + item
          else currentItems.map { if (it.food.id == item.food.id) it.copy(quantity = it.quantity + item.quantity) else it }
      }
  }
  ```
  `_items.update` đảm bảo thay đổi trạng thái an toàn luồng (thread-safe atomic mutation).

### 2.3 Reactive Combined State (`combine`)
Trong [`HomeViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/home/HomeViewModel.kt):
```kotlin
val restaurants: StateFlow<List<Restaurant>> = combine(
    _allRestaurants,
    _searchQuery,
    _selectedCategory,
) { all, query, category ->
    all.filter { restaurant ->
        val matchesQuery = query.isBlank() || restaurant.name.contains(query, ignoreCase = true)
        val matchesCategory = category.isBlank() || restaurant.category.equals(category, ignoreCase = true)
        matchesQuery && matchesCategory
    }
}.stateIn(...)
```
Mỗi khi người dùng gõ từ khóa tìm kiếm hoặc chọn danh mục, `combine` tự động lọc lại danh sách nhà hàng và phát ra value mới.

### 2.4 Local Composable State (`remember` & `mutableStateOf`)
Được dùng cho các trạng thái giao diện thuần túy cục bộ, ví dụ trạng thái nút Favorite trong card nhà hàng:
```kotlin
var isFavorite by remember { mutableStateOf(false) }
```

---

## 3. Vòng Đời & Side Effects (Lifecycle)

### 3.1 Activity Lifecycle
* [`MainActivity`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/MainActivity.kt) kế thừa `ComponentActivity`.
* Khởi chạy `enableEdgeToEdge()` và `setContent { QuickBiteTheme { ... } }`.

### 3.2 Phạm vi Lưu Trữ ViewModel (Scope Strategy)
Dự án chia làm 2 chiến lược lưu trữ ViewModel:
1. **Shared ViewModel (Cấp Root NavHost)**:
   * [`cartViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/MainActivity.kt#L41) và [`checkoutViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/MainActivity.kt#L45) được tạo tại root của `NavHost` trong `MainActivity`.
   * Tồn tại xuyên suốt quá trình điều hướng giữa các màn hình, giúp lưu giữ dữ liệu giỏ hàng (`items`) và thông tin đơn hàng vừa đặt (`lastOrder`).
2. **Screen-Scoped ViewModel (Cấp Màn Hình)**:
   * [`HomeViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/home/HomeViewModel.kt), [`FoodDetailViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/food/FoodDetailViewModel.kt), [`RestaurantViewModel`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/restaurant/RestaurantViewModel.kt) được tạo bên trong route `composable(...)`.
   * Tự động tiêu hủy (clear) khi Pop BackStack khỏi route tương ứng.

### 3.3 Compose Side Effects (`LaunchedEffect`)
Trong [`FoodDetailScreen`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/food/FoodDetailScreen.kt#L48):
```kotlin
LaunchedEffect(foodId) {
    viewModel.loadFood(foodId)
}
```
* **Cơ chế**: Chạy một coroutine khi Composable đi vào Composition lần đầu tiên.
* **Tự động hủy**: Khi `foodId` thay đổi hoặc màn hình bị Pop khỏi BackStack, coroutine cũ sẽ bị cancel tự động.

---

## 4. Coroutines & Reactive Flow

### 4.1 `viewModelScope`
* Mọi tác vụ bất đồng bộ trong ViewModel sử dụng `viewModelScope`.
* Khi ViewModel bị destroyed, tất cả các Coroutines con sẽ tự động bị hủy để tránh leak bộ nhớ.

### 4.2 Optimizing with `SharingStarted.WhileSubscribed(5000)`
Trong [`HomeViewModel.kt`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/presentation/home/HomeViewModel.kt#L42):
```kotlin
started = SharingStarted.WhileSubscribed(5000)
```
* Khi không có Composable nào thu thập (subscribe) `restaurants` StateFlow (ví dụ ứng dụng chuyển sang background), Flow sẽ giữ bộ đệm 5000ms. Sau 5 giây, upstream flow sẽ tạm dừng để tiết kiệm CPU và pin.

---

## 5. Giải Thích Chi Tiết Các Annotations

| Annotation | Vị trí xuất hiện | Ý nghĩa & Mục đích kỹ thuật |
| :--- | :--- | :--- |
| **`@Composable`** | `HomeScreen()`, `FoodDetailScreen()`, `OrderTrackingScreen()`, v.v. | **Đánh dấu hàm phát sinh UI**: Báo cho Kotlin Compose Compiler chuyển đổi hàm thành nút trong cây giao diện (UI Tree). |
| **`@OptIn(...)`** | `HomeScreenContent`, `FoodDetailContent`, `OrderTrackingScreen` | **Chấp nhận Experimental API**: Ví dụ `@OptIn(ExperimentalMaterial3Api::class)`. Dùng khi gọi các component Material 3 (như `Scaffold`, `TopAppBar`) đang ở trạng thái thử nghiệm/đang phát triển API. |
| **`@Preview`** | `HomeScreenPreview()`, `FoodDetailPreview()` | **Render xem trước trên IDE**: Cho phép Android Studio render giao diện trực tiếp trên màn hình Design/Preview mà không cần chạy app lên emulator hoặc thiết bị thật. |

---

## 6. Sơ Đồ Kiến Trúc & Luồng Dữ Liệu

```mermaid
flowchart TD
    subgraph UI ["Presentation Layer (Compose UI)"]
        A[HomeScreen / FoodDetailScreen] -->|User Action: Click / Search| B[ViewModel Method Call]
        A <-- Collect State via collectAsState() -- C[StateFlow / UiState]
    end

    subgraph ViewModel ["ViewModel Layer"]
        B --> D[viewModelScope Coroutine]
        D --> E[Update MutableStateFlow / UiState]
        E --> C
    end

    subgraph Domain ["Domain Layer (Business Logic)"]
        D --> F[FoodProduct / DiscountCalculator / PaymentMethod]
    end

    subgraph Data ["Data Layer (Repository)"]
        F --> G[FakeRestaurantRepository]
    end
```

---

## 7. Đánh Giá & Best Practices

### Ưu điểm nổi bật trong dự án QuickBite:
1. **Tuân thủ triệt để Unidirectional Data Flow (UDF)**: UI gửi Event đến ViewModel -> ViewModel xử lý logic -> Cập nhật StateFlow -> UI lắng nghe và Recompose.
2. **Type-Safe Loading State**: Lớp [`UiState<T>`](file:///C:/Users/Admin04/AndroidStudioProjects/QuickBite/app/src/main/java/com/example/quickbite/util/UiState.kt) giúp code UI xử lý Loading/Success/Error vô cùng rõ ràng và không bị lỡ trường hợp.
3. **Thread-Safe State Mutation**: Dùng `_items.update` tránh được race condition khi thao tác giỏ hàng.
4. **Cấu trúc Shared ViewModel chuẩn xác**: Giúp dữ liệu đơn hàng và giỏ hàng liền mạch giữa các màn hình.
