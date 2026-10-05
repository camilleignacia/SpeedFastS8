package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal con navegación hacia las demás ventanas.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        inicializarVentana();
    }

    private void inicializarVentana() {
        setTitle("SpeedFast — Sistema de Gestión de Entregas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
        panelTitulo.setBackground(new Color(153, 53, 86));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitulo = new JLabel("SPEEDFAST", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 29));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Entregas", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(244, 192, 209));

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 60, 25, 60));

        JButton btnRegistrar = new JButton("Registrar Pedido");
        JButton btnAsignar = new JButton("Asignar Repartidor — Iniciar Entrega");
        JButton btnPedidos = new JButton("Gestión de Pedidos");
        JButton btnEntregas = new JButton("Gestionar Entregas");
        JButton btnRepartidores = new JButton("Gestionar Repartidores");

        for (JButton btn : new JButton[]{btnRepartidores, btnAsignar, btnRegistrar, btnPedidos, btnEntregas}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
        }

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnAsignar);
        panelBotones.add(btnPedidos);
        panelBotones.add(btnEntregas);
        panelBotones.add(btnRepartidores);

        add(panelBotones, BorderLayout.CENTER);

        btnRepartidores.addActionListener(e -> new VentanaRepartidores().setVisible(true));
        btnRegistrar.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnPedidos.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnEntregas.addActionListener(e -> new VentanaEntregas().setVisible(true));
        btnAsignar.addActionListener(e -> new VentanaAsignacion().setVisible(true));
    }
}