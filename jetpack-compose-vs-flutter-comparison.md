# So sánh Jetpack Compose vs Flutter — State, Lifecycle, Coroutines & Architecture

> Tài liệu bổ sung, đối chiếu các khái niệm ở tài liệu chính với Flutter — dựa trên stack bạn đang dùng (feature-first, `flutter_riverpod`, `freezed`, `fpdart`, `dio`/`retrofit`) để dễ liên hệ khi research.

---

## 1. Navigation & MVVM Architecture

| Compose | Flutter tương ứng | Ghi chú khác biệt |
|---|---|---|
| `NavController` | `GoRouter` / `Navigator` (router object) | Cùng vai trò điều khiển back stack; nếu bạn dùng `go_router`, `GoRouter.of(context)` ≈ `NavController` |
| `NavHost` + `composable(route)` | `GoRoute` list trong `GoRouter` config | Compose khai báo route ngay trong cây UI (composable function); Flutter/go_router khai báo route tách biệt thành 1 config object |
| Navigate với arguments | `context.go('/detail/$id')` hoặc `extra:` param | Compose thường nhét arg vào route string; go_router có thêm `extra` để truyền object phức tạp trực tiếp (không cần serialize) |
| Nested navigation graph | `ShellRoute` / nested `GoRoute` | Cùng mục đích: gom nhóm luồng con (VD: auth flow, tab flow) |
| Bottom navigation + `launchSingleTop`/`restoreState` | `StatefulShellRoute` (giữ state riêng từng tab) | go_router's `StatefulShellRoute` giải quyết đúng vấn đề "không tạo lại stack khi đổi tab" mà Compose cần config thủ công bằng `launchSingleTop`/`restoreState` |
| Deep linking (`navDeepLink`) | Deep linking qua `GoRouter` (`uriPattern` tương tự) hoặc `Uri` parsing thủ công | Tương tự về mục đích, khác cú pháp cấu hình |
| MVVM: `ViewModel` (Hilt) | `Notifier`/`AsyncNotifier` (Riverpod) hoặc `ChangeNotifier` (Provider cũ) | ViewModel Android gắn với lifecycle Activity/NavBackStackEntry; Riverpod `Notifier` gắn với `ProviderScope`/`ref` — về bản chất đều là "nơi giữ state + business logic tách khỏi UI" |
| `hiltViewModel()` | `ref.watch(myNotifierProvider)` | Cả 2 đều là cơ chế DI để lấy instance state-holder đã cấu hình sẵn dependency |
| One-shot event qua `Channel` | Không có khái niệm tương đương built-in — thường tự implement bằng 1 `StreamController` hoặc field kiểu `AsyncValue`/callback 1 lần, hoặc dùng package như `flutter_bloc` (`BlocListener` cho side-effect) | Đây là điểm Compose/Android có pattern chuẩn hoá rõ hơn (Channel + collect trong LaunchedEffect); Flutter cộng đồng dùng nhiều cách khác nhau tùy state management lib |

---

## 2. State Management

| Compose | Flutter tương ứng | Ghi chú khác biệt |
|---|---|---|
| Recomposition | Rebuild (Flutter gọi lại `build()`) | Cùng cơ chế declarative: state đổi → UI tự vẽ lại. Compose recompose theo scope nhờ Snapshot System (fine-grained); Flutter rebuild theo `Widget` (thường coarser, cần `const`/`Consumer` để tối ưu) |
| `mutableStateOf` + `remember` | `useState`-style hoặc `StateProvider`/`ref.watch` (Riverpod), hoặc `setState()` trong `StatefulWidget` | Riverpod's `StateProvider`/`NotifierProvider` gần với `remember { mutableStateOf() }` hơn `setState` truyền thống (vì tách state khỏi widget) |
| `rememberSaveable` | Không có built-in tương đương trực tiếp — dùng `RestorationMixin` (Flutter's State Restoration API) hoặc tự lưu vào local storage | Cả 2 đều nhằm sống sót qua process kill, nhưng cơ chế Flutter phức tạp hơn, ít dùng phổ biến bằng |
| State hoisting | Cùng khái niệm — "lift state up" | Thuật ngữ giống hệt trong React/Flutter world; bạn hẳn đã quen với pattern này qua Riverpod (Notifier hoist state ra khỏi Widget) |
| Stateless/Stateful Composable | `StatelessWidget` / `StatefulWidget` | Tên gọi gần như song song 1-1 |
| ViewModel quan sát `StateFlow` | `Notifier` expose `state` (Riverpod), UI dùng `ref.watch(provider)` | `StateFlow.collectAsStateWithLifecycle()` ≈ `ref.watch(provider)` — cả 2 đều tự động subscribe + tự huỷ khi widget/composable rời cây |
| UI State / UI Event / UI Effect | Giống pattern MVI bạn có thể đã áp dụng với Riverpod: `state` (AsyncValue/data class), `method` gọi trên Notifier (event), side-effect qua `ref.listen` | `ref.listen()` trong Riverpod chính là cách phổ biến để xử lý one-shot effect (show snackbar, navigate) — tương đương `Channel` + `LaunchedEffect` bên Compose |
| Single source of truth / UDF | Cùng nguyên tắc — Riverpod khuyến khích mạnh pattern này (`Notifier` sở hữu state, Widget chỉ đọc) | Không khác biệt về triết lý |
| Immutable state (`data class` + `copy()`) | `freezed` class + `copyWith()` | Gần như 1-1: bạn đã dùng `freezed`, nên tư duy immutable state này rất quen thuộc — `.copy()` (Kotlin) ≈ `.copyWith()` (freezed) |
| `StateFlow` / `SharedFlow` / `Channel` | `Stream` / `StreamController` (Dart), hoặc Riverpod `StreamProvider` | Dart `Stream` là khái niệm tổng quát hơn (không phân biệt hot/cold rõ ràng như Kotlin Flow); Riverpod thường trừu tượng hoá bớt phần này |
| Sealed interface cho Loading/Success/Error/Empty | `freezed` sealed union (`@freezed sealed class`) hoặc Riverpod's `AsyncValue<T>` (`.when(data:, loading:, error:)`) | Đây là điểm rất quen với bạn — `AsyncValue` của Riverpod bản chất là 1 sealed union built-in y hệt mục đích của sealed interface trong Compose |
| `derivedStateOf` | `Provider` (Riverpod) tính từ `ref.watch(otherProvider)` — tự động cache & chỉ rebuild khi input đổi | Riverpod `Provider` (không phải State/Notifier) chính là cơ chế derived/computed state, rất giống `derivedStateOf` về mục đích tối ưu |
| `snapshotFlow` | Không có tương đương trực tiếp (đặc thù Compose Snapshot System) | Flutter không có khái niệm "snapshot state → stream" vì kiến trúc state khác nền tảng |
| Configuration change / process death / ViewModel lifecycle | Flutter: `State` object mất khi hot reload/restart, nhưng Riverpod `ProviderScope` (nếu đặt ở root `runApp`) sống suốt vòng đời app trong bộ nhớ — Flutter **không có khái niệm configuration change gây recreate Activity** như Android (không dùng multi-Activity) | Đây là khác biệt nền tảng lớn: Flutter tự vẽ UI toàn bộ (không phụ thuộc Android Activity lifecycle bên dưới theo cách lộ ra ngoài), nên vấn đề "mất state khi xoay màn hình" gần như không tồn tại trừ khi bạn tự thiết kế sai (VD: state đặt trong `StatefulWidget` bị dispose khi Navigator pop/push lại) |

---

## 3. Lifecycle & Side Effects

| Compose | Flutter tương ứng | Ghi chú khác biệt |
|---|---|---|
| `LaunchedEffect(key)` | `initState()` (chạy 1 lần) hoặc `useEffect` nếu dùng `flutter_hooks` | `flutter_hooks`'s `useEffect(callback, [key])` gần như song song 1-1 với `LaunchedEffect(key)` — cùng cơ chế "chạy lại khi dependency đổi" |
| `DisposableEffect` | `initState()` + `dispose()` trong `StatefulWidget`, hoặc `useEffect` với return cleanup function (hooks) | `useEffect(() { ...; return () => cleanup(); }, [key])` ≈ `DisposableEffect` gần như 1-1 |
| `SideEffect` (chạy mỗi recomposition) | Không có tương đương trực tiếp; gần nhất là code đặt cuối `build()` hoặc `WidgetsBinding.instance.addPostFrameCallback` | Mục đích khác nhau đôi chút — Flutter thường tránh side-effect ngay trong `build()` |
| `produceState` | `useFuture`/`useStream` (flutter_hooks) hoặc `FutureBuilder`/`StreamBuilder` | Cùng mục đích: chuyển API bất đồng bộ non-reactive thành state UI đọc được |
| `rememberCoroutineScope` | Không cần khái niệm riêng — Dart cho phép gọi trực tiếp `async` function trong `onPressed` mà không cần "scope" tường minh (nhưng nên huỷ qua `mounted` check hoặc `CancelToken` với dio) | Dart không có structured concurrency built-in chặt như Kotlin coroutine — bạn phải tự check `if (!mounted) return` sau await để tránh lỗi setState sau dispose |
| `rememberUpdatedState` | Không có tương đương trực tiếp phổ biến | Đặc thù của cách Compose closure capture giá trị tại thời điểm composition |
| Composition lifecycle vs Activity lifecycle vs ViewModel lifecycle | Widget lifecycle (`createState → initState → build → dispose`) vs App lifecycle (`AppLifecycleState`: resumed/inactive/paused/detached) vs Provider/Notifier lifecycle (theo `ProviderScope`/`autoDispose`) | Khái niệm 3 tầng tương tự, nhưng Flutter gộp "Activity" thành 1 app lifecycle đơn giản hơn (không có multi-Activity như Android) |
| Tránh gọi API trùng lặp | Gọi API trong Notifier's `build()` (Riverpod v2, chạy 1 lần khi provider khởi tạo) thay vì trong Widget's `build()` | Cùng nguyên tắc: đặt side-effect quan trọng ở tầng state-management, không phải tầng UI widget |
| `collectAsStateWithLifecycle` | `ref.watch(provider)` (Riverpod tự dispose theo widget tree qua `autoDispose`) | Riverpod's `autoDispose` modifier giải quyết vấn đề tương tự — tự huỷ subscription khi không còn ai lắng nghe |

---

## 4. Coroutines & Flow

| Compose/Kotlin | Flutter/Dart tương ứng | Ghi chú khác biệt |
|---|---|---|
| Coroutine | `Future` (1 kết quả) / `async`-`await` (Dart) | Dart cũng có `async`/`await` cú pháp gần giống Kotlin, nhưng Dart coroutine không "suspend" theo structured concurrency chặt như Kotlin |
| Structured concurrency | Không có khái niệm chính thức tương đương — Dart không tự huỷ các `Future` con khi 1 scope cha bị huỷ | Đây là khác biệt lớn: bạn phải tự quản lý huỷ (VD: dùng `CancelToken` của `dio` để cancel request khi widget dispose) |
| `CoroutineScope` / `viewModelScope` | Không có scope built-in tương đương; Riverpod's `ref.onDispose()` dùng để cleanup thủ công | Riverpod cung cấp `ref.onDispose(() => cancelToken.cancel())` để mô phỏng lại hành vi tự huỷ giống `viewModelScope` |
| `Dispatchers` (Main/IO/Default) | `compute()` (chạy trên Isolate riêng cho CPU-intensive) | Dart single-threaded (event loop) trừ khi dùng `Isolate`; không có khái niệm "background thread pool" như Kotlin Dispatchers.IO — I/O trong Dart vẫn non-blocking qua event loop, không cần dispatcher riêng |
| Cancellation | `CancelToken` (dio), hoặc check `if (!mounted)` | Dio's `CancelToken` chính là cách bạn hay dùng để mô phỏng cancellation giống Kotlin coroutine |
| `SupervisorJob` | Không có tương đương trực tiếp — mỗi `Future`/`async` độc lập theo mặc định, lỗi 1 cái không tự propagate huỷ cái khác | Dart mặc định đã giống hành vi "supervisor" (lỗi không lan) nên không cần khái niệm riêng |
| `async { } / await` | `Future(() => ...)` + `await`, hoặc `Future.wait([f1, f2])` | `Future.wait` ≈ chạy song song nhiều `async{}.await()` của Kotlin |
| `Flow` | `Stream` (Dart) | Tương đương gần nhất — nhưng Dart Stream không phân biệt rạch ròi cold/hot bằng tên gọi như Kotlin |
| Cold Flow / Hot Flow | Single-subscription `Stream` (cold-like) vs Broadcast `Stream` (`StreamController.broadcast()`, hot-like) | Dart phân biệt qua `broadcast()` factory thay vì 2 loại class riêng như Kotlin (`Flow` vs `SharedFlow`/`StateFlow`) |
| `StateFlow` | Riverpod's `StateProvider`/`NotifierProvider` state, hoặc `ValueNotifier` | `ValueNotifier<T>` (Flutter built-in) gần nhất về mặt "luôn giữ 1 giá trị hiện tại + notify listener" |
| `stateIn` / `shareIn` | Không có toán tử tương đương trực tiếp; Riverpod tự động "cache" giá trị provider tương tự mục đích | Riverpod trừu tượng hoá phần này, bạn thường không cần tự chuyển đổi cold→hot thủ công như Kotlin Flow |
| `combine` | `Rx.combineLatest` (nếu dùng RxDart) hoặc `Provider` phụ thuộc nhiều `ref.watch` khác (Riverpod) | Riverpod cho phép 1 `Provider` watch nhiều provider khác — tự động re-run khi bất kỳ nguồn nào đổi, giống mục đích `combine` |
| `flatMapLatest` | `RxDart`'s `switchMap` | Cùng tên gọi khái niệm trong reactive programming (ReactiveX), chỉ khác binding ngôn ngữ |
| `debounce` | `RxDart`'s `debounceTime`, hoặc tự implement bằng `Timer` | Nếu không dùng RxDart, thường tự viết `Timer(Duration(...), callback)` + cancel timer cũ |
| `distinctUntilChanged` | `RxDart`'s `distinct()`, hoặc Riverpod tự động skip nếu giá trị `==` nhau (với `freezed`/`Equatable`) | Vì bạn dùng `freezed` (có `==` tự sinh), Riverpod provider mặc định đã tự "distinct" theo equality — lợi thế có sẵn từ stack hiện tại |
| `retry` / `catch` | `dio`'s interceptor retry, hoặc `try/catch` quanh `await` | dio thường xử lý retry ở tầng interceptor thay vì ở tầng Stream/Flow như Kotlin |
| `flowWithLifecycle` / `collectAsStateWithLifecycle` | `ref.watch()` tự dispose theo widget tree (đặc biệt với `autoDispose`) | Riverpod xử lý phần lifecycle-awareness này "miễn phí" hơn, ít cần cấu hình tường minh như Compose |

---

## 5. Nhận xét tổng quan

- **Tư duy declarative UI** (state → UI tự vẽ lại) là điểm chung lớn nhất giữa Compose và Flutter — kinh nghiệm Flutter/Riverpod của bạn sẽ giúp nắm nhanh phần lớn khái niệm state trong Compose.
- **Khác biệt lớn nhất nằm ở tầng concurrency**: Kotlin Coroutine/Flow có structured concurrency và phân loại hot/cold rõ ràng bằng type system; Dart/Flutter linh hoạt hơn nhưng phải tự kỷ luật quản lý cancellation (qua `CancelToken`, `mounted` check) thay vì được framework đảm bảo.
- **`freezed` + `AsyncValue` (Riverpod)** là điểm tương đồng gần nhất với **immutable data class + sealed interface** bên Compose — gần như cùng 1 triết lý, khác cú pháp.
- **Configuration change / process death** là khái niệm gần như đặc thù Android (do multi-Activity + Android OS quản lý bộ nhớ), không có tương đương trực tiếp bên Flutter vì Flutter tự vẽ toàn bộ UI trong 1 view duy nhất.
- **Lifecycle-awareness** (`collectAsStateWithLifecycle`) mà Compose phải cấu hình tường minh thì Riverpod's `ref.watch()` + `autoDispose` xử lý gần như "miễn phí" theo widget tree — đây là điểm bạn có thể thấy Compose "thủ công" hơn so với thói quen Riverpod.

---

*Tài liệu bổ sung — đối chiếu Jetpack Compose với Flutter, dùng kèm tài liệu chính "State, Lifecycle, Coroutines & Architecture".*
