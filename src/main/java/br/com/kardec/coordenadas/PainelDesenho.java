package br.com.kardec.coordenadas;

import br.com.kardec.algoritmos.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PainelDesenho extends JPanel {
    private static final int PIXEL_SIZE = 20; // Tamanho de cada célula em pixels
    private int larguraPixels = 30; // Largura em número de pixels (ajustável pelo usuário)
    private int alturaPixels = 30;  // Altura em número de pixels (ajustável pelo usuário)
    private String selectedAlgorithm = "";
    private ArrayList<Ponto> pontosDesenho = new ArrayList<>();
    private final ArrayList<Ponto> pontosPolilinha = new ArrayList<>();
    private final ArrayList<Ponto> pontosEntrada = new ArrayList<>();
    private final ArrayList<Ponto> polilinhaPontos = new ArrayList<>();
    private final ArrayList<Ponto[]> linhasDesenhadas = new ArrayList<>();
    private ArrayList<Ponto> pontosClicados = new ArrayList<>();
    private final JTextArea coordenadasArea;
    private BufferedImage canvas;
    private int clickCount = 0;
    private int x1, y1, x2, y2, x3, y3, x4, y4;
    private int bezierDegree = 3; // Grau padrão para curvas de Bézier
    private Ponto recorteCanto1, recorteCanto2;
    private boolean definindoRecorte = false; // Controla se estamos definindo a janela de recorte
    private int raioX, raioY;
    private Ponto centroElipse;
    private ArrayList<Ponto3D> pontosSolido3D = new ArrayList<>(); // Para armazenar os vértices 3D
    private ArrayList<Ponto> pontosProjetados = new ArrayList<>(); // Para armazenar os pontos projetados em 2D



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
                        if ("RecortePoligono".equals(selectedAlgorithm)) {
                            aplicarRecortePoligono();
                        } else if ("Recorte".equals(selectedAlgorithm)) {
                            aplicarRecorte();
                        }
                    }
                } else {
                    switch (selectedAlgorithm) {
                        case "Bresenham" -> handleBresenhamClick(e);
                        case "Circulo" -> handleCirculoClick(e);
                        case "Elipse" -> handleElipseClick(gridX, gridY);
                        case "Curvas" -> handleCurvasClick(e);
                        case "Polilinhas" -> handlePolilinhasClick(e);
                        case "Recursivo" -> handlePreenchimentoRecursivoClick(e);
                        case "Varredura" -> handlePreenchimentoVarreduraClick(e);
                        case "Translacao" -> aplicarTranslacao();
                        case "Rotacao" -> aplicarRotacao();
                        case "Escala" -> aplicarEscala();
                    }
                }
                repaint();
            }
        });
    }

    public void setBezierDegree(int degree) {
        this.bezierDegree = degree;
    }

    // Meodo para definir pontos do sólido
    void definirPontosSolido3D() {
        while (true) {
            int x = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada X do ponto 3D (ou cancelar para terminar):"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada Y do ponto 3D:"));
            int z = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada Z do ponto 3D:"));
            pontosSolido3D.add(new Ponto3D(x, y, z));

            int resposta = JOptionPane.showConfirmDialog(this, "Deseja adicionar mais pontos?");
            if (resposta != JOptionPane.YES_OPTION) {
                break;
            }
        }
    }

    // Meétodo para aplicar projeção ortográfica
    void aplicarProjecaoOrtografica() {
        pontosProjetados = Projecoes.ortografica(pontosSolido3D);
        desenharProjecao();
    }

    // Metodo para aplicar projeção oblíqua
    void aplicarProjecaoObliqua() {
        double angulo = Double.parseDouble(JOptionPane.showInputDialog("Digite o ângulo da projeção oblíqua:"));
        pontosProjetados = Projecoes.obliqua(pontosSolido3D, angulo);
        desenharProjecao();
    }

    // Metodo para aplicar projeção em perspectiva
    void aplicarProjecaoPerspectiva() {
        double distancia = Double.parseDouble(JOptionPane.showInputDialog("Digite a distância da projeção perspectiva:"));
        pontosProjetados = Projecoes.perspectiva(pontosSolido3D, distancia);
        desenharProjecao();
    }

    // Metodo para desenhar a projeção com Bresenham
    private void desenharProjecao() {
        pontosDesenho.clear();
        for (int i = 0; i < pontosProjetados.size() - 1; i++) {
            Ponto p1 = pontosProjetados.get(i);
            Ponto p2 = pontosProjetados.get(i + 1);
            desenharLinha(p1, p2);
        }
        updateCoordenadasArea(pontosProjetados);
        repaint();
    }

    // Métodos de Transformação
    public void aplicarTranslacao() {
        int dx = Integer.parseInt(JOptionPane.showInputDialog("Digite o deslocamento em X:"));
        int dy = Integer.parseInt(JOptionPane.showInputDialog("Digite o deslocamento em Y:"));
        Transformacoes transformacoes = new Transformacoes();
        pontosDesenho = Transformacoes.transladar(pontosDesenho, dx, dy);
        updateCoordenadasArea(pontosDesenho);
    }

    public void aplicarRotacao() {
        double angulo = Double.parseDouble(JOptionPane.showInputDialog("Digite o ângulo de rotação (em graus):"));
        int px = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada X do ponto de pivô:"));
        int py = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada Y do ponto de pivô:"));
        pontosDesenho = Transformacoes.rotacionar(pontosDesenho, angulo, new Ponto(px, py));
        updateCoordenadasArea(pontosDesenho);
    }

    public void aplicarEscala() {
        double escalaX = Double.parseDouble(JOptionPane.showInputDialog("Digite o fator de escala em X:"));
        double escalaY = Double.parseDouble(JOptionPane.showInputDialog("Digite o fator de escala em Y:"));
        int px = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada X do ponto fixo:"));
        int py = Integer.parseInt(JOptionPane.showInputDialog("Digite a coordenada Y do ponto fixo:"));
        pontosDesenho = Transformacoes.escalarContorno(pontosDesenho, escalaX, escalaY, new Ponto(px, py));
        updateCoordenadasArea(pontosDesenho);
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

        if ("Recorte".equals(selectedAlgorithm) || "RecortePoligono".equals(selectedAlgorithm)) {
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
        pontosPolilinha.clear();
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
        updateCoordenadasArea(pontosDesenho);
    }

    private void aplicarRecortePoligono() {
        if (recorteCanto1 != null && recorteCanto2 != null) {
            int xMin = Math.min(recorteCanto1.getX(), recorteCanto2.getX());
            int yMin = Math.min(recorteCanto1.getY(), recorteCanto2.getY());
            int xMax = Math.max(recorteCanto1.getX(), recorteCanto2.getX());
            int yMax = Math.max(recorteCanto1.getY(), recorteCanto2.getY());

            RecorteDePoligono recorte = new RecorteDePoligono(xMin, yMin, xMax, yMax);
            ArrayList<Ponto> pontosRecortados = recorte.recortarPoligono(pontosPolilinha);

            pontosDesenho.clear();
            pontosDesenho.addAll(pontosRecortados);
            updateCoordenadasArea(pontosDesenho);

        }
    }






    private void handleElipseClick(int gridX, int gridY) {
        if (clickCount == 0) {
            centroElipse = new Ponto(gridX, gridY);
            pontosClicados.add(centroElipse); // Adiciona o centro aos pontos temporários
            clickCount++;
            JOptionPane.showMessageDialog(this, "Clique para definir os raios da elipse.");
        } else if (clickCount == 1) {
            raioX = Math.abs(gridX - centroElipse.getX());
            raioY = Math.abs(gridY - centroElipse.getY());
            Ponto pontoRaio = new Ponto(gridX, gridY);
            pontosClicados.add(pontoRaio); // Adiciona o ponto do raio aos pontos temporários
            executeElipse(raioX, raioY, centroElipse);
            clickCount = 0;
            pontosClicados.clear(); // Limpa os pontos temporários após desenhar a elipse
        }
        updateCoordenadasArea(pontosDesenho);
    }

    private void executeElipse(int raioX, int raioY, Ponto centro) {
        Elipse elipse = new Elipse(raioX, raioY, centro);
        pontosDesenho = elipse.getPontos();
        updateCoordenadasArea(pontosDesenho);
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
            updateCoordenadasArea(pontosDesenho);
        }
    }

    private void handlePolilinhasClick(MouseEvent e) {
        int gridX = e.getX() / PIXEL_SIZE;
        int gridY = e.getY() / PIXEL_SIZE;

        polilinhaPontos.add(new Ponto(gridX, gridY));
        pontosEntrada.add(new Ponto(gridX, gridY));

        if (SwingUtilities.isRightMouseButton(e) && polilinhaPontos.size() >= 3) {
            executePolilinhas(polilinhaPontos);
            pontosPolilinha.addAll(polilinhaPontos);
            polilinhaPontos.clear();
        }
        repaint();
    }


    private void desenharLinha(Ponto p1, Ponto p2) {
        Bresenham bresenham = new Bresenham(p1, p2);
        pontosDesenho.addAll(bresenham.getPontos());
        linhasDesenhadas.add(new Ponto[]{p1, p2});
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
        updateCoordenadasArea(pontosDesenho);
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
        updateCoordenadasArea(pontosDesenho);
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
        // Desenhar os pontos que o usuário clicou em azul
        g2d.setColor(Color.BLUE);
        for (Ponto ponto : pontosClicados) {
            int screenX = ponto.getX() * PIXEL_SIZE;
            int screenY = ponto.getY() * PIXEL_SIZE;
            g2d.fillOval(screenX, screenY, PIXEL_SIZE, PIXEL_SIZE);
        }

        // Desenhar a elipse final em vermelho
        g2d.setColor(Color.RED);
        for (Ponto ponto : pontosDesenho) {
            int screenX = ponto.getX() * PIXEL_SIZE;
            int screenY = ponto.getY() * PIXEL_SIZE;
            g2d.fillRect(screenX, screenY, PIXEL_SIZE, PIXEL_SIZE);
        }

    }

}
