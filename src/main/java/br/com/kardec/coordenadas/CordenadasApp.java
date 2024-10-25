package br.com.kardec.coordenadas;

import javax.swing.*;
import java.awt.*;

public class CordenadasApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Coordenadas - Desenhos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Painel de desenho
        JTextArea coordenadasArea = new JTextArea();
        coordenadasArea.setEditable(false);
        PainelDesenho painelDesenho = new PainelDesenho(coordenadasArea);

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

        // Botão para selecionar Curvas
        JButton curvasButton = new JButton("Curvas");
        curvasButton.addActionListener(e -> {
            String[] options = {"Grau 2", "Grau 3"};
            int response = JOptionPane.showOptionDialog(null, "Escolha o tipo de curva Bezier:",
                    "Curva Bezier", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            painelDesenho.setBezierDegree(response == 0 ? 2 : 3);
            painelDesenho.setAlgorithm("Curvas");
        });

        // Botão para limpar a tela
        JButton clearButton = new JButton("Limpar");
        clearButton.addActionListener(e -> painelDesenho.clearScreen());

        // Botão para definir o tamanho da tela de coordenadas
        JButton definirTamanhoButton = new JButton("Definir Tamanho da Tela");
        definirTamanhoButton.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Digite o valor máximo das coordenadas (exemplo: 20 para -20 a 20):");
            if (input != null) {
                try {
                    int tamanho = Integer.parseInt(input);
                    if (tamanho > 0) {
                        painelDesenho.setTamanhoCoordenadas(tamanho);
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, insira um número positivo.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um número válido.");
                }
            }
        });

        controlPanel.add(bresenhamButton);
        controlPanel.add(circuloButton);
        controlPanel.add(curvasButton);
        controlPanel.add(clearButton);
        controlPanel.add(definirTamanhoButton);

        // Adicionar a área de texto para exibir coordenadas
        controlPanel.add(new JLabel("Coordenadas dos Pontos:"));
        JScrollPane scrollPane = new JScrollPane(coordenadasArea);
        scrollPane.setPreferredSize(new Dimension(180, 200));
        controlPanel.add(scrollPane);

        // Usar JSplitPane para permitir redimensionamento
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelDesenho, controlPanel);
        splitPane.setDividerLocation(600);
        frame.add(splitPane);
        frame.setVisible(true);
    }
}
