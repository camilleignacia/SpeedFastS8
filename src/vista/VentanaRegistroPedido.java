package vista;

import modelo.ControladorPedidos;
import modelo.Pedido;

import javax.swing.*;
import java.awt.*;

/** Formulario para registrar un nuevo pedido. */
public class VentanaRegistroPedido extends JFrame {

    private ControladorPedidos controlador;

    private JTextField   txtId;
    private JTextField   txtDireccion;
    private JComboBox<String> cbxTipo;

    /** @param controlador Lista compartida de pedidos. */
    public VentanaRegistroPedido(ControladorPedidos controlador) {
        this.controlador = controlador;
        inicializarVentana();
    }

    /** Configura el formulario de registro. */
    private void inicializarVentana() {
        setTitle("SpeedFast - Registrar Pedido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(380, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Registrar Pedido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 12));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));

        panelCampos.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelCampos.add(txtId);

        panelCampos.add(new JLabel("Dirección de entrega:"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Tipo:"));
        cbxTipo = new JComboBox<>(new String[]{"Comida", "Encomienda", "Express"});
        panelCampos.add(cbxTipo);

        add(panelCampos, BorderLayout.CENTER);

        // Botón guardar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 13));
        panelBoton.add(btnGuardar);
        add(panelBoton, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarPedido());
    }

    /** Valida los campos y agrega el pedido al controlador. */
    private void guardarPedido() {
        String textoId   = txtId.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String tipo      = (String) cbxTipo.getSelectedItem();

        // Validar que el ID no esté vacío
        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El ID no puede estar vacío.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el ID sea un número entero
        int id;
        try {
            id = Integer.parseInt(textoId);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número entero.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el ID sea positivo
        if (id <= 0) {
            JOptionPane.showMessageDialog(this,
                    "El ID debe ser un número positivo.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el ID no esté duplicado
        if (controlador.existeId(id)) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un pedido con el ID " + id + ".", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que la dirección no esté vacía
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección de entrega no puede estar vacía.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear y registrar el pedido
        Pedido nuevoPedido = new Pedido(id, direccion, tipo);
        controlador.agregarPedido(nuevoPedido);

        JOptionPane.showMessageDialog(this,
                "Pedido registrado correctamente.\n"
                        + "ID: "        + id        + "\n"
                        + "Destino: "   + direccion + "\n"
                        + "Tipo: "      + tipo,
                "Pedido Registrado", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar campos para registrar otro pedido
        txtId.setText("");
        txtDireccion.setText("");
        cbxTipo.setSelectedIndex(0);
    }
}