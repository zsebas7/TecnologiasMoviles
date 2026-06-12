# Super Ahorro

Aplicación Android (Jetpack Compose) para registrar, consultar y analizar compras de supermercado. Incluye persistencia local con Room y DataStore, consumo de APIs REST con Retrofit, escaneo de tickets por OCR y asistente de finanzas con IA (Gemini).

## Objetivo académico

Proyecto de la materia Aplicaciones Móviles (IUA). Integra los temas de las dos primeras entregas:

- Activities y ciclo de vida.
- Intents explícitos e implícitos (email, cámara, galería, compartir).
- UI declarativa con Jetpack Compose y Material 3.
- Arquitectura MVVM con separación por capas.
- Corrutinas con `viewModelScope`, `Flow`, `StateFlow`, `combine`.
- Persistencia con DataStore (preferencias) y Room (base de datos local).
- Networking con Retrofit y patrón offline-first.
- Internacionalización (`strings.xml` en español e inglés).
- Versionado con GitFlow.

## Stack técnico

| Capa | Tecnología |
|---|---|
| UI | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose |
| Estado | ViewModel + StateFlow |
| Preferencias | DataStore (Preferences) |
| Base de datos local | Room (SQLite) |
| Networking | Retrofit + Gson |
| IA / Chat | Gemini API (generativelanguage.googleapis.com) |
| OCR | OCR.space API |
| Concurrencia | Kotlin Coroutines |
| Lenguaje | Kotlin (JVM 11, minSdk 29, compileSdk 36) |

## Dónde se usa cada concepto teórico

### Activity y punto de entrada
- `MainActivity.kt` — única Activity, monta el `SuperAhorroApp` Composable.
- `SuperAhorroApplication.kt` — inicializa `AppContainer` con el contexto de aplicación.

### Navegación
- `ui/navigation/SuperAhorroNavHost.kt` — `NavHost` con todas las rutas; la splash decide Login vs Home según DataStore.
- `ui/navigation/AppRoute.kt` — rutas como constantes.
- `ui/navigation/BottomNavItem.kt` — ítems de la barra de navegación inferior.

### MVVM
- `ui/viewmodel/` — un ViewModel por caso de uso (Auth, Home, History, NewPurchase, Promotions, Stats, Chat, Profile, Settings).
- `ui/state/` — data classes de estado de UI inmutables.
- `data/repository/` — repositorios como única fuente de verdad para los ViewModels.

### DataStore (Preferences)
- `data/local/preferences/UserPreferencesDataSource.kt` — claves tipadas (`IS_LOGGED_IN`, `USER_NAME`, `USER_EMAIL`, `USER_CITY`, `NOTIFICATIONS_ENABLED`, `MONTHLY_SUMMARY_ENABLED`). Expone cada preferencia como `Flow`.
- `data/repository/DefaultWalletRepository.kt` — delega lectura/escritura de preferencias al DataSource.
- `ui/viewmodel/AuthViewModel.kt` — observa `isLoggedIn` vía `collectLatest`; `login()` y `register()` escriben a DataStore.
- `ui/viewmodel/ProfileViewModel.kt` — lee y guarda nombre, email y ciudad del usuario.
- `ui/viewmodel/SettingsViewModel.kt` — controla notificaciones y resumen mensual.

### Room (base de datos local)
- `data/local/AppDatabase.kt` — singleton `@Database` con tablas: `purchases`, `products`, `promotions`. Versión 2.
- `data/local/entity/PurchaseEntity.kt` — `@Entity(tableName = "purchases")`, `@PrimaryKey(autoGenerate = true)`.
- `data/local/entity/ProductEntity.kt` — `@Entity` con `ForeignKey` a `purchases` (`onDelete = CASCADE`) e `@Index` en `purchase_id`.
- `data/local/entity/PromotionEntity.kt` — caché de promociones de la API.
- `data/local/dao/PurchaseDao.kt` — `observeAll(): Flow<List<PurchaseEntity>>` (reactivo), `insert`, `update`, `deleteById`.
- `data/local/dao/ProductDao.kt` — `observeByPurchaseId()`, `observeAll()`, `insert`, `insertAll`, `update`, `deleteById`.
- `data/local/dao/PromotionDao.kt` — `observeAll()`, `upsertAll`, `deleteAll`.
- `data/repository/DefaultWalletRepository.kt` — mapea `Entity ↔ Domain` y expone Flows a los ViewModels.

### Networking (Retrofit + offline-first)
- `data/remote/RetrofitClient.kt` — instancias de Retrofit para cuatro endpoints: dummyjson.com (promociones), jsonplaceholder.typicode.com (sincronización), api.ocr.space (OCR) y generativelanguage.googleapis.com (Gemini).
- `data/remote/PromotionApiService.kt` — `@GET("products") suspend fun getPromotions(limit: Int)`.
- `data/repository/PromotionsRepository.kt` — patrón offline-first: emite la caché de Room inmediatamente vía `observePromotions(): Flow`, luego refresca desde la API con `refreshPromotions()` en `Dispatchers.IO`.
- `data/remote/OcrApiService.kt` + `data/repository/OcrRepository.kt` — sube imagen y parsea texto de ticket.
- `data/remote/GeminiApiService.kt` + `data/repository/ChatRepository.kt` — chat con IA usando el modelo Gemma 4 26B.

### Corrutinas
- Todos los ViewModels usan `viewModelScope.launch` para operaciones asíncronas.
- `HistoryViewModel` usa `combine()` para fusionar dos Flows (compras + productos) en un solo estado.
- `PromotionsRepository` usa `withContext(Dispatchers.IO)` para el acceso a red.

### Intents
- Email de soporte: `SettingsScreen.kt` — `Intent(Intent.ACTION_SENDTO)`.
- Selección de imagen de galería / captura con cámara: `NewPurchaseScreen.kt`.
- Compartir resumen de compra: `PurchaseDetailScreen.kt` — `Intent(Intent.ACTION_SEND)`.

### Internacionalización
- Español: `app/src/main/res/values/strings.xml`
- Inglés: `app/src/main/res/values-en/strings.xml`

## Módulos y pantallas

| Sección | Pantallas |
|---|---|
| Identidad | Splash, Login, Registro, Recuperación de contraseña |
| Principal | Home (resumen mensual + últimas compras), Historial, Detalle de compra, Nueva compra |
| Análisis | Estadísticas (diario / mensual) |
| Servicios | Promociones (con caché offline), Chat IA, Escaneo OCR |
| Usuario | Perfil, Ajustes |

Las pantallas están en `ui/screens/`.

## Estructura del proyecto

```
app/src/main/java/com/undef/superahorro/caparrozruiz/
├── core/
│   └── AppContainer.kt              # Inyección de dependencias manual (singleton)
├── data/
│   ├── dto/                         # Data Transfer Objects (respuestas de API)
│   ├── local/
│   │   ├── AppDatabase.kt           # Room DB (versión 2)
│   │   ├── dao/                     # PurchaseDao, ProductDao, PromotionDao
│   │   ├── entity/                  # Entidades Room
│   │   └── preferences/             # DataStore
│   ├── model/                       # Modelos de dominio
│   ├── remote/                      # Retrofit services + RetrofitClient
│   └── repository/                  # Repositorios (fuente de verdad)
├── ui/
│   ├── components/                  # Composables reutilizables
│   ├── navigation/                  # NavHost, rutas, bottom bar
│   ├── screens/                     # Pantallas por módulo
│   ├── state/                       # UI state data classes
│   ├── theme/                       # Colores, tipografía, tema Material 3
│   └── viewmodel/                   # ViewModels por caso de uso
└── util/
    ├── CurrencyExt.kt               # Double.toCurrencyString()
    └── TicketParser.kt              # Parser de texto OCR → ParsedTicket
```

## Configuración

### API Keys

El proyecto requiere dos API keys externas. Crearlas en `local.properties` (no comitear):

```properties
OCR_SPACE_API_KEY=tu_key_de_ocr_space
GEMINI_API_KEY=tu_key_de_google_ai_studio
```

Ambas se inyectan en `BuildConfig` vía `build.gradle.kts` y se consumen en `RetrofitClient`.

### Ejecución

1. Abrir el proyecto en Android Studio.
2. Agregar las API keys en `local.properties`.
3. Sincronizar Gradle.
4. Ejecutar el módulo `app` en emulador (API 29+) o dispositivo físico.

## Flujo de ramas

`main` → estable · `develop` → integración · `feature/*` → desarrollo · `release/*` / `hotfix/*` según necesidad.
