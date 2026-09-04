# Android Native Research — State, Lifecycle, Coroutines & Architecture (Jetpack Compose)

> Task: [Android] Android Native Research - State, Lifecycle, Coroutines & Architecture
> Mục tiêu: Research toàn diện 4 mảng — Navigation/MVVM, State Management, Lifecycle & Side Effects, Coroutines & Flow — làm tài liệu tham khảo nội bộ và chuẩn bị buổi sharing team.

---

## Mục lục

1. [Navigation & MVVM Architecture](#1-navigation--mvvm-architecture)
2. [State Management](#2-state-management)
3. [Lifecycle & Side Effects](#3-lifecycle--side-effects)
4. [Coroutines & Flow](#4-coroutines--flow)
5. [Tổng kết & Best Practices](#5-tổng-kết--best-practices)
6. [Tài liệu tham khảo](#6-tài-liệu-tham-khảo)

---

## 1. Navigation & MVVM Architecture

### 1.1 Navigation Compose

**Dependency:**
```kotlin
implementation("androidx.navigation:navigation-compose:2.8.x")
```

**Thành phần cốt lõi:**
- `NavController`: quản lý back stack, điều hướng giữa các composable "destination".
- `NavHost`: container khai báo graph điều hướng, ánh xạ route → composable.

```kotlin
val navController = rememberNavController()

NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable("detail/{itemId}") { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId")
        DetailScreen(itemId)
    }
}
```

**Navigate với arguments:**
```kotlin
composable(
    route = "detail/{itemId}",
    arguments = listOf(navArgument("itemId") { type = NavType.StringType })
) { backStackEntry ->
    val itemId = backStackEntry.arguments?.getString("itemId")
}

// Điều hướng:
navController.navigate("detail/$itemId")
```

Đối với object phức tạp, khuyến nghị dùng **type-safe navigation** (Navigation Compose 2.8+, dùng `@Serializable` route object) thay vì string route thủ công.

**Nested navigation graph:**
- Gom nhóm các màn hình liên quan (VD: flow đăng nhập, flow onboarding) vào 1 sub-graph riêng, giúp tách biệt trách nhiệm và dễ tái sử dụng.
```kotlin
navigation(startDestination = "login", route = "auth") {
    composable("login") { LoginScreen() }
    composable("register") { RegisterScreen() }
}
```

**Bottom Navigation & Drawer:**
- Kết hợp `Scaffold` + `NavHost`: `BottomNavigation`/`NavigationBar` thay đổi `currentDestination` bằng `navController.navigate(route) { launchSingleTop = true; restoreState = true }` để tránh tạo lại stack trùng lặp.
- `ModalNavigationDrawer` cho drawer menu, phối hợp với `rememberDrawerState`.

**Deep Linking:**
```kotlin
composable(
    "detail/{itemId}",
    deepLinks = listOf(navDeepLink { uriPattern = "myapp://detail/{itemId}" })
) { ... }
```

### 1.2 MVVM Architecture với Compose

**Vai trò các layer:**
- **View (Composable)**: chỉ render UI dựa theo state, không chứa business logic.
- **ViewModel**: expose UI state (thường qua `StateFlow`), xử lý logic, gọi UseCase/Repository.
- **Model (Repository/UseCase)**: nguồn dữ liệu, business rule.

**Inject ViewModel với Hilt:**
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() { ... }

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) { ... }
```

**UI State modeling:** dùng 1 data class/sealed interface duy nhất đại diện toàn bộ trạng thái màn hình (xem chi tiết ở phần 2).

**Xử lý one-time event (Toast, navigation trigger):**
Đây là vấn đề kinh điển: state là "sticky" (recomposition có thể đọc lại), nhưng event (VD: hiện Toast, navigate) chỉ nên xảy ra **1 lần**. Có 2 cách tiếp cận phổ biến:

1. **Channel/SharedFlow cho one-shot event:**
```kotlin
private val _events = Channel<UiEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()

// Trong Composable:
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is UiEvent.ShowToast -> { /* show toast */ }
            is UiEvent.Navigate -> navController.navigate(event.route)
        }
    }
}
```
2. **Event field trong state + consume flag** (đơn giản hơn nhưng dễ lỗi nếu quên reset).

Khuyến nghị: dùng `Channel` (buffer hữu hạn, đảm bảo không miss event) hơn là `SharedFlow` cho one-shot event, vì `SharedFlow` mặc định replay có thể gây phát lại event khi có subscriber mới (VD: sau configuration change).

---

## 2. State Management

### 2.1 Recomposition là gì?

Recomposition là quá trình Compose runtime **gọi lại các Composable function** khi state mà chúng đọc thay đổi, để cập nhật UI. Compose theo dõi state thông qua cơ chế **Snapshot System** — khi 1 `State` object được đọc trong 1 Composable scope, Compose ghi nhận "dependency", và khi giá trị đổi, chỉ những scope đọc state đó mới bị recompose (smart recomposition), không phải toàn bộ cây UI.

### 2.2 `remember` và `mutableStateOf`

```kotlin
var count by remember { mutableStateOf(0) }
```
- `remember`: lưu giá trị qua các lần recomposition (nhưng **mất** khi Activity bị recreate do configuration change, trừ khi dùng `rememberSaveable`).
- `mutableStateOf`: tạo 1 `State` object observable, đọc trong Composable → tự động subscribe.

**`rememberSaveable`**: tương tự `remember` nhưng lưu vào `Bundle`, sống sót qua configuration change và process death (miễn là kiểu dữ liệu `Parcelable`/`Serializable` hoặc dùng `Saver` tùy chỉnh).

```kotlin
var text by rememberSaveable { mutableStateOf("") }
```

### 2.3 State Hoisting

Nguyên tắc: **đưa state ra khỏi Composable con, để Composable con "stateless"** — chỉ nhận `value` và `onValueChange` callback từ bên ngoài (composable cha hoặc ViewModel).

```kotlin
// Stateless (dễ test, dễ tái sử dụng, dễ preview)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(value = query, onValueChange = onQueryChange)
}

// Stateful (giữ state nội bộ hoặc hoist lên ViewModel)
@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    SearchBar(query = viewModel.query, onQueryChange = viewModel::onQueryChange)
}
```

Lợi ích: single source of truth, dễ unit test logic tách biệt UI, composable tái sử dụng linh hoạt hơn.

### 2.4 ViewModel + Compose: quan sát state

```kotlin
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // render theo uiState
}
```

- `collectAsState()`: collect Flow bất kể lifecycle của Composable (có thể gây collect ngầm khi app ở background → lãng phí tài nguyên).
- `collectAsStateWithLifecycle()` **(khuyến nghị)**: chỉ collect khi lifecycle ≥ `STARTED`, tự động dừng khi app vào background — tránh crash/leak và tiết kiệm tài nguyên.

### 2.5 UI State vs UI Event vs UI Effect

| Khái niệm | Đặc điểm | Ví dụ |
|---|---|---|
| **UI State** | Trạng thái "sticky", tồn tại lâu dài, đại diện cho những gì hiển thị | Danh sách item, loading flag |
| **UI Event** | Hành động từ user gửi lên ViewModel | Click nút, gõ text |
| **UI Effect** | Hành động one-shot từ ViewModel gửi xuống UI | Show toast, navigate, show snackbar |

Mô hình phổ biến: **MVI-like pattern** — `Intent/Event → ViewModel xử lý → State (StateFlow) + Effect (Channel)`.

### 2.6 Single Source of Truth & Unidirectional Data Flow (UDF)

- **Single source of truth**: mỗi phần dữ liệu chỉ có 1 nơi "sở hữu" (thường là ViewModel/Repository), UI chỉ đọc và render, không tự ý giữ bản sao lệch pha.
- **UDF**: dữ liệu chảy 1 chiều: `State ↓ (xuống UI)` và `Event ↑ (lên ViewModel)`. Giúp luồng dữ liệu dễ trace, debug, test.

```
   ┌─────────────┐   State (StateFlow)   ┌──────────┐
   │  ViewModel  │ ─────────────────────▶│    UI    │
   │             │◀───────────────────── │(Composable)
   └─────────────┘      Event (lambda)    └──────────┘
```

### 2.7 Immutable UI State

State nên là **immutable data class** — mỗi lần thay đổi tạo ra instance mới (`copy()`), thay vì mutate trực tiếp field. Điều này giúp Compose so sánh (structural equality) chính xác để quyết định recomposition, tránh bug khó debug do mutable state bị thay đổi ngoài luồng kiểm soát.

```kotlin
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null
)

_uiState.update { it.copy(isLoading = true) }
```

### 2.8 `StateFlow`, `SharedFlow`, `Channel`

| | StateFlow | SharedFlow | Channel |
|---|---|---|---|
| Giữ giá trị hiện tại | ✅ (luôn có 1 value) | Tùy config (`replay`) | ❌ |
| Nhiều subscriber | ✅ | ✅ | ❌ (1 receiver tiêu thụ) |
| Dùng cho | UI state | Event broadcast, log | One-shot event (Toast, navigate) |
| Conflated (bỏ giá trị cũ) | ✅ luôn | Tùy config | Tùy `Channel` capacity |

### 2.9 Loading / Success / Error / Empty state — Sealed Interface

```kotlin
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(val items: List<Item>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
```

Ưu điểm dùng `sealed interface` thay vì boolean flags rải rác: Compiler ép `when` phải exhaustive, tránh trạng thái mâu thuẫn (VD: vừa loading vừa error cùng lúc do quên set flag).

### 2.10 Derived State

- **`derivedStateOf`**: tính toán giá trị state phái sinh từ 1 hoặc nhiều state khác, **chỉ recompute khi giá trị đầu vào thực sự đổi** (không phải mỗi lần recomposition) — tối ưu hiệu năng khi tính toán tốn kém hoặc input đổi thường xuyên hơn output.
```kotlin
val listState = rememberLazyListState()
val showButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}
```
- **`snapshotFlow`**: chuyển đổi Compose `State` → cold `Flow`, hữu ích khi muốn dùng các Flow operator (`debounce`, `distinctUntilChanged`) trên giá trị state.
```kotlin
LaunchedEffect(listState) {
    snapshotFlow { listState.firstVisibleItemIndex }
        .distinctUntilChanged()
        .collect { index -> /* xử lý */ }
}
```

**Tránh state thừa (unnecessary state):** không lưu những gì có thể tính toán được từ state khác (dùng `derivedStateOf` hoặc tính trực tiếp trong composition), tránh duplicate source of truth.

### 2.11 State Restoration

- **Configuration change** (xoay màn hình, đổi ngôn ngữ...): Activity bị destroy & recreate → `remember` mất giá trị, nhưng `ViewModel` (nhờ `ViewModelStore` gắn với `NonConfigurationInstance`) và `rememberSaveable` (nhờ `Bundle`) vẫn giữ được.
- **Process death**: hệ thống có thể kill process khi thiếu bộ nhớ (app ở background). Lúc này `ViewModel` cũng mất — chỉ dữ liệu trong `SavedStateHandle` (ViewModel) hoặc `rememberSaveable` (UI) mới khôi phục được.
- **ViewModel lifecycle**: ViewModel sống từ khi được tạo đến khi scope liên kết (Activity/Fragment/NavBackStackEntry) bị destroy hoàn toàn (không tính configuration change) — đây là lý do ViewModel là nơi lý tưởng để giữ state cần sống lâu hơn 1 Composable.

---

## 3. Lifecycle & Side Effects

### 3.1 Side Effect APIs

| API | Dùng khi nào |
|---|---|
| `LaunchedEffect(key)` | Chạy coroutine gắn với composition, restart khi `key` đổi, hủy khi rời composition |
| `DisposableEffect(key)` | Cần cleanup (đăng ký/hủy đăng ký listener, broadcast receiver...) |
| `SideEffect` | Đồng bộ Compose state → non-Compose object (chạy mỗi lần recomposition thành công) |
| `produceState` | Chuyển đổi non-Compose state (callback, Flow không phải state) thành Compose `State` |
| `rememberCoroutineScope` | Lấy `CoroutineScope` gắn với composition để launch coroutine từ **event handler** (VD: onClick), không phải trong composition trực tiếp |
| `rememberUpdatedState` | Giữ tham chiếu "mới nhất" của 1 giá trị/lambda bên trong `LaunchedEffect` dài hạn mà không muốn effect bị restart khi giá trị đó đổi |

**Ví dụ `LaunchedEffect` vs `rememberCoroutineScope`:**
```kotlin
// LaunchedEffect: tự chạy khi vào composition, gắn với key
LaunchedEffect(userId) {
    viewModel.loadUser(userId)
}

// rememberCoroutineScope: launch theo user action
val scope = rememberCoroutineScope()
Button(onClick = {
    scope.launch { viewModel.onButtonClicked() }
}) { Text("Click") }
```

**`DisposableEffect` ví dụ:**
```kotlin
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> ... }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
}
```

**Effect keys & cancellation:** khi `key` của `LaunchedEffect`/`DisposableEffect` thay đổi, effect cũ bị hủy (coroutine cancel, `onDispose` gọi) và effect mới chạy lại. Dùng `Unit` làm key nếu muốn effect chỉ chạy đúng 1 lần khi composable enter composition.

### 3.2 Composition Lifecycle vs Activity Lifecycle vs ViewModel Lifecycle

- **Composition lifecycle**: gắn với việc 1 Composable có đang nằm trong cây UI hay không (enter/leave composition) — độc lập với Activity lifecycle, có thể "leave composition" mà Activity vẫn `RESUMED` (VD: item bị scroll ra khỏi `LazyColumn`).
- **Activity/Fragment lifecycle**: `CREATED → STARTED → RESUMED → ...→ DESTROYED`, theo hệ điều hành quản lý.
- **ViewModel lifecycle**: sống qua nhiều composition (composable có thể bị dispose/recreate nhiều lần) nhưng chỉ 1 ViewModel instance, cho đến khi scope chủ (Activity/NavBackStackEntry) bị destroy thật sự.

### 3.3 Tránh gọi API trùng lặp (duplicated API call)

Vấn đề: nếu gọi API trong `LaunchedEffect(Unit)` nhưng composable bị recompose nhiều lần do thay đổi state không liên quan, và key không ổn định → dễ gọi lại API không cần thiết.

Giải pháp:
- Đặt logic gọi API trong `ViewModel.init {}` (chạy đúng 1 lần theo ViewModel lifecycle) thay vì trong Composable.
- Nếu bắt buộc trigger từ Composable, dùng `LaunchedEffect(Unit)` (không phải key thay đổi liên tục) và đảm bảo composable đó không bị tạo lại không cần thiết.

### 3.4 Lifecycle-aware Flow Collection

`collectAsStateWithLifecycle()` (từ `androidx.lifecycle:lifecycle-runtime-compose`) tự động **dừng collect khi lifecycle xuống dưới `STARTED`** (app vào background) và **resume khi trở lại** — tránh lãng phí tài nguyên, tránh crash khi update UI lúc không active, và khắc phục vấn đề nổi tiếng của `collectAsState()` "collect ngầm mãi mãi" kể cả khi màn hình không hiển thị.

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

---

## 4. Coroutines & Flow

### 4.1 Coroutine Fundamentals

- **Structured concurrency**: mọi coroutine phải chạy trong 1 `CoroutineScope`, khi scope bị hủy → toàn bộ coroutine con bị hủy theo, tránh leak.
- **CoroutineScope & Dispatchers**: `Dispatchers.Main` (UI), `Dispatchers.IO` (network/disk), `Dispatchers.Default` (CPU-intensive).
- **`viewModelScope`**: scope built-in gắn với ViewModel, tự hủy khi `ViewModel.onCleared()`.
```kotlin
viewModelScope.launch(Dispatchers.IO) {
    val result = repository.fetchData()
    withContext(Dispatchers.Main) { _uiState.update { it.copy(data = result) } }
}
```

**Cancellation & Exception Handling:**
```kotlin
viewModelScope.launch {
    try {
        val data = repository.fetchData()
    } catch (e: CancellationException) {
        throw e // luôn re-throw CancellationException
    } catch (e: Exception) {
        _uiState.update { it.copy(error = e.message) }
    }
}
```
- **`SupervisorJob`**: dùng khi muốn 1 coroutine con fail **không** hủy các coroutine con khác trong cùng scope (khác với `Job` mặc định — fail 1 con sẽ hủy toàn bộ).
- **`async/await`**: chạy song song nhiều task, `await()` gộp kết quả:
```kotlin
val userDeferred = async { repository.getUser() }
val postsDeferred = async { repository.getPosts() }
val user = userDeferred.await()
val posts = postsDeferred.await()
```

### 4.2 Flow

- **Cold Flow**: chỉ bắt đầu emit khi có collector (mỗi collector nhận luồng riêng, tính từ đầu). VD: `flow { ... }`, kết quả từ Room/Retrofit.
- **Hot Flow**: emit độc lập với collector, nhiều collector share cùng 1 luồng. VD: `StateFlow`, `SharedFlow`.

**`stateIn` / `shareIn`**: chuyển 1 cold Flow → hot Flow (StateFlow/SharedFlow), thường dùng khi expose Flow từ Repository lên ViewModel:
```kotlin
val uiState: StateFlow<HomeUiState> = repository.getDataFlow()
    .map { HomeUiState(items = it) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )
```
`SharingStarted.WhileSubscribed(5000)`: giữ Flow active 5s sau khi subscriber cuối cùng biến mất, tránh restart liên tục khi xoay màn hình.

**Các operator quan trọng:**
- `combine`: gộp nhiều Flow, emit khi bất kỳ nguồn nào đổi.
- `flatMapLatest`: khi có giá trị mới từ Flow nguồn, hủy Flow con trước đó, chỉ giữ Flow con mới nhất (hữu ích cho search-as-you-type).
- `debounce`: chỉ emit sau khi ngừng phát 1 khoảng thời gian — dùng cho search input.
- `distinctUntilChanged`: bỏ qua giá trị trùng với giá trị trước.
- `retry` / `catch`: xử lý lỗi trong Flow pipeline (network request retry).

```kotlin
searchQueryFlow
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query -> repository.search(query) }
    .catch { emit(emptyList()) }
    .collect { results -> ... }
```

**`flowWithLifecycle` / `collectAsStateWithLifecycle`**: đảm bảo Flow chỉ collect khi lifecycle đủ điều kiện (tương tự phần 3.4), tránh leak và xử lý dữ liệu khi UI không hiển thị.

---

## 5. Tổng kết & Best Practices

1. **UDF nghiêm ngặt**: State luôn chảy xuống, Event luôn chảy lên; không để Composable tự ý mutate state của ViewModel.
2. **Immutable state + sealed interface** cho Loading/Success/Error/Empty — tránh tổ hợp trạng thái vô nghĩa.
3. **Tách UI State và UI Effect** — dùng `Channel` cho one-shot event, không nhét Toast/Navigate vào state thường trực.
4. **Luôn dùng `collectAsStateWithLifecycle()`** thay vì `collectAsState()` trong production code.
5. **`stateIn` với `WhileSubscribed(5000)`** khi expose Flow từ Repository → ViewModel, tránh restart flow liên tục qua configuration change.
6. **Đặt side-effect có ý nghĩa (gọi API) trong `ViewModel.init`**, hạn chế phụ thuộc vào `LaunchedEffect` cho logic quan trọng.
7. **Phân biệt rõ** `remember` (mất khi configuration change) vs `rememberSaveable` (sống sót configuration change) vs ViewModel state (sống sót configuration change, mất khi process death trừ khi dùng `SavedStateHandle`).
8. **`derivedStateOf`** cho giá trị tính toán từ state khác để tránh recomposition/tính toán thừa.

---

## 6. Tài liệu tham khảo

- Android Developers — [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state)
- Android Developers — [Lifecycle of composables](https://developer.android.com/develop/ui/compose/lifecycle)
- Android Developers — [Side-effects in Compose](https://developer.android.com/develop/ui/compose/side-effects)
- Android Developers — [Navigation with Compose](https://developer.android.com/develop/ui/compose/navigation)
- Android Developers — [Kotlin flows on Android](https://developer.android.com/kotlin/flow)
- Android Developers — [ViewModel overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- Medium — Jetpack Compose Roadmap for Android Developers (tham khảo gốc từ task)

---

*Tài liệu này chuẩn bị cho buổi sharing team về Jetpack Compose — phần State, Lifecycle, Coroutines & Architecture.*
