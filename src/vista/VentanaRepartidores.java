package vista;

import dao.RepartidorDAO;
import modelo.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana para gestionar repartidores con operaciones CRUD completas.
 */
public class VentanaRepartidores extends JFrame {

    private RepartidorDAO repartidorDAO;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtNombre;
    private int idSeleccionado = -1;

    public VentanaRepartidores() {
        this.repartidorDAO = new RepartidorDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Gestionar Repartidores");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(490, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- NORTE ---
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Gestión de Repartidores", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // --- CENTRO: tabla ---
        String[] columnas = {"ID", "Nombre"};
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

        // Al seleccionar una fila, llena el campo nombre
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabla.getSelectedRow();
                if (row >= 0) {
                    idSeleccionado = (int) modeloTabla.getValueAt(row, 0);
                    txtNombre.setText((String) modeloTabla.getValueAt(row, 1));
                }
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- SUR: campo + botones ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBorder(BorderFactory.createTitledBorder("Datos del repartidor"));

        JPanel panelCampo = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCampo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        panelCampo.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampo.add(txtNombre);
        panelSur.add(panelCampo, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnRegistrar = new JButton("Registrar Nuevo");
        JButton btnGuardar = new JButton("Guardar Cambios");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        for (JButton btn : new JButton[]{btnRegistrar, btnGuardar, btnEliminar, btnRefrescar}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 12));
            panelBotones.add(btn);
        }
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);

        // --- Listeners ---
        btnRegistrar.addActionListener(e -> registrarNuevo());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarRepartidor());
        btnRefrescar.addActionListener(e -> cargarRepartidores());

        cargarRepartidores();
    }

    /**
     * Carga todos los repartidores en la tabla.
     */
    private void cargarRepartidores() {
        modeloTabla.setRowCount(0);
        idSeleccionado = -1;

        try {
            List<Repartidor> lista = repartidorDAO.listarTodos();
            for (Repartidor r : lista) {
                modeloTabla.addRow(new Object[]{r.getId(), r.getNombre()});
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar repartidores:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registra un nuevo repartidor con el nombre indicado en el campo.
     */
    private void registrarNuevo() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            repartidorDAO.guardar(new Repartidor(nombre));
            JOptionPane.showMessageDialog(this,
                    "Repartidor \"" + nombre + "\" registrado correctamente.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            txtNombre.setText("");
            tabla.clearSelection();
            idSeleccionado = -1;
            cargarRepartidores();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar el repartidor:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza el nombre del repartidor seleccionado.
     */
    private void guardarCambios() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un repartidor de la tabla para editarlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            repartidorDAO.actualizar(new Repartidor(idSeleccionado, nombre));
            JOptionPane.showMessageDialog(this,
                    "Repartidor actualizado correctamente.",
                    "Actualización", JOptionPane.INFORMATION_MESSAGE);
            cargarRepartidores();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar el repartidor:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina el repartidor seleccionado con confirmación previa.
     */
    private void eliminarRepartidor() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un repartidor de la tabla para eliminarlo.",
                    "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al repartidor #" + idSeleccionado + "?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf == JOptionPane.YES_OPTION) {
            try {
                repartidorDAO.eliminar(idSeleccionado);
                JOptionPane.showMessageDialog(this,
                        "Repartidor eliminado correctamente.",
                        "Eliminación", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.setText("");
                idSeleccionado = -1;
                cargarRepartidores();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar este repartidor.\n"
                                + "Primero elimina las entregas que tenga asignadas.",
                        "Error al eliminar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}