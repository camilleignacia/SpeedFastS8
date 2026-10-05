# SpeedFast - Persistiendo datos con objetos y base de datos.

---

## Descripción General
Actividad sumativa de **Desarrollo Orientado a Objetos II** (Semana 8).
Extiende el sistema SpeedFast implementando operaciones CRUD completas
para repartidores, pedidos y entregas, conectadas a una base de datos
MySQL mediante JDBC e integradas con una interfaz gráfica Swing.

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
│ ├── VentanaRepartidores.java
│ ├── VentanaEntregas.java
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
- `pedido` — almacena los pedidos registrados. El ID se genera automáticamente.
- `entrega` — registra la relación entre pedido y repartidor, con fecha y hora.

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

- **VentanaPrincipal:** menú de navegación con acceso a todas las funcionalidades del sistema.
- **VentanaRegistroPedido:** formulario para registrar un nuevo pedido con dirección y tipo. El estado siempre inicia en PENDIENTE automáticamente y el ID lo genera la base de datos.
- **VentanaListaPedidos:** tabla con todos los pedidos registrados. Permite filtrar por estado, editar y eliminar pedidos existentes.
- **VentanaRepartidores:** gestión completa de repartidores — registrar nuevos, editar nombres y eliminar. Si un repartidor tiene entregas asociadas no puede eliminarse.
- **VentanaEntregas:** tabla con todas las entregas registradas. Permite editar el repartidor, fecha y hora de una entrega, y eliminarla. Al eliminar una entrega el pedido asociado vuelve automáticamente a estado PENDIENTE.
- **VentanaAsignacion:** asigna un repartidor a un pedido pendiente e inicia la entrega. El cambio de estado del pedido y el registro de la entrega se ejecutan dentro de una transacción para garantizar consistencia.

---

## Operaciones CRUD por entidad

| Entidad | Crear | Leer | Actualizar | Eliminar |
|---|---|---|---|---|
| Repartidor | ✓ | ✓ | ✓ | ✓ |
| Pedido | ✓ | ✓ | ✓ | ✓ |
| Entrega | ✓ | ✓ | ✓ | ✓ |

---

## Autora del proyecto
Camila Sepúlveda Marcos

---

© Duoc UC | Escuela de Informática y Telecomunicaciones