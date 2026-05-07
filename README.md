# Super Ahorro

Aplicación Android para registrar y visualizar gastos de supermercado.

## Primera entrega (UI)

- Flujo de identidad: Splash, Login, Registro y Olvidé mi contraseña.
- Módulo principal: Home con últimas compras, Historial, Nueva compra, Estadísticas y Chat opcional.
- Pantalla Más: acceso a Perfil y Settings.
- Gestión visual de compras: editar/eliminar compras y productos, adjuntar ticket (simulado).
- Tecnologías: Jetpack Compose, Navigation Compose, MVVM con mock data, Intents y corrutinas.

## Configuración base

- Package: `com.undef.superahorro.caparrozruiz`.
- Flujo GitFlow: `main`, `develop`, ramas `feature/*`.

## Estructura implementada

- Navegación principal con `NavHost`: Splash, Login, Registro, Recuperación, Home, Historial, Detalle, Nueva compra, Nuevo producto, Estadísticas, Chat y Más.
- Patrón MVVM con ViewModels por pantalla y repositorio mock compartido.
- Internacionalización lista en `values/strings.xml` y `values-en/strings.xml`.
- Settings incluye acción de cerrar sesión y consulta por email vía Intent.
- Nueva compra incluye simulación de adjunto de ticket (galería/cámara).
- UI refinada con estilo wallet: cards, acentos azules y layouts coherentes entre auth y pantallas principales.
