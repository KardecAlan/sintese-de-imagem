package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.Bresenham;
import br.com.kardec.algoritmos.Circulo;
import br.com.kardec.algoritmos.Ponto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PainelDesenho extends JPanel {
    private int x1, y1, x2, y2;
    private boolean isFirstClick = true;
    private String selectedAlgorithm = "";
    private ArrayList<Ponto> pontosDesenho = new ArrayList<>();

    public PainelDesenho() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedAlgorithm.equals("Bresenham")) {
                    handleBresenhamClick(e);
                } else if (selectedAlgorithm.equals("Circulo")) {
                    handleCirculoClick(e);
                }
            }
        });
    }

    public void setAlgorithm(String algorithm) {
        this.selectedAlgorithm = algorithm;
    }

    public void clearScreen() {
        pontosDesenho.clear();
        repaint();
    }

    private void handleBresenhamClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Converter coordenadas de tela para coordenadas do sistema cartesiano centrado
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int gridX = (mouseX - centerX) / (getWidth() / 22);
        int gridY = (centerY - mouseY) / (getHeight() / 22);

        if (isFirstClick) {
            // Primeiro clique define o ponto A
            x1 = gridX;
            y1 = gridY;
            isFirstClick = false;
        } else {
            // Segundo clique define o ponto B e desenha a linha usando Bresenham
            x2 = gridX;
            y2 = gridY;
            isFirstClick = true;
            executeBresenham(new Ponto(x1, y1), new Ponto(x2, y2));
        }
    }

    private void handleCirculoClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        // Converter coordenadas de tela para coordenadas do sistema cartesiano centrado
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int gridX = (mouseX - centerX) / (getWidth() / 22);
        int gridY = (centerY - mouseY) / (getHeight() / 22);

        // Solicitar raio ao usuário
        String inputRaio = JOptionPane.showInputDialog("Digite o raio do círculo:");
        if (inputRaio != null) {
            try {
                int raio = Integer.parseInt(inputRaio);
                executeCirculo(raio, new Ponto(gridX, gridY));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido para o raio.");
            }
        }
    }

    private void executeBresenham(Ponto p1, Ponto p2) {
        Bresenham bresenham = new Bresenham(p1, p2);
        pontosDesenho = bresenham.getPontos();
        repaint();
    }

    private void executeCirculo(int raio, Ponto centro) {
        Circulo circulo = new Circulo(raio, centro);
        pontosDesenho = circulo.getPontos();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Configurar sistema de coordenadas
        int width = getWidth();
        int height = getHeight();

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        // Desenhar linhas de grade
        g2d.setColor(Color.GREEN);
        for (int i = -11; i <= 11; i++) {
            int x = width / 2 + i * (width / 22);
            int y = height / 2 - i * (height / 22);
            g2d.drawLine(x, 0, x, height); // Linhas verticais
            g2d.drawLine(0, y, width, y); // Linhas horizontais
        }

        // Desenhar eixos
        g2d.setColor(Color.BLACK);
        g2d.drawLine(width / 2, 0, width / 2, height); // Eixo Y
        g2d.drawLine(0, height / 2, width, height / 2); // Eixo X

        // Desenhar pontos da linha de Bresenham ou círculo
        g2d.setColor(Color.RED);
        for (Ponto ponto : pontosDesenho) {
            int screenX = getWidth() / 2 + ponto.getX() * (getWidth() / 22);
            int screenY = getHeight() / 2 - ponto.getY() * (getHeight() / 22);
            g2d.fillRect(screenX - 2, screenY - 2, 5, 5);
        }
    }

}
