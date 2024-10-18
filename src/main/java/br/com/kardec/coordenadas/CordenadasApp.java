package br.com.kardec.coordenadas;

import javax.swing.*;
import java.awt.*;

public class CordenadasApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Coordenadas - Desenhos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Painel de desenho
        PainelDesenho painelDesenho = new PainelDesenho();
        frame.add(painelDesenho, BorderLayout.CENTER);

        // Painel de controle
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Botão para selecionar Bresenham
        JButton bresenhamButton = new JButton("Bresenham");
        bresenhamButton.addActionListener(e -> painelDesenho.setAlgorithm("Bresenham"));

        // Botão para selecionar Circulo
        JButton circuloButton = new JButton("Circulo");
        circuloButton.addActionListener(e -> painelDesenho.setAlgorithm("Circulo"));

        // Botão para limpar a tela
        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> painelDesenho.clearScreen());

        controlPanel.add(bresenhamButton);
        controlPanel.add(circuloButton);
        controlPanel.add(clearButton);

        frame.add(controlPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }
}
