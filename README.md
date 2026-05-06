# Super Ahorro

Aplicación Android para registrar y visualizar gastos de supermercado.

## Primera entrega (UI)

- Flujo de identidad: Splash, Login, Registro y Olvidé mi contraseña.
- Módulo principal: Home con últimas compras, Mi Perfil, Settings y Chat opcional.
- Tecnologías: Jetpack Compose, Navigation Compose, MVVM con mock data, Intents y corrutinas.

## Configuración base

- Package: `com.undef.superahorro.caparrozruiz`.
- Flujo GitFlow: `main`, `develop`, ramas `feature/*`.

## Estructura implementada

- Navegación principal con `NavHost`: Splash, Login, Registro, Recuperación, Home, Perfil, Settings y Chat.
- Patrón MVVM con ViewModels por pantalla y repositorio mock compartido.
- Internacionalización lista en `values/strings.xml` y `values-en/strings.xml`.
- Settings incluye acción de cerrar sesión y consulta por email vía Intent.
- UI refinada con estilo wallet: cards, acentos azules y layouts coherentes entre auth y pantallas principales.
