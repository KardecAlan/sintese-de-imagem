package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Transformacoes {

    // Método para translação
    public static ArrayList<Ponto> transladar(ArrayList<Ponto> pontos, int deslocamentoX, int deslocamentoY) {
        ArrayList<Ponto> pontosTransladados = new ArrayList<>();
        for (Ponto ponto : pontos) {
            int novoX = ponto.getX() + deslocamentoX;
            int novoY = ponto.getY() + deslocamentoY;
            pontosTransladados.add(new Ponto(novoX, novoY));
        }
        return pontosTransladados;
    }

    // Método para rotação
    public static ArrayList<Ponto> rotacionar(ArrayList<Ponto> pontos, double angulo, Ponto pontoPivo) {
        ArrayList<Ponto> pontosRotacionados = new ArrayList<>();
        double radianos = Math.toRadians(angulo);

        for (Ponto ponto : pontos) {
            int x = ponto.getX() - pontoPivo.getX();
            int y = ponto.getY() - pontoPivo.getY();

            int novoX = (int) Math.round(x * Math.cos(radianos) - y * Math.sin(radianos)) + pontoPivo.getX();
            int novoY = (int) Math.round(x * Math.sin(radianos) + y * Math.cos(radianos)) + pontoPivo.getY();

            pontosRotacionados.add(new Ponto(novoX, novoY));
        }
        return pontosRotacionados;
    }

    // Método para aplicar escala com preenchimento apenas no contorno
    public static ArrayList<Ponto> escalarContorno(ArrayList<Ponto> pontosOriginais, double fatorEscalaX, double fatorEscalaY, Ponto pontoFixo) {
        ArrayList<Ponto> pontosEscalados = new ArrayList<>();

        // Escalar cada ponto do contorno
        for (int i = 0; i < pontosOriginais.size(); i++) {
            Ponto ponto = pontosOriginais.get(i);

            // Calcula as novas coordenadas escaladas
            int novoX = (int) Math.round(pontoFixo.getX() + (ponto.getX() - pontoFixo.getX()) * fatorEscalaX);
            int novoY = (int) Math.round(pontoFixo.getY() + (ponto.getY() - pontoFixo.getY()) * fatorEscalaY);

            // Adiciona o ponto escalado ao contorno
            pontosEscalados.add(new Ponto(novoX, novoY));

            // Conecta o ponto escalado atual com o próximo ponto para preencher apenas as linhas de contorno
            if (i < pontosOriginais.size() - 1) {
                Ponto proximoPonto = pontosOriginais.get(i + 1);
                int proximoX = (int) Math.round(pontoFixo.getX() + (proximoPonto.getX() - pontoFixo.getX()) * fatorEscalaX);
                int proximoY = (int) Math.round(pontoFixo.getY() + (proximoPonto.getY() - pontoFixo.getY()) * fatorEscalaY);

                // Preenche os pontos entre o ponto atual e o próximo ponto para desenhar apenas o contorno
                pontosEscalados.addAll(preencherLinha(novoX, novoY, proximoX, proximoY));
            }
        }

        return pontosEscalados;
    }

    // Método auxiliar para preencher uma linha entre dois pontos
    private static ArrayList<Ponto> preencherLinha(int x1, int y1, int x2, int y2) {
        ArrayList<Ponto> linha = new ArrayList<>();

        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            linha.add(new Ponto(x1, y1));

            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }

        return linha;
    }
}
