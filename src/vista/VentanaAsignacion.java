package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import modelo.Entrega;
import modelo.EstadoPedido;
import modelo.Pedido;
import modelo.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/** Asigna un repartidor a un pedido pendiente e inicia la entrega. */
public class VentanaAsignacion extends JFrame {

    private PedidoDAO     pedidoDAO;
    private RepartidorDAO repartidorDAO;
    private EntregaDAO    entregaDAO;

    private List<Pedido>      pedidosPendientes;
    private List<Repartidor>  repartidores;

    private JComboBox<String> cbxPedido;
    private JComboBox<String> cbxRepartidor;

    public VentanaAsignacion() {
        this.pedidoDAO     = new PedidoDAO();
        this.repartidorDAO = new RepartidorDAO();
        this.entregaDAO    = new EntregaDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast - Asignar Repartidor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Asignar Repartidor / Iniciar Entrega", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(18, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panelCampos.add(new JLabel("Pedido pendiente:"));
        cbxPedido = new JComboBox<>();
        cargarPedidosPendientes();
        panelCampos.add(cbxPedido);

        panelCampos.add(new JLabel("Repartidor:"));
        cbxRepartidor = new JComboBox<>();
        cargarRepartidores();
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
                cargarPedidosPendientes();
            }
            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {}
        });
    }

    /** Carga los pedidos pendientes desde la BD. */
    private void cargarPedidosPendientes() {
        cbxPedido.removeAllItems();
        pedidosPendientes = pedidoDAO.listarPendientes();
        for (Pedido p : pedidosPendientes) {
            cbxPedido.addItem("Pedido #" + p.getId() + " — " + p.getDireccionEntrega());
        }
    }

    /** Carga los repartidores desde la BD. */
    private void cargarRepartidores() {
        cbxRepartidor.removeAllItems();
        repartidores = repartidorDAO.listarTodos();
        for (Repartidor r : repartidores) {
            cbxRepartidor.addItem(r.getNombre());
        }
    }

    /** Cambia el estado del pedido a EN_REPARTO y guarda la entrega en la BD. */
    private void iniciarEntrega() {
        if (pedidosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes para asignar.",
                    "Sin pedidos pendientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int indice            = cbxPedido.getSelectedIndex();
        Pedido pedido         = pedidosPendientes.get(indice);
        Repartidor repartidor = repartidores.get(cbxRepartidor.getSelectedIndex());

        pedidoDAO.actualizarEstado(pedido.getId(), EstadoPedido.EN_REPARTO);

        Entrega entrega = new Entrega(
                pedido.getId(),
                repartidor.getId(),
                LocalDate.now(),
                LocalTime.now()
        );
        entregaDAO.guardar(entrega);

        JOptionPane.showMessageDialog(this,
                "Entrega iniciada correctamente.\n"
                        + "Pedido #"     + pedido.getId()               + "\n"
                        + "Destino: "    + pedido.getDireccionEntrega() + "\n"
                        + "Repartidor: " + repartidor.getNombre()       + "\n"
                        + "Estado: EN_REPARTO",
                "Entrega Iniciada", JOptionPane.INFORMATION_MESSAGE);

        cargarPedidosPendientes();
    }
}