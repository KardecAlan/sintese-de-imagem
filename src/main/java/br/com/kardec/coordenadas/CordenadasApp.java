package br.com.kardec.coordenadas;

import javax.swing.*;
import java.awt.*;

public class CordenadasApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Coordenadas - Desenhos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);


        PainelDesenho painelDesenho = new PainelDesenho();


        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));


        JButton bresenhamButton = new JButton("Bresenham");
        bresenhamButton.addActionListener(e -> painelDesenho.setAlgorithm("Bresenham"));


        JButton circuloButton = new JButton("Circulo");
        circuloButton.addActionListener(e -> painelDesenho.setAlgorithm("Circulo"));


        JButton curvasButton = new JButton("Curvas");
        curvasButton.addActionListener(e -> {
            String[] options = {"Grau 2", "Grau 3"};
            int response = JOptionPane.showOptionDialog(null, "Escolha o tipo de curva Bezier:",
                    "Curva Bezier", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            painelDesenho.setBezierDegree(response == 0 ? 2 : 3);
            painelDesenho.setAlgorithm("Curvas");
        });


        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> painelDesenho.clearScreen());

        controlPanel.add(bresenhamButton);
        controlPanel.add(circuloButton);
        controlPanel.add(curvasButton);
        controlPanel.add(clearButton);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelDesenho, controlPanel);
        splitPane.setDividerLocation(600);
        frame.add(splitPane);
        frame.setVisible(true);
    }

}
