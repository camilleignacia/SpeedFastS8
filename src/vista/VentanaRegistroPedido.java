package vista;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;

/**
 * Formulario para registrar un nuevo pedido.
 */
public class VentanaRegistroPedido extends JFrame {

    private PedidoDAO pedidoDAO;
    private JTextField txtDireccion;
    private JComboBox<String> cbxTipo;

    public VentanaRegistroPedido() {
        this.pedidoDAO = new PedidoDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Registrar Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("Registrar Pedido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));

        panelCampos.add(new JLabel("Dirección de entrega:"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Tipo:"));
        cbxTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
        panelCampos.add(cbxTipo);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 13));
        panelBoton.add(btnGuardar);
        add(panelBoton, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarPedido());
    }

    private void guardarPedido() {
        String direccion = txtDireccion.getText().trim();
        String tipo = (String) cbxTipo.getSelectedItem();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección de entrega no puede estar vacía.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Pedido nuevoPedido = new Pedido(direccion, tipo);
            pedidoDAO.guardar(nuevoPedido);

            JOptionPane.showMessageDialog(this,
                    "Pedido registrado correctamente.\n"
                            + "Destino: " + direccion + "\n"
                            + "Tipo: " + tipo + "\n"
                            + "Estado: PENDIENTE",
                    "Pedido Registrado", JOptionPane.INFORMATION_MESSAGE);

            txtDireccion.setText("");
            cbxTipo.setSelectedIndex(0);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar el pedido:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}