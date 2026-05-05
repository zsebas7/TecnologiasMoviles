# Super Ahorro

Aplicacion Android para registrar y visualizar gastos de supermercado.

## Primera entrega (UI)

- Flujo de identidad: Splash, Login, Registro y Olvide mi contrasena.
- Modulo principal: Home con ultimas compras, Mi Perfil, Settings y Chat opcional.
- Tecnologias: Jetpack Compose, Navigation Compose, MVVM con mock data, Intents y corrutinas.

## Configuracion base

- Package: `com.undef.superahorro.caparrozruiz`.
- Flujo GitFlow: `main`, `develop`, ramas `feature/*`.

## Estructura implementada

- Navegacion principal con `NavHost`: Splash, Login, Registro, Recuperacion, Home, Perfil, Settings y Chat.
- Patron MVVM con ViewModels por pantalla y repositorio mock compartido.
- Internacionalizacion lista en `values/strings.xml` y `values-en/strings.xml`.
