package vista;

import dao.ConexionDB;
import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para gestionar entregas con operaciones CRUD completas.
 */
public class VentanaEntregas extends JFrame {

    private EntregaDAO entregaDAO;
    private RepartidorDAO repartidorDAO;
    private PedidoDAO pedidoDAO;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private List<Repartidor> repartidores = new ArrayList<>();
    private List<Pedido> pedidosPendientes = new ArrayList<>();
    private List<Entrega> entregas = new ArrayList<>();

    private int idEntregaSeleccionada = -1;
    private int idPedidoSeleccionado = -1;

    // Campos para crear nueva entrega
    private JComboBox<String> cbxPedidoNuevo;
    private JComboBox<String> cbxRepartidorNuevo;

    // Campos para editar entrega existente
    private JComboBox<String> cbxRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    public VentanaEntregas() {
        this.entregaDAO = new EntregaDAO();
        this.repartidorDAO = new RepartidorDAO();
        this.pedidoDAO = new PedidoDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Gestionar Entregas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 660);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- NORTE ---
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Gestión de Entregas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // --- CENTRO: tabla ---
        String[] columnas = {"ID", "Pedido", "Repartidor", "Fecha", "Hora"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Al seleccionar una fila, llena los campos de edición
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabla.getSelectedRow();
                if (row >= 0) {
                    Entrega entrega = entregas.get(row);
                    idEntregaSeleccionada = entrega.getId();
                    idPedidoSeleccionado = entrega.getIdPedido();

                    int idRep = entrega.getIdRepartidor();
                    for (int i = 0; i < repartidores.size(); i++) {
                        if (repartidores.get(i).getId() == idRep) {
                            cbxRepartidor.setSelectedIndex(i);
                            break;
                        }
                    }

                    txtFecha.setText(entrega.getFecha().toString());
                    txtHora.setText(entrega.getHora().toString());
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- SUR: panel crear + panel editar ---
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BoxLayout(panelSur, BoxLayout.Y_AXIS));

        // --- Panel crear nueva entrega ---
        JPanel panelCrear = new JPanel(new BorderLayout());
        panelCrear.setBorder(BorderFactory.createTitledBorder("Nueva entrega"));

        JPanel panelCamposCrear = new JPanel(new GridLayout(2, 2, 10, 8));
        panelCamposCrear.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        panelCamposCrear.add(new JLabel("Pedido pendiente:"));
        cbxPedidoNuevo = new JComboBox<>();
        panelCamposCrear.add(cbxPedidoNuevo);

        panelCamposCrear.add(new JLabel("Repartidor:"));
        cbxRepartidorNuevo = new JComboBox<>();
        panelCamposCrear.add(cbxRepartidorNuevo);

        panelCrear.add(panelCamposCrear, BorderLayout.CENTER);

        JPanel panelBotonCrear = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnRegistrar = new JButton("Registrar Entrega");
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 12));
        panelBotonCrear.add(btnRegistrar);
        panelCrear.add(panelBotonCrear, BorderLayout.SOUTH);

        panelSur.add(panelCrear);

        // --- Panel editar entrega existente ---
        JPanel panelEditar = new JPanel(new BorderLayout());
        panelEditar.setBorder(BorderFactory.createTitledBorder("Editar entrega seleccionada"));

        JPanel panelCamposEditar = new JPanel(new GridLayout(3, 2, 10, 8));
        panelCamposEditar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        panelCamposEditar.add(new JLabel("Repartidor:"));
        cbxRepartidor = new JComboBox<>();
        panelCamposEditar.add(cbxRepartidor);

        panelCamposEditar.add(new JLabel("Fecha (yyyy-MM-dd):"));
        txtFecha = new JTextField();
        panelCamposEditar.add(txtFecha);

        panelCamposEditar.add(new JLabel("Hora (HH:mm:ss):"));
        txtHora = new JTextField();
        panelCamposEditar.add(txtHora);

        panelEditar.add(panelCamposEditar, BorderLayout.CENTER);

        JPanel panelBotonesEditar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        for (JButton btn : new JButton[]{btnGuardar, btnEliminar, btnRefrescar}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            panelBotonesEditar.add(btn);
        }
        panelEditar.add(panelBotonesEditar, BorderLayout.SOUTH);

        panelSur.add(panelEditar);

        add(panelSur, BorderLayout.SOUTH);

        // --- Listeners ---
        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarEntrega());
        btnRefrescar.addActionListener(e -> cargarEntregas());

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                cargarEntregas();
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
            }
        });

        cargarEntregas();
    }

    /**
     * Carga la tabla de entregas, los repartidores y los pedidos pendientes.
     */
    private void cargarEntregas() {
        modeloTabla.setRowCount(0);
        idEntregaSeleccionada = -1;
        idPedidoSeleccionado = -1;
        cbxRepartidor.removeAllItems();
        cbxRepartidorNuevo.removeAllItems();
        cbxPedidoNuevo.removeAllItems();

        try {
            repartidores = repartidorDAO.listarTodos();
            for (Repartidor r : repartidores) {
                cbxRepartidor.addItem(r.getNombre());
                cbxRepartidorNuevo.addItem(r.getNombre());
            }

            pedidosPendientes = pedidoDAO.listarPendientes();
            for (Pedido p : pedidosPendientes) {
                cbxPedidoNuevo.addItem("#" + p.getId() + " - " + p.getDireccionEntrega());
            }

            entregas = entregaDAO.listarTodos();
            for (Entrega ent : entregas) {
                modeloTabla.addRow(new Object[]{
                        ent.getId(),
                        ent.getTextoPedido(),
                        ent.getTextoRepartidor(),
                        ent.getFecha().toString(),
                        ent.getHora().toString()
                });
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registra una nueva entrega asociando un pedido pendiente con un repartidor.
     * El cambio de estado del pedido y el registro de la entrega se ejecutan
     * dentro de una única transacción para garantizar consistencia.
     */
    private void registrarEntrega() {
        if (pedidosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes para registrar una entrega.",
                    "Sin pedidos pendientes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores disponibles.",
                    "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido pedido = pedidosPendientes.get(cbxPedidoNuevo.getSelectedIndex());
        Repartidor repartidor = repartidores.get(cbxRepartidorNuevo.getSelectedIndex());

        try (Connection conn = ConexionDB.conectar()) {
            conn.setAutoCommit(false);

            try {
                // 1. Actualizar estado del pedido a EN_REPARTO
                String sql1 = "UPDATE pedido SET estado = ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
                    stmt.setString(1, EstadoPedido.EN_REPARTO.name());
                    stmt.setInt(2, pedido.getId());
                    stmt.executeUpdate();
                }

                // 2. Registrar la entrega
                String sql2 = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql2)) {
                    stmt.setInt(1, pedido.getId());
                    stmt.setInt(2, repartidor.getId());
                    stmt.setDate(3, Date.valueOf(LocalDate.now()));
                    stmt.setTime(4, Time.valueOf(LocalTime.now()));
                    stmt.executeUpdate();
                }

                conn.commit();

                JOptionPane.showMessageDialog(this,
                        "Entrega registrada correctamente.\n"
                                + "Pedido #" + pedido.getId() + "\n"
                                + "Destino: " + pedido.getDireccionEntrega() + "\n"
                                + "Repartidor: " + repartidor.getNombre() + "\n"
                                + "Estado: EN_REPARTO",
                        "Entrega Registrada", JOptionPane.INFORMATION_MESSAGE);

                cargarEntregas();

            } catch (SQLException ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                        "Error al registrar la entrega. Se revirtieron los cambios.\n" + ex.getMessage(),
                        "Error de transacción", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión con la base de datos:\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la entrega seleccionada con los datos del formulario.
     */
    private void guardarCambios() {
        if (idEntregaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una entrega de la tabla para editarla.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores disponibles.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String textoFecha = txtFecha.getText().trim();
        String textoHora = txtHora.getText().trim();

        if (textoFecha.isEmpty() || textoHora.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La fecha y la hora no pueden estar vacías.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fecha;
        LocalTime hora;
        try {
            fecha = LocalDate.parse(textoFecha);
            hora = LocalTime.parse(textoHora);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato incorrecto.\nUsa yyyy-MM-dd para la fecha y HH:mm:ss para la hora.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Repartidor repartidor = repartidores.get(cbxRepartidor.getSelectedIndex());
            Entrega actualizada = new Entrega(
                    idEntregaSeleccionada,
                    idPedidoSeleccionado,
                    repartidor.getId(),
                    fecha,
                    hora
            );
            entregaDAO.actualizar(actualizada);

            JOptionPane.showMessageDialog(this,
                    "Entrega #" + idEntregaSeleccionada + " actualizada correctamente.",
                    "Actualización", JOptionPane.INFORMATION_MESSAGE);
            cargarEntregas();

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar la entrega:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina la entrega seleccionada y revierte el pedido a PENDIENTE,
     * ya que la entrega que lo vinculaba con un repartidor ya no existe.
     */
    private void eliminarEntrega() {
        if (idEntregaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una entrega de la tabla para eliminarla.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la entrega #" + idEntregaSeleccionada + "?\n"
                        + "El pedido asociado volverá a estado PENDIENTE.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            try {
                entregaDAO.eliminar(idEntregaSeleccionada);
                pedidoDAO.actualizarEstado(idPedidoSeleccionado, EstadoPedido.PENDIENTE);

                JOptionPane.showMessageDialog(this,
                        "Entrega eliminada correctamente.\n"
                                + "El pedido #" + idPedidoSeleccionado + " volvió a estado PENDIENTE.",
                        "Eliminación", JOptionPane.INFORMATION_MESSAGE);

                idEntregaSeleccionada = -1;
                idPedidoSeleccionado = -1;
                cargarEntregas();

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar la entrega:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}