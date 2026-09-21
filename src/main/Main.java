package main;

import vista.VentanaPrincipal;
import javax.swing.*;

/** Punto de entrada del sistema SpeedFast. */
public class Main {

    public static void main(String[] args) {
        // Inicia la ventana principal en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}