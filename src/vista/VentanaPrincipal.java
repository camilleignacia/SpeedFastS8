package vista;

import javax.swing.*;
import java.awt.*;

/** Ventana principal con navegación hacia las demás ventanas. */
public class VentanaPrincipal extends JFrame {

    /** Crea la ventana principal. */
    public VentanaPrincipal() {
        inicializarVentana();
    }

    /** Configura el diseño y los componentes de la ventana principal. */
    private void inicializarVentana() {
        setTitle("SpeedFast - Sistema de Gestión de Entregas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));

        JLabel lblTitulo = new JLabel("SPEEDFAST", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(18, 10, 2, 10));

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Entregas", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitulo.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 60, 25, 60));

        JButton btnRegistrar = new JButton("Registrar Pedido");
        JButton btnListar    = new JButton("Listar Pedidos");
        JButton btnAsignar   = new JButton("Asignar Repartidor / Iniciar Entrega");

        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnListar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnAsignar.setFont(new Font("Arial", Font.PLAIN, 11));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnAsignar);
        add(panelBotones, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> new VentanaRegistroPedido().setVisible(true));
        btnListar.addActionListener(e -> new VentanaListaPedidos().setVisible(true));
        btnAsignar.addActionListener(e -> new VentanaAsignacion().setVisible(true));
    }
}