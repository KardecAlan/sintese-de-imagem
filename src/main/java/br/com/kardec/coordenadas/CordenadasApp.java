package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.Ponto;

import javax.swing.*;
import java.awt.*;

public class CordenadasApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Coordenadas - Desenhos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // Painel de desenho
        JTextArea coordenadasArea = new JTextArea();
        coordenadasArea.setEditable(false);
        PainelDesenho painelDesenho = new PainelDesenho(coordenadasArea);

        // Painel de controle
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 600));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Botões para os algoritmos de desenho
        JButton bresenhamButton = new JButton("Bresenham");
        bresenhamButton.addActionListener(e -> painelDesenho.setAlgorithm("Bresenham"));

        JButton circuloButton = new JButton("Círculo");
        circuloButton.addActionListener(e -> painelDesenho.setAlgorithm("Circulo"));

        JButton elipseButton = new JButton("Elipse"); // Botão para Elipse
        elipseButton.addActionListener(e -> painelDesenho.setAlgorithm("Elipse"));

        JButton curvasButton = new JButton("Curvas de Bézier");
        curvasButton.addActionListener(e -> {
            String[] options = {"Grau 2", "Grau 3"};
            int response = JOptionPane.showOptionDialog(null, "Escolha o grau da curva Bézier:",
                    "Curva Bézier", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            painelDesenho.setBezierDegree(response == 0 ? 2 : 3);
            painelDesenho.setAlgorithm("Curvas");
        });

        JButton polilinhasButton = new JButton("Polilinhas");
        polilinhasButton.addActionListener(e -> painelDesenho.setAlgorithm("Polilinhas"));

        JButton recursivoButton = new JButton("Preenchimento Recursivo");
        recursivoButton.addActionListener(e -> painelDesenho.setAlgorithm("Recursivo"));

        JButton varreduraButton = new JButton("Preenchimento Varredura");
        varreduraButton.addActionListener(e -> painelDesenho.setAlgorithm("Varredura"));

        // **Botão para selecionar Recorte de Linha**
        JButton recorteButton = new JButton("Recorte de Linha");
        recorteButton.addActionListener(e -> painelDesenho.setAlgorithm("Recorte"));

        JButton recortePoligonoButton = new JButton("Recorte de Polígono");
        recortePoligonoButton.addActionListener(e -> painelDesenho.setAlgorithm("RecortePoligono"));

        JButton translacaoButton = new JButton("Translação");
        translacaoButton.addActionListener(e -> painelDesenho.setAlgorithm("Translacao"));

        JButton rotacaoButton = new JButton("Rotação");
        rotacaoButton.addActionListener(e -> painelDesenho.setAlgorithm("Rotacao"));

        JButton escalaButton = new JButton("Escala");
        escalaButton.addActionListener(e -> painelDesenho.setAlgorithm("Escala"));


        // Botão para definir pontos de um sólido 3D
        JButton definirSolidoButton = new JButton("Definir Sólido 3D");
        definirSolidoButton.addActionListener(e -> painelDesenho.definirPontosSolido3D());

        // Botão para projeção ortográfica
        JButton projecaoOrtograficaButton = new JButton("Projeção Ortográfica");
        projecaoOrtograficaButton.addActionListener(e -> painelDesenho.aplicarProjecaoOrtografica());

        // Botão para projeção oblíqua
        JButton projecaoObliquaButton = new JButton("Projeção Oblíqua");
        projecaoObliquaButton.addActionListener(e -> painelDesenho.aplicarProjecaoObliqua());

        // Botão para projeção perspectiva
        JButton projecaoPerspectivaButton = new JButton("Projeção Perspectiva");
        projecaoPerspectivaButton.addActionListener(e -> painelDesenho.aplicarProjecaoPerspectiva());

        // Botão para limpar a tela
        JButton clearButton = new JButton("Limpar Tela");
        clearButton.addActionListener(e -> painelDesenho.clearScreen());

        // Adicionar todos os botões ao painel de controle
        controlPanel.add(bresenhamButton);
        controlPanel.add(circuloButton);
        controlPanel.add(elipseButton); // Adiciona botão de Elipse
        controlPanel.add(curvasButton);
        controlPanel.add(polilinhasButton);
        controlPanel.add(recursivoButton);
        controlPanel.add(varreduraButton);
        controlPanel.add(recorteButton); // Novo botão de recorte
        controlPanel.add(recortePoligonoButton);
        controlPanel.add(translacaoButton);
        controlPanel.add(rotacaoButton);
        controlPanel.add(escalaButton);
        controlPanel.add(definirSolidoButton);
        controlPanel.add(projecaoOrtograficaButton);
        controlPanel.add(projecaoObliquaButton);
        controlPanel.add(projecaoPerspectivaButton);
        controlPanel.add(clearButton);

        // Área de texto para exibir coordenadas dos pontos
        controlPanel.add(new JLabel("Coordenadas dos Pontos:"));
        JScrollPane scrollPane = new JScrollPane(coordenadasArea);
        scrollPane.setPreferredSize(new Dimension(180, 200));
        controlPanel.add(scrollPane);

        // Configuração do layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelDesenho, controlPanel);
        splitPane.setDividerLocation(600);

        frame.add(splitPane);
        frame.setVisible(true);
    }
}
