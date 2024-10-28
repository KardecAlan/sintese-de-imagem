package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PainelDesenho extends JPanel {
    private static final int PIXEL_SIZE = 20; // Tamanho de cada célula em pixels
    private int larguraPixels = 50; // Largura em número de pixels (ajustável pelo usuário)
    private int alturaPixels = 50;  // Altura em número de pixels (ajustável pelo usuário)
    private String selectedAlgorithm = "";
    private ArrayList<Ponto> pontosDesenho = new ArrayList<>();
    private final ArrayList<Ponto> pontosEntrada = new ArrayList<>();
    private final ArrayList<Ponto> polilinhaPontos = new ArrayList<>();
    private final ArrayList<Ponto[]> linhasDesenhadas = new ArrayList<>();
    private final JTextArea coordenadasArea;
    private BufferedImage canvas;
    private int clickCount = 0;
    private int x1, y1, x2, y2, x3, y3, x4, y4;
    private int bezierDegree = 3; // Grau padrão para curvas de Bézier
    private Ponto recorteCanto1, recorteCanto2;
    private boolean definindoRecorte = false; // Controla se estamos definindo a janela de recorte



    public PainelDesenho(JTextArea coordenadasArea) {
        this.coordenadasArea = coordenadasArea;
        inicializarCanvas();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int gridX = e.getX() / PIXEL_SIZE;
                int gridY = e.getY() / PIXEL_SIZE;

                if (definindoRecorte) {
                    if (clickCount == 0) {
                        recorteCanto1 = new Ponto(gridX, gridY);
                        clickCount++;
                    } else if (clickCount == 1) {
                        recorteCanto2 = new Ponto(gridX, gridY);
                        clickCount = 0;
                        definindoRecorte = false;
                        aplicarRecorte();
                    }
                } else {
                    switch (selectedAlgorithm) {
                        case "Bresenham" -> handleBresenhamClick(e);
                        case "Circulo" -> handleCirculoClick(e);
                        case "Curvas" -> handleCurvasClick(e);
                        case "Polilinhas" -> handlePolilinhasClick(e);
                        case "Recursivo" -> handlePreenchimentoRecursivoClick(e);
                        case "Varredura" -> handlePreenchimentoVarreduraClick(e);
                    }
                }
                repaint();
            }
        });
    }

    public void setBezierDegree(int degree) {
        this.bezierDegree = degree;
    }

    // Inicializa o canvas para o tamanho da grade
    private void inicializarCanvas() {
        if (canvas == null) {
            canvas = new BufferedImage(larguraPixels, alturaPixels, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = canvas.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, larguraPixels, alturaPixels);
            g2d.dispose();
        }
    }

    public void setAlgorithm(String algorithm) {
        this.selectedAlgorithm = algorithm;
        coordenadasArea.setText("");

        if ("Recorte".equals(selectedAlgorithm)) {
            definindoRecorte = true;
            clickCount = 0;
            JOptionPane.showMessageDialog(this, "Clique para definir os pontos da área de recorte.");
        } else {
            definindoRecorte = false;
        }
    }

    public void clearScreen() {
        inicializarCanvas();
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, larguraPixels, alturaPixels);
        g2d.dispose();
        pontosDesenho.clear();
        pontosEntrada.clear();
        polilinhaPontos.clear();
        linhasDesenhadas.clear();
        recorteCanto1 = null;
        recorteCanto2 = null;
        coordenadasArea.setText("");
        repaint();
    }

    private void aplicarRecorte() {
        int xMin = Math.min(recorteCanto1.getX(), recorteCanto2.getX());
        int yMin = Math.min(recorteCanto1.getY(), recorteCanto2.getY());
        int xMax = Math.max(recorteCanto1.getX(), recorteCanto2.getX());
        int yMax = Math.max(recorteCanto1.getY(), recorteCanto2.getY());

        RecorteDeLinha recorte = new RecorteDeLinha(xMin, yMin, xMax, yMax);
        ArrayList<Ponto> pontosRecortados = new ArrayList<>();

        for (Ponto[] linha : linhasDesenhadas) {
            Bresenham bresenham = new Bresenham(linha[0], linha[1]);
            ArrayList<Ponto> pontosLinha = bresenham.getPontos();

            for (Ponto ponto : pontosLinha) {
                if (recorte.estaDentro(ponto)) {  // Verifica se o ponto está dentro da área de recorte
                    pontosRecortados.add(ponto);
                }
            }
        }

        pontosDesenho.clear();
        pontosDesenho.addAll(pontosRecortados);
        updateCoordenadasArea(pontosRecortados);
    }

    private void handleBresenhamClick(MouseEvent e) {
        int gridX = e.getX() / PIXEL_SIZE;
        int gridY = e.getY() / PIXEL_SIZE;

        if (clickCount == 0) {
            recorteCanto1 = new Ponto(gridX, gridY);
            clickCount++;
        } else {
            recorteCanto2 = new Ponto(gridX, gridY);
            clickCount = 0;
            desenharLinha(new Ponto(recorteCanto1.getX(), recorteCanto1.getY()), new Ponto(recorteCanto2.getX(), recorteCanto2.getY()));
        }
    }

    private void desenharLinha(Ponto p1, Ponto p2) {
        Bresenham bresenham = new Bresenham(p1, p2);
        pontosDesenho.addAll(bresenham.getPontos());
        linhasDesenhadas.add(new Ponto[]{p1, p2});
//        updateCoordenadasArea();
    }

    private void handleCirculoClick(MouseEvent e) {
        int gridX = e.getX() / PIXEL_SIZE;
        int gridY = e.getY() / PIXEL_SIZE;

        pontosEntrada.add(new Ponto(gridX, gridY));

        String inputRaio = JOptionPane.showInputDialog("Digite o raio do círculo:");
        if (inputRaio != null) {
            try {
                int raio = Integer.parseInt(inputRaio);
                executeCirculo(raio, new Ponto(gridX, gridY));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido para o raio.");
            }
        }
        repaint();
    }

    private void handleCurvasClick(MouseEvent e) {
        int gridX = e.getX() / PIXEL_SIZE;
        int gridY = e.getY() / PIXEL_SIZE;

        if (bezierDegree == 2) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                pontosEntrada.add(new Ponto(x1, y1));
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                pontosEntrada.add(new Ponto(x2, y2));
                clickCount++;
            } else {
                x3 = gridX;
                y3 = gridY;
                pontosEntrada.add(new Ponto(x3, y3));
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), null, new Ponto(x3, y3));
            }
        } else if (bezierDegree == 3) {
            if (clickCount == 0) {
                x1 = gridX;
                y1 = gridY;
                pontosEntrada.add(new Ponto(x1, y1));
                clickCount++;
            } else if (clickCount == 1) {
                x2 = gridX;
                y2 = gridY;
                pontosEntrada.add(new Ponto(x2, y2));
                clickCount++;
            } else if (clickCount == 2) {
                x3 = gridX;
                y3 = gridY;
                pontosEntrada.add(new Ponto(x3, y3));
                clickCount++;
            } else {
                x4 = gridX;
                y4 = gridY;
                pontosEntrada.add(new Ponto(x4, y4));
                clickCount = 0;
                executeCurvas(new Ponto(x1, y1), new Ponto(x2, y2), new Ponto(x3, y3), new Ponto(x4, y4));
            }
        }
        repaint();
    }

    private void handlePolilinhasClick(MouseEvent e) {
        int gridX = e.getX() / PIXEL_SIZE;
        int gridY = e.getY() / PIXEL_SIZE;

        polilinhaPontos.add(new Ponto(gridX, gridY));
        pontosEntrada.add(new Ponto(gridX, gridY));

        if (SwingUtilities.isRightMouseButton(e) && polilinhaPontos.size() >= 3) {
            executePolilinhas(polilinhaPontos);
            polilinhaPontos.clear();
        }
        repaint();
    }

    private void handlePreenchimentoRecursivoClick(MouseEvent e) {
        int x = e.getX() / PIXEL_SIZE;
        int y = e.getY() / PIXEL_SIZE;

        Color borderColor = Color.RED; // Supomos que as bordas estão em vermelho
        Color fillColor = Color.YELLOW;

        PreenchimentoRecursivo preenchimento = new PreenchimentoRecursivo(new Ponto(x, y), pontosDesenho, larguraPixels, borderColor, fillColor);
        pontosDesenho.addAll(preenchimento.listapontos);
        repaint();
    }

    private void handlePreenchimentoVarreduraClick(MouseEvent e) {
        int x = e.getX() / PIXEL_SIZE;
        int y = e.getY() / PIXEL_SIZE;

        PreenchimentoVarredura varredura = new PreenchimentoVarredura(pontosDesenho, larguraPixels, Color.RED);
        pontosDesenho.addAll(varredura.listapontos);
        repaint();
    }


    private void executeCirculo(int raio, Ponto centro) {
        Circulo circulo = new Circulo(raio, centro);
        pontosDesenho = circulo.getPontos();
    }

    private void executeCurvas(Ponto pInicial, Ponto controle1, Ponto controle2, Ponto pFinal) {
        Curvas curvas = new Curvas(pInicial, controle1, controle2, pFinal);
        pontosDesenho = curvas.getPontos();
    }

    private void executePolilinhas(ArrayList<Ponto> pontos) {
        Polilinhas polilinhas = new Polilinhas(pontos);
        pontosDesenho.clear();
        pontosDesenho.addAll(polilinhas.getPontos());
        updateCoordenadasArea(pontosDesenho);
    }

    private void updateCoordenadasArea(ArrayList<Ponto> pontosDesenho) {
        StringBuilder sb = new StringBuilder();
        for (Ponto ponto : pontosDesenho) {
            sb.append("(").append(ponto.getX()).append(", ").append(ponto.getY()).append(")\n");
        }
        coordenadasArea.setText(sb.toString());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        inicializarCanvas(); // Garante que o canvas está inicializado
        g.drawImage(canvas, 0, 0, larguraPixels * PIXEL_SIZE, alturaPixels * PIXEL_SIZE, null);

        Graphics2D g2d = (Graphics2D) g;

        // Desenhar grade
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= larguraPixels; i++) {
            for (int j = 0; j <= alturaPixels; j++) {
                int x = i * PIXEL_SIZE;
                int y = j * PIXEL_SIZE;
                g2d.drawRect(x, y, PIXEL_SIZE, PIXEL_SIZE);
            }
        }

        // Desenhar pontos de entrada
        g2d.setColor(Color.BLUE);
        for (Ponto ponto : pontosEntrada) {
            int screenX = ponto.getX() * PIXEL_SIZE;
            int screenY = ponto.getY() * PIXEL_SIZE;
            g2d.fillOval(screenX, screenY, PIXEL_SIZE, PIXEL_SIZE);
        }

        // Desenhar pontos do desenho final
        g2d.setColor(Color.RED);
        for (Ponto ponto : pontosDesenho) {
            int screenX = ponto.getX() * PIXEL_SIZE;
            int screenY = ponto.getY() * PIXEL_SIZE;
            g2d.fillRect(screenX, screenY, PIXEL_SIZE, PIXEL_SIZE);
        }

        // Desenha pontos da área de recorte
        if (recorteCanto1 != null) {
            g2d.setColor(Color.blue);
            g2d.fillRect(recorteCanto1.getX() * PIXEL_SIZE, recorteCanto1.getY() * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
        }
        if (recorteCanto2 != null) {
            g2d.setColor(Color.blue);
            g2d.fillRect(recorteCanto2.getX() * PIXEL_SIZE, recorteCanto2.getY() * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
        }

        // Desenhar linhas recortadas
        g2d.setColor(Color.RED);
        for (Ponto[] linha : linhasDesenhadas) {
            int x1 = linha[0].getX() * PIXEL_SIZE;
            int y1 = linha[0].getY() * PIXEL_SIZE;
            int x2 = linha[1].getX() * PIXEL_SIZE;
            int y2 = linha[1].getY() * PIXEL_SIZE;
            g2d.drawLine(x1, y1, x2, y2);
        }
        // Desenha pontos do desenho como pixels em vez de linhas
        for (Ponto ponto : pontosDesenho) {
            int screenX = ponto.getX() * PIXEL_SIZE;
            int screenY = ponto.getY() * PIXEL_SIZE;
            g2d.fillRect(screenX, screenY, PIXEL_SIZE, PIXEL_SIZE);
        }

    }

}
