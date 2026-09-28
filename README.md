# SpeedFast - Conectando aplicaciones Java con bases de datos mediante JDBC

---

## Descripción General
Actividad formativa de **Desarrollo Orientado a Objetos II** (Semana 7).
Extiende el sistema SpeedFast incorporando persistencia de datos mediante
conexión a una base de datos MySQL utilizando JDBC.

---

## Estructura del proyecto

```
src/
├── dao/
│ ├── ConexionDB.java
│ ├── PedidoDAO.java
│ ├── RepartidorDAO.java
│ └── EntregaDAO.java
├── modelo/
│ ├── EstadoPedido.java
│ ├── Pedido.java
│ ├── Entrega.java
│ ├── ZonaDeCarga.java
│ └── Repartidor.java
├── vista/
│ ├── VentanaPrincipal.java
│ ├── VentanaRegistroPedido.java
│ ├── VentanaListaPedidos.java
│ └── VentanaAsignacion.java
└── main/
└── Main.java
```
---

## Base de datos

El sistema utiliza MySQL con la base de datos `speedfast_db`.
El script de creación se encuentra en `speedfast_db.sql` en la raíz del proyecto.

**Tablas:**
- `repartidor` — almacena los repartidores disponibles.
- `pedido` — almacena los pedidos registrados.
- `entrega` — registra la relación entre pedido y repartidor.

**Para configurar la BD:**
1. Ejecutar `speedfast_db.sql` en MySQL Workbench
2. Configurar usuario y contraseña en `dao/ConexionDB.java`
3. Agregar el conector `mysql-connector-j-8.0.33.jar` al proyecto

---

## Configuración previa para ejecutar el proyecto

1. Instalar **MySQL Server** y ejecutar `speedfast_db.sql` en MySQL Workbench
2. En `dao/ConexionDB.java` actualizar las credenciales con las propias:
```java
   private static final String USUARIO  = "root";
   private static final String PASSWORD = "contraseña";
```
3. Tener agregado el conector `mysql-connector-j-8.0.33.jar` al proyecto en IntelliJ

---

## Ventanas
- **VentanaPrincipal:** menú de navegación con tres acciones disponibles.
- **VentanaRegistroPedido:** formulario con validación que guarda pedidos en la BD.
- **VentanaListaPedidos:** tabla con todos los pedidos registrados, incluyendo repartidor asignado.
- **VentanaAsignacion:** asigna un repartidor a un pedido pendiente, inicia la entrega y guarda el registro en la BD.

---

## Autora del proyecto
Camila Sepúlveda Marcos

---

© Duoc UC | Escuela de Informática y Telecomunicaciones