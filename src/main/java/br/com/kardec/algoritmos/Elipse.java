package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Elipse {
    public ArrayList<Ponto> listapontos = new ArrayList<>();

    public Elipse(int raioX, int raioY, Ponto centro) {
        pontosElipse(raioX, raioY, centro);
    }

    public ArrayList<Ponto> getPontos() {
        return listapontos;
    }

    private void pontosElipse(int raioX, int raioY, Ponto centro) {
        int x = 0;
        int y = raioY;

        // Inicializa as variáveis de decisão
        int raioX2 = raioX * raioX;
        int raioY2 = raioY * raioY;
        int doisRaioX2 = 2 * raioX2;
        int doisRaioY2 = 2 * raioY2;

        int px = 0;
        int py = doisRaioX2 * y;

        // Região 1
        int p = (int) (raioY2 - (raioX2 * raioY) + (0.25 * raioX2));
        while (px < py) {
            addPonto(x, y, centro);
            x++;
            px += doisRaioY2;

            if (p < 0) {
                p += raioY2 + px;
            } else {
                y--;
                py -= doisRaioX2;
                p += raioY2 + px - py;
            }
        }

        // Região 2
        p = (int) (raioY2 * (x + 0.5) * (x + 0.5) + raioX2 * (y - 1) * (y - 1) - raioX2 * raioY2);
        while (y >= 0) {
            addPonto(x, y, centro);
            y--;
            py -= doisRaioX2;

            if (p > 0) {
                p += raioX2 - py;
            } else {
                x++;
                px += doisRaioY2;
                p += raioX2 - py + px;
            }
        }
    }

    private void addPonto(int x, int y, Ponto centro) {
        listapontos.add(new Ponto(centro.getX() + x, centro.getY() + y));
        listapontos.add(new Ponto(centro.getX() + x, centro.getY() - y));
        listapontos.add(new Ponto(centro.getX() - x, centro.getY() + y));
        listapontos.add(new Ponto(centro.getX() - x, centro.getY() - y));
    }
}
