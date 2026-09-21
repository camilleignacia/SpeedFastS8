## SpeedFast - Diseñando Interfaces Gráficas para aplicaciones en Java.

---

## Descripción General
Actividad formativa de **Desarrollo Orientado a Objetos II** (Semana 6).
Extiende el sistema SpeedFast incorporando una interfaz gráfica construida con Java Swing.

---

## Estructura del proyecto

```
src/
├── modelo/
│ ├── EstadoPedido.java
│ ├── Pedido.java
│ ├── ControladorPedidos.java
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

## Ventanas
- **VentanaPrincipal:** menú de navegación con tres acciones disponibles.
- **VentanaRegistroPedido:** formulario con validación para registrar pedidos.
- **VentanaListaPedidos:** tabla con todos los pedidos registrados (JTable + DefaultTableModel).
- **VentanaAsignacion:** asigna un repartidor a un pedido pendiente e inicia la entrega.

---

## Autora
Camila Sepúlveda Marcos

---

© Duoc UC | Escuela de Informática y Telecomunicaciones