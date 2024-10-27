package br.com.kardec.algoritmos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class PreenchimentoVarredura {
    public ArrayList<Ponto> listapontos = new ArrayList<>();

    public PreenchimentoVarredura(ArrayList<Ponto> pontos, int numPontos, Color cor) {
        if (pontos.get(0).equals(pontos.get(pontos.size() - 1))  && !pontos.isEmpty()) {
            varrer(pontos);
        }

    }

    public void varrer(ArrayList<Ponto> pontos) {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Ponto p : pontos) {
            int y = p.getY();
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        // verificando interseções
        for (int y = minY + 1; y < maxY; y++) {
            ArrayList<Double> intersecoes = new ArrayList<>();

            for (int i = 0; i < pontos.size(); i++) {
                Ponto p1 = pontos.get(i);
                Ponto p2 = pontos.get((i + 1) % pontos.size());

                int x1 = p1.getX();
                int y1 = p1.getY();
                int x2 = p2.getX();
                int y2 = p2.getY();

                // verifica se a aresta cruza a linha horizontal
                if ((y1 <= y && y2 > y) || (y2 <= y && y1 > y)) {
                    double xIntersecao = (double) (x1 * (y2 - y) + x2 * (y - y1)) / (y2 - y1);
                    intersecoes.add(xIntersecao);
                }
            }

            // Ordenando interseções em ordem crescente
            intersecoes.sort(Double::compare);

            // Pinta as linhas horizontais entre as interseções
            for (int i = 0; i < intersecoes.size(); i += 2) {
                int x1 = (int) Math.ceil(intersecoes.get(i));
                int x2 = (int) Math.floor(intersecoes.get(i + 1));

                for (int x = x1; x <= x2; x++) {
                    listapontos.add(new Ponto(x, y));
                }
            }
        }
    }
}
