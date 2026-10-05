package vista;

import dao.PedidoDAO;
import modelo.EstadoPedido;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Muestra y gestiona los pedidos registrados en una JTable con CRUD completo.
 */
public class VentanaListaPedidos extends JFrame {

    private PedidoDAO pedidoDAO;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private int idSeleccionado = -1;

    // Filtro
    private JComboBox<String> cbxFiltro;

    // Campos de edición
    private JTextField txtDireccion;
    private JComboBox<String> cbxTipo;
    private JComboBox<String> cbxEstado;

    public VentanaListaPedidos() {
        this.pedidoDAO = new PedidoDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Gestión de Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 660);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- NORTE: título + filtro ---
        JPanel panelNorte = new JPanel(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Gestión de Pedidos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        panelNorte.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltro.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        panelFiltro.add(new JLabel("Filtrar por estado:"));
        cbxFiltro = new JComboBox<>(new String[]{"TODOS", "PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        cbxFiltro.addActionListener(e -> cargarPedidos());
        panelFiltro.add(cbxFiltro);
        panelNorte.add(panelFiltro, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: tabla ---
        String[] columnas = {"ID", "Dirección", "Tipo", "Estado", "Repartidor"};
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
                    idSeleccionado = (int) modeloTabla.getValueAt(row, 0);
                    txtDireccion.setText((String) modeloTabla.getValueAt(row, 1));
                    cbxTipo.setSelectedItem(modeloTabla.getValueAt(row, 2));
                    cbxEstado.setSelectedItem(modeloTabla.getValueAt(row, 3));
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- SUR: panel de edición + botones ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createTitledBorder("Editar pedido seleccionado"));

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 8));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(8, 15, 5, 15));

        panelCampos.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelCampos.add(txtDireccion);

        panelCampos.add(new JLabel("Tipo:"));
        cbxTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
        panelCampos.add(cbxTipo);

        panelCampos.add(new JLabel("Estado:"));
        cbxEstado = new JComboBox<>(new String[]{"PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        panelCampos.add(cbxEstado);

        panelSur.add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnNuevo = new JButton("Nuevo Pedido");
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        for (JButton btn : new JButton[]{btnNuevo, btnGuardar, btnEliminar, btnRefrescar}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            panelBotones.add(btn);
        }
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);

        // --- Listeners ---
        btnNuevo.addActionListener(e -> {
            new VentanaRegistroPedido().setVisible(true);
            tabla.clearSelection();
            idSeleccionado = -1;
        });
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnRefrescar.addActionListener(e -> cargarPedidos());

        // Refresca la tabla al volver el foco, por si se registró un pedido nuevo
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                cargarPedidos();
            }

            @Override
            public void windowLostFocus(java.awt.event.WindowEvent e) {
            }
        });

        cargarPedidos();
    }

    /**
     * Carga los pedidos según el filtro seleccionado.
     */
    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        idSeleccionado = -1;

        try {
            String filtro = (String) cbxFiltro.getSelectedItem();
            List<Pedido> pedidos = "TODOS".equals(filtro)
                    ? pedidoDAO.listarTodos()
                    : pedidoDAO.listarPorEstado(EstadoPedido.valueOf(filtro));

            for (Pedido p : pedidos) {
                modeloTabla.addRow(new Object[]{
                        p.getId(),
                        p.getDireccionEntrega(),
                        p.getTipo(),
                        p.getEstado().name(),
                        p.getRepartidor()
                });
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar pedidos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda los cambios del pedido seleccionado en la BD.
     */
    private void guardarCambios() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la tabla para editarlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String direccion = txtDireccion.getText().trim();
        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La dirección no puede estar vacía.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Pedido actualizado = new Pedido(idSeleccionado, direccion, (String) cbxTipo.getSelectedItem());
            actualizado.setEstado((String) cbxEstado.getSelectedItem());
            pedidoDAO.actualizar(actualizado);

            JOptionPane.showMessageDialog(this,
                    "Pedido #" + idSeleccionado + " actualizado correctamente.",
                    "Actualización", JOptionPane.INFORMATION_MESSAGE);
            cargarPedidos();

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar el pedido:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina el pedido seleccionado de la BD con confirmación previa.
     */
    private void eliminarPedido() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un pedido de la tabla para eliminarlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el pedido #" + idSeleccionado + "?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            try {
                pedidoDAO.eliminar(idSeleccionado);
                JOptionPane.showMessageDialog(this,
                        "Pedido #" + idSeleccionado + " eliminado correctamente.",
                        "Eliminación", JOptionPane.INFORMATION_MESSAGE);
                idSeleccionado = -1;
                cargarPedidos();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar el pedido #" + idSeleccionado + ".\n"
                                + "Primero elimina la entrega asociada desde 'Gestionar Entregas'.",
                        "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}