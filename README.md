# Restaurant FLK

Aplicación Android para restaurantes desarrollada con **Kotlin**, **Jetpack Compose**, **MVVM**, **Koin** y preparada para **Firebase**.

## Incluye

- Jetpack Compose + Material 3
- Arquitectura MVVM + Clean Architecture
- Autenticación y base de datos en tiempo real con Firebase
- Navegación por rutas
- Integración de API externa
- Recursos e imágenes incluidos

## 🚀 Stack Tecnológico

- Kotlin
- Jetpack Compose
- MVVM + Arquitectura Limpia (Clean Architecture)
- Firebase Authentication
- Cloud Firestore
- Firebase Storage
- RESTCountries API

## 🔧 Funcionalidades Principales

### 🔹 Autenticación y Perfiles de Usuario

- Firebase Auth (Inicio de sesión con Google + perfiles personalizados)
- Sincronización de datos de usuario en tiempo real con Firestore
- Selector de país con RESTCountries API + autorrellenado del prefijo telefónico

### 🔹 Gestión de Productos para Administradores

- Añadir, actualizar y eliminar productos
- Subir imágenes de productos a Firebase Storage
- Actualizaciones de la interfaz en tiempo real con estados de carga y error

### 🔹 Catálogo de Productos y Proceso de Compra

- Vista general de productos con animaciones, categorías y favoritos
- Carrito, productos sugeridos y categorías
- Proceso de pago (Checkout) y pago rápido en un solo paso
- Pago con PayPal / Tarjeta de crédito por implementar (en un futuro).

## 🧱 Arquitectura

- Arquitectura Limpia (Clean Architecture) con Casos de Uso (Use Cases) y patrón Repositorio (Repository)
- Componentes reutilizables modularizados (Composables) para la interfaz gráfica

## 📸 Capturas de pantalla

<table>
  <tr>
    <th>Splash Screen</th>
    <th>Login</th>
    <th>Menu</th>
    <th>Productos</th>
    <th>Detalles</th>
    <th>Categorias</th>
    <th>Recomendaciones</th>
    <th>Carrito</th>
    <th>Checkout</th>
  </tr>

  <tr>
    <td>
      <img height="240" alt="Splash Screen" src="https://github.com/user-attachments/assets/09549665-949d-45a2-a695-961022b27463" />
    </td>
    <td>
      <img height="240" alt="Login" src="https://github.com/user-attachments/assets/b74fd954-5936-43a6-b285-a20ed4b26526" />
    </td>
    <td>
      <img height="240" alt="Menu" src="https://github.com/user-attachments/assets/d2a85a22-e3f7-4dc0-9907-81e570ffb183" />
    </td>
    <td>
      <img height="240" alt="Productos" src="https://github.com/user-attachments/assets/4d30aa5a-0307-4bdc-b136-9d738767417d" />
    </td>
    <td>
      <img height="240" alt="Detalles" src="https://github.com/user-attachments/assets/c20aeaab-d278-4e87-86b2-ab39374f8473" />
    </td>
    <td>
      <img height="240" alt="Categorias" src="https://github.com/user-attachments/assets/c1535ae4-3176-494a-a06c-2aa2c6edf93a" />
    </td>
    <td>
      <img height="240" alt="Recomendaciones" src="https://github.com/user-attachments/assets/3886e7d4-1507-429a-94d6-e3d442824693" />
    </td>
    <td>
      <img height="240" alt="Carrito" src="https://github.com/user-attachments/assets/4f87dc06-b1bc-4321-8be5-828b05719491" />
    </td>
    <td>
      <img height="240" alt="Checkout" src="https://github.com/user-attachments/assets/7cc5a6c2-c663-4c1d-a8b9-6db1f5d2cbde" />
    </td>
  </tr>
</table>
