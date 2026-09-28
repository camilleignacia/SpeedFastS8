package vista;

import dao.PedidoDAO;
import modelo.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Muestra los pedidos registrados en una JTable. */
public class VentanaListaPedidos extends JFrame {

    private PedidoDAO         pedidoDAO;
    private DefaultTableModel modeloTabla;

    public VentanaListaPedidos() {
        this.pedidoDAO = new PedidoDAO();
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast - Lista de Pedidos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Pedidos Registrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"ID", "Dirección", "Tipo", "Estado", "Repartidor"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        JButton btnRefrescar = new JButton("Refrescar tabla");
        btnRefrescar.setFont(new Font("Arial", Font.PLAIN, 13));
        panelBoton.add(btnRefrescar);
        add(panelBoton, BorderLayout.SOUTH);

        btnRefrescar.addActionListener(e -> cargarPedidos());

        cargarPedidos();
    }

    /** Vacía la tabla y la rellena con los pedidos desde la BD. */
    private void cargarPedidos() {
        modeloTabla.setRowCount(0);
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        for (Pedido p : pedidos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getDireccionEntrega(),
                    p.getTipo(),
                    p.getEstado(),
                    p.getRepartidor()
            });
        }
    }
}