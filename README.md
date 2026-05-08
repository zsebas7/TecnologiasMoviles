# Super Ahorro

Aplicación Android (Jetpack Compose) para registrar, consultar y analizar compras de supermercado con datos mock y navegación completa entre módulos.

## Objetivo académico

Este proyecto integra los temas principales de la materia:

- Activities y ciclo de vida.
- Intents explícitos e implícitos.
- UI declarativa con Jetpack Compose.
- Arquitectura MVVM.
- Corrutinas con `viewModelScope`.
- Internacionalización (`strings.xml`).
- Versionado con GitFlow.

## Stack técnico

- Kotlin + Android SDK
- Jetpack Compose + Material 3
- Navigation Compose
- ViewModel + StateFlow
- Corrutinas de Kotlin

## Dónde se usa cada concepto teórico

- **Activity principal**: entrada de la app en `app/src/main/java/com/undef/superahorro/caparrozruiz/MainActivity.kt`.
- **Compose app shell**: raíz visual y tema en `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/SuperAhorroApp.kt`.
- **Navegación (`NavHost`, rutas, bottom bar)**:
  - `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/navigation/SuperAhorroNavHost.kt`
  - `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/navigation/AppRoute.kt`
  - `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/navigation/BottomNavItem.kt`
- **MVVM (estado + lógica de pantalla)**: `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/viewmodel/`.
- **Modelos de dominio**: `app/src/main/java/com/undef/superahorro/caparrozruiz/data/model/`.
- **Mock data / repositorio fake**: `app/src/main/java/com/undef/superahorro/caparrozruiz/data/repository/FakeWalletRepository.kt`.
- **Corrutinas**:
  - Auth simulado (`delay`, loading): `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/viewmodel/AuthViewModel.kt`
  - Flujos de compras/productos: `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/viewmodel/NewPurchaseViewModel.kt`, `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/viewmodel/HistoryViewModel.kt`
- **Intents**:
  - Envío de consulta por email: `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/screens/settings/SettingsScreen.kt`
  - Selección/captura de imagen de ticket (simulada): `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/screens/purchase/NewPurchaseScreen.kt`
- **Internacionalización**:
  - Español: `app/src/main/res/values/strings.xml`
  - Inglés: `app/src/main/res/values-en/strings.xml`

## Módulos y pantallas

- **Identidad**: Splash, Login, Registro, Recuperación.
- **Principal**: Home (resumen y últimas compras), Historial, Detalle, Nueva compra, Nuevo producto.
- **Soporte/usuario**: Chat, Perfil, Settings, Más.
- **Análisis**: Estadísticas (indicadores y placeholders visuales).

Las pantallas están en `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/screens/`.

## Estructura del proyecto

- `app/src/main/java/com/undef/superahorro/caparrozruiz/data`: modelos y repositorio mock.
- `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/components`: componentes reutilizables.
- `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/navigation`: grafo y rutas.
- `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/screens`: pantallas Compose.
- `app/src/main/java/com/undef/superahorro/caparrozruiz/ui/viewmodel`: ViewModels por caso de uso.
- `app/src/main/res/values*`: recursos de texto y configuración visual.

## Configuración base

- Package / namespace: `com.undef.superahorro.caparrozruiz`
- Flujo de ramas: `main`, `develop`, `feature/*`, `release/*`, `hotfix/*`

## Ejecución

1. Abrir el proyecto en Android Studio.
2. Sincronizar Gradle.
3. Ejecutar el módulo `app` en emulador o dispositivo físico.

Si Android Studio muestra una configuración vieja de ejecución, recrear la Run Configuration de tipo `Android App` para el módulo `app` y lanzar la `Default Activity`.
