package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.Bresenham;
import br.com.kardec.algoritmos.Circulo;
import br.com.kardec.algoritmos.Curvas;
import br.com.kardec.algoritmos.Ponto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PainelDesenho extends JPanel {
    private int x1, y1, x2, y2, x3, y3, x4, y4;
    private int clickCount = 0;
    private int bezierDegree = 3; // Grau padrão 3
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
                } else if (selectedAlgorithm.equals("Curvas")) {
                    handleCurvasClick(e);
                }
            }
        });
    }

    public void setAlgorithm(String algorithm) {
        this.selectedAlgorithm = algorithm;
        this.clickCount = 0; // Resetar o contador de cliques ao mudar o algoritmo
    }

    public void setBezierDegree(int degree) {
        this.bezierDegree = degree;
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

        if (clickCount == 0) {
            x1 = gridX;
            y1 = gridY;
            clickCount++;
        } else {
            x2 = gridX;
            y2 = gridY;
            clickCount = 0;
            executeBresenham(new Ponto(x1, y1), new Ponto(x2, y2));
        }
    }

    private void handleCirculoClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int gridX = (mouseX - centerX) / (getWidth() / 22);
        int gridY = (centerY - mouseY) / (getHeight() / 22);

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

    private void handleCurvasClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int gridX = (mouseX - centerX) / (getWidth() / 22);
        int gridY = (centerY - mouseY) / (getHeight() / 22);

        if (bezierDegree == 2) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                clickCount++;
            } else {
                x3 = gridX;
                y3 = gridY;
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), null, new Ponto(x3, y3));
            }
        } else if (bezierDegree == 3) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                clickCount++;
            } else if (clickCount == 2) {
                x3 = gridX;
                y3 = gridY;
                clickCount++;
            } else {
                x4 = gridX;
                y4 = gridY;
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), new Ponto(x3, y3), new Ponto(x4, y4));
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

    private void executeCurvas(Ponto pInicial, Ponto controle1, Ponto controle2, Ponto pFinal) {
        Curvas curvas = new Curvas(pInicial, controle1, controle2, pFinal);
        pontosDesenho = curvas.getPontos();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.GREEN);
        for (int i = -11; i <= 11; i++) {
            int x = width / 2 + i * (width / 22);
            int y = height / 2 - i * (height / 22);
            g2d.drawLine(x, 0, x, height); // Linhas verticais
            g2d.drawLine(0, y, width, y); // Linhas horizontais
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(width / 2, 0, width / 2, height);
        g2d.drawLine(0, height / 2, width, height / 2);

        g2d.setColor(Color.RED);
        for (Ponto ponto : pontosDesenho) {
            int screenX = width / 2 + ponto.getX() * (width / 22);
            int screenY = height / 2 - ponto.getY() * (height / 22);
            g2d.fillRect(screenX - 2, screenY - 2, 5, 5);
        }
    }
}
