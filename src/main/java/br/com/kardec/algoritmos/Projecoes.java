package br.com.kardec.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Projecoes {

    public static ArrayList<Ponto> ortografica(List<Ponto3D> pontos3D) {
        ArrayList<Ponto> pontos2D = new ArrayList<>();
        for (Ponto3D ponto : pontos3D) {
            pontos2D.add(new Ponto(ponto.getX(), ponto.getY()));  // Projeção ortográfica (ignora Z)
        }
        return pontos2D;
    }

    public static ArrayList<Ponto> obliqua(List<Ponto3D> pontos3D, double angulo) {
        ArrayList<Ponto> pontos2D = new ArrayList<>();
        double cosAngulo = Math.cos(Math.toRadians(angulo));
        double sinAngulo = Math.sin(Math.toRadians(angulo));

        for (Ponto3D ponto : pontos3D) {
            int x = (int) (ponto.getX() + ponto.getZ() * cosAngulo);
            int y = (int) (ponto.getY() + ponto.getZ() * sinAngulo);
            pontos2D.add(new Ponto(x, y));
        }
        return pontos2D;
    }

    public static ArrayList<Ponto> perspectiva(List<Ponto3D> pontos3D, double distancia) {
        ArrayList<Ponto> pontos2D = new ArrayList<>();

        for (Ponto3D ponto : pontos3D) {
            // Calcula a projeção em perspectiva
            double fator = distancia / (distancia + ponto.getZ());
            int x = (int) (ponto.getX() * fator);
            int y = (int) (ponto.getY() * fator);
            Ponto pontoProjetado = new Ponto(x, y);

            // Cria o rastro usando Bresenham entre o ponto original e o ponto projetado
            Bresenham bresenham = new Bresenham(new Ponto(ponto.getX(), ponto.getY()), pontoProjetado);
            pontos2D.addAll(bresenham.getPontos()); // Adiciona o rastro

            // Adiciona o ponto projetado final
            pontos2D.add(pontoProjetado);
        }
        return pontos2D;
    }
}
