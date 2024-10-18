package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Circulo {
    public ArrayList<Ponto> listapontos = new ArrayList<>();

    public Circulo(int raio, Ponto centro) {
        pontosCirculo(raio, centro);
    }

    public ArrayList<Ponto> getPontos() {
        return listapontos;
    }

    private void pontosCirculo(int raio, Ponto centro) {
        int x = 0;
        int y = raio;
        int e = -raio;

        addPonto(x, y, centro);
        while (y > x) {
            if (e < 0) {
                e += 2 * x + 3;
            } else {
                e += 2 * (x - y) + 5;
                y--;
            }
            x++;
            addPonto(x, y, centro);
        }
    }

    private void addPonto(int x, int y, Ponto centro) {
        listapontos.add(new Ponto(centro.getX() + x, centro.getY() + y));
        listapontos.add(new Ponto(centro.getX() + x, centro.getY() - y));
        listapontos.add(new Ponto(centro.getX() + y, centro.getY() + x));
        listapontos.add(new Ponto(centro.getX() + y, centro.getY() - x));
        listapontos.add(new Ponto(centro.getX() - x, centro.getY() + y));
        listapontos.add(new Ponto(centro.getX() - x, centro.getY() - y));
        listapontos.add(new Ponto(centro.getX() - y, centro.getY() + x));
        listapontos.add(new Ponto(centro.getX() - y, centro.getY() - x));
    }
}
