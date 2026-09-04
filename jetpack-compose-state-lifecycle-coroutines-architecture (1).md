# Android Native Research — State, Lifecycle, Coroutines & Architecture (Jetpack Compose)

> Task: [Android] Android Native Research - State, Lifecycle, Coroutines & Architecture
> Mỗi mục dưới đây trình bày theo format: **Khái niệm** (là gì) → **Mục đích** (dùng để làm gì / giải quyết vấn đề gì) → ví dụ code minh hoạ (nếu cần).

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

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| `navigation-compose` | Thư viện điều hướng chính thức của Google dành riêng cho Compose | Cho phép định nghĩa và điều hướng giữa các màn hình bằng route (string/object) thay vì Fragment transaction thủ công |
| `NavController` | Đối tượng quản lý back stack và trạng thái điều hướng hiện tại | Là "bộ não" điều khiển: gọi `navigate()`, `popBackStack()`, biết đang ở màn nào |
| `NavHost` | Composable container khai báo toàn bộ navigation graph | Ánh xạ route → Composable tương ứng, là nơi duy nhất chứa `NavController` |

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

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Navigate với arguments | Truyền dữ liệu (thường là ID) qua route khi điều hướng | Cho màn đích biết cần load dữ liệu nào, tránh phải truyền cả object lớn |
| Nested navigation graph | Gom nhóm nhiều destination liên quan vào 1 sub-graph có route riêng | Tách biệt logic 1 luồng (VD: luồng auth, luồng onboarding) khỏi graph chính, dễ tái sử dụng và bảo trì |
| Bottom navigation / Drawer integration | Kết hợp `NavController` với `NavigationBar`/`ModalNavigationDrawer` | Đồng bộ tab/menu đang chọn với destination hiện tại, tránh tạo lại stack trùng lặp khi bấm lại tab |
| Deep linking | Cho phép mở thẳng 1 màn hình cụ thể từ URI bên ngoài app (link, notification) | Hỗ trợ mở app đúng ngữ cảnh (VD: click link chia sẻ → mở thẳng trang chi tiết sản phẩm) |

```kotlin
composable(
    "detail/{itemId}",
    deepLinks = listOf(navDeepLink { uriPattern = "myapp://detail/{itemId}" })
) { ... }
```

### 1.2 MVVM Architecture với Compose

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| MVVM (Model-View-ViewModel) | Kiến trúc phân tách UI (View), logic/state (ViewModel), dữ liệu (Model) | Giảm phụ thuộc lẫn nhau giữa các layer, dễ test, dễ maintain khi app lớn dần |
| Inject ViewModel với Hilt | Dùng `hiltViewModel()` để lấy instance ViewModel đã được DI framework khởi tạo sẵn dependency | Tránh phải tự new ViewModel + Repository thủ công trong Composable, đảm bảo đúng scope (theo NavBackStackEntry hoặc Activity) |
| UI state modeling | Thiết kế 1 class duy nhất (thường `data class`) đại diện toàn bộ trạng thái cần cho 1 màn hình | Đảm bảo "single source of truth" cho UI, tránh rải rác nhiều biến `mutableStateOf` lẻ tẻ khó đồng bộ |
| One-time UI event | Sự kiện chỉ nên xảy ra đúng 1 lần (Toast, navigate, show Snackbar) — khác với state là "sticky" | Nếu nhét vào state thường trực, event có thể bị lặp lại khi recomposition hoặc configuration change → cần cơ chế riêng (`Channel`) |

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
}

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

**Vì sao dùng `Channel` thay vì `SharedFlow` cho one-shot event:** `Channel` đảm bảo mỗi event chỉ được tiêu thụ đúng 1 lần bởi 1 collector (giống hàng đợi), trong khi `SharedFlow` có thể replay event cho subscriber mới (VD: sau configuration change) gây lặp Toast/navigate không mong muốn.

---

## 2. State Management

### 2.1 Recomposition

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Recomposition | Quá trình Compose runtime gọi lại các Composable function khi state mà chúng đọc bị thay đổi | Cập nhật UI tự động theo dữ liệu mới, không cần thao tác `findViewById`/`notifyDataSetChanged` như View system cũ |
| Smart recomposition | Compose chỉ recompose đúng những scope (Composable) thực sự đọc state bị đổi, nhờ Snapshot System theo dõi "ai đọc gì" | Tối ưu hiệu năng — tránh vẽ lại toàn bộ cây UI chỉ vì 1 state nhỏ thay đổi |

### 2.2 `remember` và `mutableStateOf`

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| `mutableStateOf` | Tạo ra 1 `State` object có thể observe được (đọc trong Composable sẽ tự động subscribe) | Là "viên gạch" cơ bản để Compose biết khi nào cần recompose |
| `remember` | Lưu 1 giá trị/object để nó tồn tại qua các lần recomposition, thay vì bị tạo mới mỗi lần | Tránh mất state (VD: giá trị input) mỗi khi Composable recompose |
| `rememberSaveable` | Giống `remember` nhưng lưu thêm vào `Bundle` (qua `SavedStateRegistry`) | Giữ được giá trị qua **configuration change** (xoay màn hình) và cả **process death**, không như `remember` thường sẽ mất |

```kotlin
var count by remember { mutableStateOf(0) }          // mất khi xoay màn hình
var text by rememberSaveable { mutableStateOf("") }   // sống sót khi xoay màn hình
```

### 2.3 State Hoisting & Stateless/Stateful Composable

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| State hoisting | Kỹ thuật "đẩy" state ra khỏi Composable con, đưa lên Composable cha (hoặc ViewModel) sở hữu, Composable con chỉ nhận `value` + callback (`onValueChange`) | Tách state khỏi UI logic → Composable con dễ test, dễ preview, tái sử dụng được ở nhiều nơi với nguồn state khác nhau |
| Stateless Composable | Composable không tự giữ state nội bộ, hoàn toàn phụ thuộc tham số truyền vào | Dễ test (chỉ cần truyền input, assert output UI), không side-effect |
| Stateful Composable | Composable tự quản lý state của chính nó (dùng `remember` nội bộ) | Tiện lợi cho state cục bộ đơn giản, không cần chia sẻ ra ngoài (VD: trạng thái mở/đóng của 1 dropdown) |

```kotlin
// Stateless — dễ test, dễ tái sử dụng
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(value = query, onValueChange = onQueryChange)
}
```

### 2.4 ViewModel + Compose

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Quan sát LiveData/StateFlow từ ViewModel | Composable đọc giá trị hiện tại của luồng dữ liệu bất đồng bộ trong ViewModel | Cầu nối giữa business logic (ViewModel) và UI (Composable) theo mô hình reactive |
| `collectAsState()` | Hàm mở rộng chuyển 1 `Flow`/`StateFlow` thành Compose `State`, collect **không quan tâm lifecycle** | Đơn giản, nhưng nhược điểm là tiếp tục collect kể cả khi UI ở background → lãng phí tài nguyên |
| `collectAsStateWithLifecycle()` | Tương tự nhưng chỉ collect khi lifecycle ≥ `STARTED`, tự dừng khi xuống background | **Khuyến nghị dùng trong production** — tránh lãng phí CPU/network, tránh update UI khi không hiển thị |

```kotlin
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

### 2.5 UI State vs UI Event vs UI Effect

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| UI State | Trạng thái "sticky" — tồn tại lâu dài, đại diện cho những gì đang hiển thị trên màn hình tại 1 thời điểm | Nguồn dữ liệu để Compose render UI |
| UI Event | Hành động người dùng gửi **lên** ViewModel (click, gõ chữ, kéo thả) | Kích hoạt xử lý logic trong ViewModel theo hướng UDF (đi ngược chiều với State) |
| UI Effect | Hành động one-shot ViewModel gửi **xuống** UI, không nên lưu như state thường trực | Thực thi 1 hành động đúng 1 lần (Toast, navigate, scroll to top) mà không bị lặp lại do recomposition |

### 2.6 Single Source of Truth & Unidirectional Data Flow (UDF)

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Single source of truth | Nguyên tắc mỗi phần dữ liệu chỉ có đúng 1 nơi "sở hữu" chính thức (thường là ViewModel/Repository) | Tránh tình trạng nhiều nơi giữ bản sao dữ liệu lệch pha nhau, gây bug khó tái hiện |
| Unidirectional Data Flow (UDF) | Nguyên tắc dữ liệu chỉ chảy 1 chiều: **State chảy xuống UI**, **Event chảy lên ViewModel** | Giúp luồng dữ liệu dễ trace, dễ debug, dễ test vì logic thay đổi state luôn nằm ở 1 chỗ (ViewModel) |

```
ViewModel ── State (StateFlow) ──▶ UI
ViewModel ◀── Event (lambda) ──── UI
```

### 2.7 Immutable UI State

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Immutable state | State được định nghĩa bằng `data class` với `val` (không cho mutate trực tiếp), mỗi thay đổi tạo instance mới qua `copy()` | Đảm bảo Compose so sánh (structural equality) chính xác để quyết định recomposition; tránh bug do state bị thay đổi "lén" ngoài luồng kiểm soát của ViewModel |

```kotlin
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList()
)
_uiState.update { it.copy(isLoading = true) }
```

### 2.8 `StateFlow`, `SharedFlow`, `Channel`

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| `StateFlow` | Hot flow luôn giữ **1 giá trị hiện tại**, conflated (bỏ giá trị cũ nếu chưa kịp collect) | Đại diện lý tưởng cho UI State — luôn có giá trị để hiển thị ngay cả khi collector đến muộn |
| `SharedFlow` | Hot flow tổng quát hơn, có thể cấu hình `replay`, không bắt buộc có giá trị khởi tạo | Dùng để broadcast sự kiện cho nhiều subscriber (VD: log, analytics) |
| `Channel` | Hàng đợi (queue) chỉ có 1 receiver tiêu thụ mỗi giá trị đúng 1 lần | Phù hợp nhất cho **one-shot event** (Toast, navigate) vì đảm bảo không bị mất và không bị phát lại |

### 2.9 Loading / Success / Error / Empty State — Sealed Interface

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Sealed interface cho UI state | Dùng `sealed interface` với các nhánh con (`Loading`, `Success`, `Error`, `Empty`) thay vì nhiều boolean flag rời rạc | Compiler ép buộc `when` phải xử lý đầy đủ (exhaustive), loại bỏ khả năng tồn tại trạng thái mâu thuẫn (VD: vừa loading vừa error do quên reset flag) |

```kotlin
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(val items: List<Item>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
```

### 2.10 Derived State

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Derived state | Giá trị state được tính toán/suy ra từ 1 hoặc nhiều state khác, thay vì lưu độc lập | Tránh trùng lặp nguồn dữ liệu (duplicate source of truth) |
| `derivedStateOf` | API tạo derived state chỉ recompute khi **giá trị đầu vào thực sự đổi**, không phải mỗi lần recomposition | Tối ưu hiệu năng khi input đổi thường xuyên hơn nhiều so với output cần thiết (VD: tính "có nên hiện nút scroll-to-top" từ vị trí scroll) |
| `snapshotFlow` | Chuyển đổi Compose `State` (snapshot) thành cold `Flow` | Cho phép áp dụng các Flow operator (`debounce`, `distinctUntilChanged`...) lên giá trị state |
| Tránh state thừa | Nguyên tắc không lưu những gì có thể tính toán được từ state đã có | Giảm nguy cơ state bị lệch pha, giảm số lượng state cần đồng bộ thủ công |

```kotlin
val listState = rememberLazyListState()
val showButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 0 }
}
```

### 2.11 State Restoration

| Khái niệm | Định nghĩa | Mục đích khi tìm hiểu |
|---|---|---|
| Configuration change | Sự kiện hệ thống destroy & recreate Activity khi thay đổi cấu hình (xoay màn hình, đổi ngôn ngữ, đổi theme...) | Hiểu vì sao `remember` thường mất giá trị nhưng ViewModel/`rememberSaveable` thì không |
| Process death | Hệ điều hành chủ động kill process app khi thiếu bộ nhớ, lúc app đang ở background | Hiểu giới hạn: kể cả ViewModel cũng mất, chỉ dữ liệu trong `SavedStateHandle`/`rememberSaveable` (lưu vào Bundle hệ thống) mới khôi phục được |
| ViewModel lifecycle | Vòng đời ViewModel: được tạo khi cần, sống qua nhiều lần recomposition/configuration change, chỉ bị huỷ khi scope chủ (Activity/NavBackStackEntry) destroy thật sự | Giải thích vì sao ViewModel là nơi lý tưởng để giữ state cần sống lâu hơn 1 Composable, nhưng vẫn cần `SavedStateHandle` nếu muốn sống sót qua process death |

---

## 3. Lifecycle & Side Effects

### 3.1 Side Effect APIs

| Khái niệm | Định nghĩa | Mục đích |   |
|---|---|---|---|
| `LaunchedEffect(key)` | Composable function chạy 1 coroutine gắn với composition; coroutine restart khi `key` đổi, huỷ khi rời composition | Thực thi tác vụ bất đồng bộ (gọi API, delay, collect flow) đúng lúc composable xuất hiện hoặc khi 1 giá trị cụ thể đổi |   |
| `DisposableEffect(key)` | Side effect có bước dọn dẹp bắt buộc (`onDispose`) khi composable rời composition hoặc key đổi | Dùng khi cần đăng ký/huỷ đăng ký listener, BroadcastReceiver, callback ngoài Compose — tránh leak |   |
| `SideEffect` | Chạy 1 block code sau **mỗi lần recomposition thành công** (không có cleanup, không phải coroutine) | Đồng bộ giá trị Compose state sang object non-Compose (VD: cập nhật thư viện analytics/crash reporting) |   |
| `produceState` | Chuyển đổi nguồn dữ liệu non-Compose (callback API, Flow bên thứ 3) thành Compose `State` | Wrap API bất đồng bộ không phải Flow/State thành dạng Compose có thể đọc trực tiếp |   |
| `rememberCoroutineScope` | Lấy về 1 `CoroutineScope` gắn với composition, dùng để launch coroutine **từ event handler** (onClick...) chứ không launch ngay trong composition | Cho phép trigger coroutine theo hành động người dùng, khác với `LaunchedEffect` (tự chạy khi vào composition) |   |
| `rememberUpdatedState` | Giữ tham chiếu "mới nhất" của 1 giá trị/lambda để dùng bên trong 1 `LaunchedEffect` chạy dài hạn, mà không muốn effect đó bị restart mỗi khi giá trị đổi | Tránh phải đưa giá trị vào key của `LaunchedEffect` (gây restart không cần thiết) nhưng vẫn đảm bảo dùng giá trị mới nhất |   |
| Effect keys | Tham số key truyền vào `LaunchedEffect`/`DisposableEffect`, quyết định khi nào effect cũ bị huỷ và effect mới chạy lại | Kiểm soát chính xác thời điểm effect chạy lại, tránh chạy lại quá thường xuyên hoặc không đủ |   |
| Effect cancellation | Khi key đổi hoặc composable rời composition, coroutine trong effect tự động bị `cancel()` | Đảm bảo tài nguyên (coroutine, listener) không bị leak khi không còn cần thiết |   |

```kotlin
// LaunchedEffect: tự chạy khi vào composition
LaunchedEffect(userId) { viewModel.loadUser(userId) }

// rememberCoroutineScope: launch theo hành động người dùng
val scope = rememberCoroutineScope()
Button(onClick = { scope.launch { viewModel.onButtonClicked() } }) { Text("Click") }

// DisposableEffect: cần cleanup
DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event -> /* ... */ }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
}
```

### 3.2 Composition Lifecycle vs Activity Lifecycle vs ViewModel Lifecycle

| Khái niệm | Định nghĩa | Mục đích tìm hiểu |
|---|---|---|
| Composition lifecycle | Vòng đời riêng của 1 Composable: enter composition → (có thể recompose nhiều lần) → leave composition | Hiểu rằng 1 Composable có thể "biến mất" (VD: bị scroll khỏi `LazyColumn`) mà Activity vẫn `RESUMED` — độc lập với Activity lifecycle |
| Activity/Fragment lifecycle | Vòng đời hệ điều hành quản lý: `CREATED → STARTED → RESUMED → PAUSED → STOPPED → DESTROYED` | Là "khung" bên ngoài chứa composition — Composition bị huỷ hoàn toàn khi Activity destroy |
| ViewModel lifecycle | Sống xuyên suốt qua nhiều lần composition bị tạo/huỷ, chỉ mất khi scope chủ (Activity/NavBackStackEntry) destroy thật sự (không tính configuration change) | Giải thích lý do ViewModel phù hợp giữ state/logic cần bền hơn vòng đời UI |
| Handling configuration changes | Chiến lược giữ dữ liệu qua configuration change: dựa vào ViewModel (tự động) + `rememberSaveable` cho state UI thuần tuý | Tránh mất dữ liệu người dùng khi xoay màn hình |

### 3.3 Tránh gọi API trùng lặp (duplicated API call)

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Duplicated API call | Lỗi gọi lại API không cần thiết do đặt logic gọi API sai chỗ (VD: trong `LaunchedEffect` với key không ổn định, hoặc composable bị recompose nhiều lần) | Nhận diện nguyên nhân để tránh lãng phí băng thông, tránh race-condition dữ liệu |
| Giải pháp | Đặt lời gọi API quan trọng trong `ViewModel.init {}` (chạy đúng 1 lần theo ViewModel lifecycle) thay vì phụ thuộc vào composition | Đảm bảo API chỉ gọi đúng 1 lần bất kể composable bị tạo/huỷ lại bao nhiêu lần |

### 3.4 Lifecycle-aware Flow Collection

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Lifecycle-aware Flow collection | Cơ chế collect Flow có nhận biết trạng thái lifecycle, tự dừng/tiếp tục theo lifecycle state | Tránh lãng phí tài nguyên và tránh update UI khi màn hình không hiển thị |
| `collectAsStateWithLifecycle()` | API (từ `lifecycle-runtime-compose`) chỉ collect Flow khi lifecycle ≥ `STARTED`, tự dừng khi xuống background, resume khi quay lại | Giải pháp khuyến nghị thay thế `collectAsState()` trong mọi ViewModel-to-UI binding ở production |
| `flowWithLifecycle` | Toán tử tương tự nhưng dùng trực tiếp trên `Flow` (thường ở tầng ngoài Compose, VD: Activity/Fragment) | Áp dụng nguyên tắc lifecycle-aware collection ở những nơi không dùng Composable |

---

## 4. Coroutines & Flow

### 4.1 Coroutine Fundamentals

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| Coroutine | Đơn vị công việc bất đồng bộ, nhẹ hơn Thread, có thể tạm dừng (suspend) và tiếp tục mà không block thread | Viết code bất đồng bộ theo phong cách tuần tự (sequential), dễ đọc hơn callback lồng nhau |
| Structured concurrency | Nguyên tắc mọi coroutine phải chạy trong 1 `CoroutineScope` xác định; khi scope bị huỷ, toàn bộ coroutine con bị huỷ theo | Đảm bảo không có coroutine "mồ côi" chạy mãi gây leak khi màn hình đã đóng |
| `CoroutineScope` | Đối tượng định nghĩa phạm vi sống của coroutine (gắn với 1 Job) | Kiểm soát vòng đời nhóm coroutine — huỷ toàn bộ cùng lúc khi không cần nữa |
| `Dispatchers` | Chỉ định coroutine chạy trên thread pool nào: `Main` (UI), `IO` (network/disk), `Default` (CPU-intensive) | Đảm bảo tác vụ nặng không chạy trên Main thread gây giật UI |
| `viewModelScope` | `CoroutineScope` built-in gắn với ViewModel, tự huỷ khi `ViewModel.onCleared()` được gọi | Launch coroutine trong ViewModel mà không cần tự quản lý huỷ thủ công |
| Cancellation | Cơ chế dừng 1 coroutine đang chạy (chủ động qua `cancel()` hoặc do scope cha bị huỷ) | Giải phóng tài nguyên khi kết quả không còn cần thiết (VD: user rời màn hình giữa lúc đang load) |
| Exception handling | Cách bắt và xử lý lỗi xảy ra trong coroutine (`try/catch`, `CoroutineExceptionHandler`) | Tránh crash app khi có lỗi network/parse, đồng thời hiển thị trạng thái Error phù hợp cho UI |
| `SupervisorJob` | Loại Job đặc biệt: 1 coroutine con fail sẽ **không** huỷ các coroutine con khác trong cùng scope | Dùng khi cần chạy nhiều tác vụ độc lập song song, lỗi 1 tác vụ không nên ảnh hưởng tác vụ khác |
| `async/await` | `async` chạy coroutine trả về `Deferred`, `await()` chờ và lấy kết quả | Chạy nhiều tác vụ song song rồi gộp kết quả, thay vì chạy tuần tự tốn thời gian |

```kotlin
viewModelScope.launch(Dispatchers.IO) {
    try {
        val userDeferred = async { repository.getUser() }
        val postsDeferred = async { repository.getPosts() }
        val result = userDeferred.await() to postsDeferred.await()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        _uiState.update { it.copy(error = e.message) }
    }
}
```

### 4.2 Flow

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| `Flow` | Luồng dữ liệu bất đồng bộ, có thể emit nhiều giá trị theo thời gian | Đại diện cho dữ liệu thay đổi liên tục (VD: kết quả query Room, response streaming) |
| Cold Flow | Flow chỉ bắt đầu emit khi có collector, mỗi collector nhận luồng riêng biệt tính từ đầu | Phù hợp cho tác vụ 1-lần theo yêu cầu (VD: gọi API, đọc DB) — không tốn tài nguyên khi chưa có ai collect |
| Hot Flow | Flow emit độc lập với việc có collector hay không, nhiều collector share chung 1 luồng | Phù hợp cho state/event dùng chung (VD: `StateFlow` cho UI state) |
| `StateFlow` | Hot flow luôn giữ 1 giá trị hiện tại | (xem mục 2.8) |
| `SharedFlow` | Hot flow tổng quát, cấu hình được replay | (xem mục 2.8) |
| `stateIn` | Toán tử chuyển 1 cold Flow thành `StateFlow` (hot) | Dùng khi expose Flow từ Repository (thường là cold) lên ViewModel dưới dạng `StateFlow` cho UI quan sát |
| `shareIn` | Toán tử chuyển cold Flow thành `SharedFlow` | Dùng khi cần chia sẻ 1 Flow cho nhiều subscriber mà không cần giá trị khởi tạo mặc định như `stateIn` |
| `combine` | Gộp nhiều Flow, emit giá trị mới mỗi khi **bất kỳ** nguồn nào thay đổi | Kết hợp nhiều nguồn dữ liệu độc lập (VD: filter + search query) thành 1 kết quả tổng hợp |
| `flatMapLatest` | Khi Flow nguồn emit giá trị mới, huỷ Flow con đang xử lý trước đó và chỉ giữ Flow con mới nhất | Tránh xử lý kết quả cũ không còn liên quan (VD: search-as-you-type, chỉ quan tâm kết quả của từ khoá mới nhất) |
| `debounce` | Chỉ emit giá trị sau khi nguồn ngừng phát trong 1 khoảng thời gian nhất định | Giảm số lần gọi API khi user đang gõ liên tục (search input) |
| `distinctUntilChanged` | Bỏ qua giá trị mới nếu giống hệt giá trị emit trước đó | Tránh xử lý/re-render thừa khi giá trị không thực sự đổi |
| `retry` | Tự động thử lại khi Flow gặp lỗi | Tăng độ ổn định khi gọi network trong điều kiện mạng chập chờn |
| `catch` | Bắt exception xảy ra trong Flow pipeline (upstream) | Xử lý lỗi tập trung mà không cần try/catch lồng nhau tại nơi collect |

```kotlin
searchQueryFlow
    .debounce(300)
    .distinctUntilChanged()
    .flatMapLatest { query -> repository.search(query) }
    .catch { emit(emptyList()) }
    .collect { results -> /* update state */ }
```

| Khái niệm | Định nghĩa | Mục đích |
|---|---|---|
| `flowWithLifecycle` | (xem mục 3.4) | |
| `collectAsStateWithLifecycle` | (xem mục 2.4, 3.4) | |

---

## 5. Tổng kết & Best Practices

1. **UDF nghiêm ngặt**: State luôn chảy xuống, Event luôn chảy lên; Composable không tự ý mutate state của ViewModel.
2. **Immutable state + sealed interface** cho Loading/Success/Error/Empty — tránh tổ hợp trạng thái mâu thuẫn.
3. **Tách UI State và UI Effect**: dùng `Channel` cho one-shot event, không nhét Toast/Navigate vào state thường trực.
4. **Luôn dùng `collectAsStateWithLifecycle()`** thay vì `collectAsState()` trong production code.
5. **`stateIn` với `WhileSubscribed(5000)`** khi expose Flow từ Repository → ViewModel, tránh restart flow liên tục qua configuration change.
6. **Đặt side-effect có ý nghĩa (gọi API) trong `ViewModel.init`**, hạn chế phụ thuộc `LaunchedEffect` cho logic quan trọng.
7. **Phân biệt rõ**: `remember` (mất khi configuration change) vs `rememberSaveable` (sống sót configuration change) vs ViewModel state (sống sót configuration change, mất khi process death trừ khi dùng `SavedStateHandle`).
8. **`derivedStateOf`** cho giá trị tính toán từ state khác, tránh recomposition/tính toán thừa.

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

*Tài liệu chuẩn bị cho buổi sharing team về Jetpack Compose — phần State, Lifecycle, Coroutines & Architecture.*
