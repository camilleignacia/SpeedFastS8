package vista;

import dao.ConexionDB;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Asigna un repartidor a un pedido pendiente e inicia la entrega.
 */
public class VentanaAsignacion extends JFrame {

    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    private List<Pedido> pedidosPendientes = new ArrayList<>();
    private List<Repartidor> repartidores = new ArrayList<>();

    private JComboBox<String> cbxPedido;
    private JComboBox<String> cbxRepartidor;

    public VentanaAsignacion() {
        this.pedidoDAO = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Asignar Repartidor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Asignar Repartidor — Iniciar Entrega", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 17));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panelCampos.add(new JLabel("Pedido pendiente:"));
        cbxPedido = new JComboBox<>();
        panelCampos.add(cbxPedido);

        panelCampos.add(new JLabel("Repartidor:"));
        cbxRepartidor = new JComboBox<>();
        panelCampos.add(cbxRepartidor);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 0, 18, 0));
        JButton btnIniciar = new JButton("Iniciar Entrega");
        btnIniciar.setFont(new Font("Arial", Font.PLAIN, 13));
        panelBoton.add(btnIniciar);
        add(panelBoton, BorderLayout.SOUTH);

        btnIniciar.addActionListener(e -> iniciarEntrega());

        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                cargarDatos();
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
            }
        });

        cargarDatos();
    }

    /**
     * Carga pedidos pendientes y repartidores desde la BD.
     */
    private void cargarDatos() {
        cbxPedido.removeAllItems();
        cbxRepartidor.removeAllItems();

        try {
            pedidosPendientes = pedidoDAO.listarPendientes();
            repartidores = repartidorDAO.listarTodos();

            for (Pedido p : pedidosPendientes) {
                cbxPedido.addItem("#" + p.getId() + " - " + p.getDireccionEntrega());
            }
            for (Repartidor r : repartidores) {
                cbxRepartidor.addItem(r.getNombre());
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cambia el estado del pedido a EN_REPARTO y registra la entrega en la BD.
     * Ambas operaciones se ejecutan dentro de una única transacción para
     * garantizar que ninguna quede incompleta si ocurre un error.
     */
    private void iniciarEntrega() {
        if (pedidosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes para asignar.",
                    "Sin pedidos pendientes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay repartidores disponibles.\nRegistra al menos uno desde 'Gestionar Repartidores'.",
                    "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pedido pedido = pedidosPendientes.get(cbxPedido.getSelectedIndex());
        Repartidor repartidor = repartidores.get(cbxRepartidor.getSelectedIndex());

        try (Connection conn = ConexionDB.conectar()) {
            conn.setAutoCommit(false);

            try {
                // 1. Actualizar estado del pedido
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
                        "Entrega iniciada correctamente.\n"
                                + "Pedido #" + pedido.getId() + "\n"
                                + "Destino: " + pedido.getDireccionEntrega() + "\n"
                                + "Repartidor: " + repartidor.getNombre() + "\n"
                                + "Estado: EN_REPARTO",
                        "Entrega Iniciada", JOptionPane.INFORMATION_MESSAGE);

                cargarDatos();

            } catch (SQLException ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                        "Error al iniciar la entrega. Se revirtieron los cambios.\n" + ex.getMessage(),
                        "Error de transacción", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error de conexión con la base de datos:\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}