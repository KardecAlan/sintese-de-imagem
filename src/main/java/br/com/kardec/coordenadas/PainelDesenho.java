package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PainelDesenho extends JPanel {
    private int x1, y1, x2, y2, x3, y3, x4, y4;
    private int clickCount = 0;
    private int bezierDegree = 3; // Grau padrão 3
    private int tamanhoCoordenadas = 11; // Valor padrão para coordenadas (-11 a 11)
    private String selectedAlgorithm = "";
    private ArrayList<Ponto> pontosDesenho = new ArrayList<>();
    private ArrayList<Ponto> pontosEntrada = new ArrayList<>(); // Armazenar pontos de entrada do usuário
    private ArrayList<Ponto> polilinhaPontos = new ArrayList<>();
    private JTextArea coordenadasArea;

    public PainelDesenho(JTextArea coordenadasArea) {
        this.coordenadasArea = coordenadasArea;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedAlgorithm.equals("Bresenham")) {
                    handleBresenhamClick(e);
                } else if (selectedAlgorithm.equals("Circulo")) {
                    handleCirculoClick(e);
                } else if (selectedAlgorithm.equals("Curvas")) {
                    handleCurvasClick(e);
                } else if (selectedAlgorithm.equals("Polilinhas")) {
                    handlePolilinhasClick(e);
                }
            }
        });
    }

    public void setAlgorithm(String algorithm) {
        this.selectedAlgorithm = algorithm;
        this.clickCount = 0; // Resetar o contador de cliques ao mudar o algoritmo
        pontosEntrada.clear(); // Limpar pontos de entrada quando mudar o algoritmo
        polilinhaPontos.clear(); // Limpar pontos da polilinha
        coordenadasArea.setText(""); // Limpar coordenadas quando mudar o algoritmo
    }

    public void setBezierDegree(int degree) {
        this.bezierDegree = degree;
    }

    public void setTamanhoCoordenadas(int tamanho) {
        this.tamanhoCoordenadas = tamanho;
        revalidate(); // Forçar atualização da interface
        repaint();    // Redesenhar com o novo tamanho
    }

    public void clearScreen() {
        pontosDesenho.clear();
        pontosEntrada.clear(); // Limpar pontos de entrada
        polilinhaPontos.clear(); // Limpar pontos da polilinha
        coordenadasArea.setText(""); // Limpar a área de texto
        repaint();
    }

    private int converterParaCoordenadaCartesiana(int valorPixel, int tamanhoPainel, int tamanhoCoordenadas) {
        int centro = tamanhoPainel / 2;
        return (valorPixel - centro) / (tamanhoPainel / (tamanhoCoordenadas * 2));
    }

    private void handlePolilinhasClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int gridX = converterParaCoordenadaCartesiana(mouseX, getWidth(), tamanhoCoordenadas);
        int gridY = converterParaCoordenadaCartesiana(getHeight() - mouseY, getHeight(), tamanhoCoordenadas);

        // Adicionar o ponto clicado para a polilinha
        polilinhaPontos.add(new Ponto(gridX, gridY));
        pontosEntrada.add(new Ponto(gridX, gridY)); // Adicionar ponto de entrada para destacar

        // Verificar se o botão direito foi pressionado para finalizar a polilinha
        if (SwingUtilities.isRightMouseButton(e)) {
            if (polilinhaPontos.size() >= 4) { // Garantir que haja pelo menos 4 pontos
                executePolilinhas(polilinhaPontos);
                polilinhaPontos.clear();
                pontosEntrada.clear();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecione pelo menos 4 pontos para formar uma polilinha.");
            }
        }

        repaint(); // Atualizar para destacar os pontos de entrada
    }



    private void handleBresenhamClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int gridX = converterParaCoordenadaCartesiana(mouseX, getWidth(), tamanhoCoordenadas);
        int gridY = converterParaCoordenadaCartesiana(getHeight() - mouseY, getHeight(), tamanhoCoordenadas);

        if (clickCount == 0) {
            x1 = gridX;
            y1 = gridY;
            pontosEntrada.add(new Ponto(x1, y1)); // Adicionar ponto de entrada
            clickCount++;
        } else {
            x2 = gridX;
            y2 = gridY;
            pontosEntrada.add(new Ponto(x2, y2)); // Adicionar ponto de entrada
            clickCount = 0;
            executeBresenham(new Ponto(x1, y1), new Ponto(x2, y2));
        }
        repaint(); // Atualizar para destacar os pontos de entrada
    }

    private void handleCirculoClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int gridX = converterParaCoordenadaCartesiana(mouseX, getWidth(), tamanhoCoordenadas);
        int gridY = converterParaCoordenadaCartesiana(getHeight() - mouseY, getHeight(), tamanhoCoordenadas);

        pontosEntrada.add(new Ponto(gridX, gridY)); // Adicionar ponto de entrada para o centro do círculo

        String inputRaio = JOptionPane.showInputDialog("Digite o raio do círculo:");
        if (inputRaio != null) {
            try {
                int raio = Integer.parseInt(inputRaio);
                executeCirculo(raio, new Ponto(gridX, gridY));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido para o raio.");
            }
        }
        repaint(); // Atualizar para destacar os pontos de entrada
    }

    private void handleCurvasClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int gridX = converterParaCoordenadaCartesiana(mouseX, getWidth(), tamanhoCoordenadas);
        int gridY = converterParaCoordenadaCartesiana(getHeight() - mouseY, getHeight(), tamanhoCoordenadas);

        if (bezierDegree == 2) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                pontosEntrada.add(new Ponto(x1, y1)); // Adicionar ponto de entrada
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                pontosEntrada.add(new Ponto(x2, y2)); // Adicionar ponto de entrada
                clickCount++;
            } else {
                x3 = gridX;
                y3 = gridY;
                pontosEntrada.add(new Ponto(x3, y3)); // Adicionar ponto de entrada
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), null, new Ponto(x3, y3));
            }
        } else if (bezierDegree == 3) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                pontosEntrada.add(new Ponto(x1, y1)); // Adicionar ponto de entrada
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                pontosEntrada.add(new Ponto(x2, y2)); // Adicionar ponto de entrada
                clickCount++;
            } else if (clickCount == 2) {
                x3 = gridX;
                y3 = gridY;
                pontosEntrada.add(new Ponto(x3, y3)); // Adicionar ponto de entrada
                clickCount++;
            } else {
                x4 = gridX;
                y4 = gridY;
                pontosEntrada.add(new Ponto(x4, y4)); // Adicionar ponto de entrada
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), new Ponto(x3, y3), new Ponto(x4, y4));
            }
        }
        repaint(); // Atualizar para destacar os pontos de entrada
    }

    private void executeBresenham(Ponto p1, Ponto p2) {
        Bresenham bresenham = new Bresenham(p1, p2);
        pontosDesenho = bresenham.getPontos();
        updateCoordenadasArea(pontosDesenho);
        repaint();
    }

    private void executeCirculo(int raio, Ponto centro) {
        Circulo circulo = new Circulo(raio, centro);
        pontosDesenho = circulo.getPontos();
        updateCoordenadasArea(pontosDesenho);
        repaint();
    }

    private void executeCurvas(Ponto pInicial, Ponto controle1, Ponto controle2, Ponto pFinal) {
        Curvas curvas = new Curvas(pInicial, controle1, controle2, pFinal);
        pontosDesenho = curvas.getPontos();
        updateCoordenadasArea(pontosDesenho);
        repaint();
    }

    private void executePolilinhas(ArrayList<Ponto> pontos) {
        Polilinhas polilinhas = new Polilinhas(pontos);
        pontosDesenho.clear();  // Limpar a lista antes de adicionar novos pontos
        pontosDesenho.addAll(polilinhas.getPontos());
        updateCoordenadasArea(pontosDesenho);
        repaint();
    }



    private void updateCoordenadasArea(ArrayList<Ponto> pontos) {
        StringBuilder sb = new StringBuilder();
        for (Ponto ponto : pontos) {
            sb.append("(").append(ponto.getX()).append(", ").append(ponto.getY()).append(")\n");
        }
        coordenadasArea.setText(sb.toString());
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
        for (int i = -tamanhoCoordenadas; i <= tamanhoCoordenadas; i++) {
            int x = width / 2 + i * (width / (tamanhoCoordenadas * 2));
            int y = height / 2 - i * (height / (tamanhoCoordenadas * 2));
            g2d.drawLine(x, 0, x, height); // Linhas verticais
            g2d.drawLine(0, y, width, y); // Linhas horizontais
        }

        g2d.setColor(Color.BLACK);
        g2d.drawLine(width / 2, 0, width / 2, height);
        g2d.drawLine(0, height / 2, width, height / 2);

        // Desenhar pontos destacados de entrada
        g2d.setColor(Color.BLUE);
        for (Ponto ponto : pontosEntrada) {
            int screenX = width / 2 + ponto.getX() * (width / (tamanhoCoordenadas * 2));
            int screenY = height / 2 - ponto.getY() * (height / (tamanhoCoordenadas * 2));
            g2d.fillOval(screenX - 5, screenY - 5, 10, 10); // Desenhar ponto de entrada como um círculo maior
        }

        // Desenhar pontos do desenho final
        g2d.setColor(Color.RED);
        for (Ponto ponto : pontosDesenho) {
            int screenX = width / 2 + ponto.getX() * (width / (tamanhoCoordenadas * 2));
            int screenY = height / 2 - ponto.getY() * (height / (tamanhoCoordenadas * 2));
            g2d.fillRect(screenX - 2, screenY - 2, 5, 5);
        }
    }
}
