package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Curvas {
    public ArrayList<Ponto> listapontos = new ArrayList<>();

    public Curvas(Ponto pInicial, Ponto controle1, Ponto controle2, Ponto pFinal) {
        if (controle2 == null) {
            bezierGrau2(pInicial, controle1, pFinal);
        } else {
            bezierGrau3(pInicial, controle1, controle2, pFinal);
        }
    }

    public ArrayList<Ponto> getPontos() {
        return listapontos;
    }

    private void bezierGrau2(Ponto pInicial, Ponto controle, Ponto pFinal) {
        double i = 0.01;
        Ponto anterior = null;

        for (double t = 0; t <= 1; t += i) {
            double x = coordenadas(t, pInicial.x, controle.x, pFinal.x);
            double y = coordenadas(t, pInicial.y, controle.y, pFinal.y);

            Ponto ponto = new Ponto((int)x, (int)y);

            if (anterior != null) {
                Bresenham b = new Bresenham(anterior, ponto);
                listapontos.addAll(b.listapontos);
            }
            anterior = ponto;
        }
    }

    private void bezierGrau3(Ponto pInicial, Ponto controle1, Ponto controle2, Ponto pFinal) {
        double i = 0.01;
        Ponto anterior = null;

        for (double t = 0; t <= 1; t += i) {
            double x = coordenadas(t, pInicial.x, controle1.x, controle2.x, pFinal.x);
            double y = coordenadas(t, pInicial.y, controle1.y, controle2.y, pFinal.y);

            Ponto ponto = new Ponto((int)x, (int)y);

            if (anterior != null) {
                Bresenham b = new Bresenham(anterior, ponto);
                listapontos.addAll(b.listapontos);
            }
            anterior = ponto;
        }
    }

    private double coordenadas(double t, int... pontos) {
        if (pontos.length == 3) {
            double b = 1 - t;
            return b * b * pontos[0] + 2 * b * t * pontos[1] + t * t * pontos[2];
        } else {
            double b = 1 - t;
            return Math.pow(b, 3) * pontos[0] + 3 * Math.pow(b, 2) * t * pontos[1] + 3 * b * Math.pow(t, 2) * pontos[2] + Math.pow(t, 3) * pontos[3];
        }
    }

}
