package vista;

import modelo.ControladorPedidos;
import modelo.EstadoPedido;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Asigna un repartidor a un pedido pendiente e inicia la entrega. */
public class VentanaAsignacion extends JFrame {

    private ControladorPedidos controlador;
    private List<Pedido>       pedidosPendientes;

    private JComboBox<String> cbxPedido;
    private JComboBox<String> cbxRepartidor;

    /** @param controlador Lista compartida de pedidos. */
    public VentanaAsignacion(ControladorPedidos controlador) {
        this.controlador       = controlador;
        this.pedidosPendientes = new ArrayList<>();
        inicializarVentana();
    }

    /** Configura la ventana de asignación de repartidores. */
    private void inicializarVentana() {
        setTitle("SpeedFast - Asignar Repartidor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Asignar Repartidor / Iniciar Entrega", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(18, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panelCampos.add(new JLabel("Pedido pendiente:"));
        cbxPedido = new JComboBox<>();
        cargarPedidosPendientes();
        panelCampos.add(cbxPedido);

        panelCampos.add(new JLabel("Repartidor:"));
        cbxRepartidor = new JComboBox<>(new String[]{
                "Sol López", "Germán Carmona", "Nicole Espinoza"
        });
        panelCampos.add(cbxRepartidor);

        add(panelCampos, BorderLayout.CENTER);

        // Botón iniciar entrega
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 0, 18, 0));
        JButton btnIniciar = new JButton("Iniciar Entrega");
        btnIniciar.setFont(new Font("Arial", Font.PLAIN, 13));
        panelBoton.add(btnIniciar);
        add(panelBoton, BorderLayout.SOUTH);

        btnIniciar.addActionListener(e -> iniciarEntrega());
    }

    /** Llena el combo con los pedidos que tienen estado PENDIENTE. */
    private void cargarPedidosPendientes() {
        cbxPedido.removeAllItems();
        pedidosPendientes.clear();
        for (Pedido p : controlador.getPedidos()) {
            if (p.getEstado() == EstadoPedido.PENDIENTE) {
                pedidosPendientes.add(p);
                cbxPedido.addItem("Pedido #" + p.getId() + " — " + p.getDireccionEntrega());
            }
        }
    }

    /** Cambia el estado del pedido seleccionado a EN_REPARTO. */
    private void iniciarEntrega() {
        if (pedidosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes para asignar.",
                    "Sin pedidos pendientes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int indice      = cbxPedido.getSelectedIndex();
        Pedido pedido   = pedidosPendientes.get(indice);
        String repartidor = (String) cbxRepartidor.getSelectedItem();

        pedido.setEstado(EstadoPedido.EN_REPARTO);

        JOptionPane.showMessageDialog(this,
                "Entrega iniciada correctamente.\n"
                        + "Pedido #"     + pedido.getId()               + "\n"
                        + "Destino: "    + pedido.getDireccionEntrega() + "\n"
                        + "Repartidor: " + repartidor                   + "\n"
                        + "Estado: "     + pedido.getEstado(),
                "Entrega Iniciada", JOptionPane.INFORMATION_MESSAGE);

        // Actualizar el combo
        cargarPedidosPendientes();
    }
}