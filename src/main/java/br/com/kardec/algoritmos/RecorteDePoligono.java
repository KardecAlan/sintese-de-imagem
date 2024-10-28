package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class RecorteDePoligono {
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private int xMin, yMin, xMax, yMax;

    public RecorteDePoligono(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    // Metodo para calcular o código de região de um ponto
    private int computeCode(int x, int y) {
        int code = INSIDE;
        if (x < xMin) code |= LEFT;
        else if (x > xMax) code |= RIGHT;
        if (y < yMin) code |= BOTTOM;
        else if (y > yMax) code |= TOP;
        return code;
    }

    //Metodo de recorte de linha usando o algoritmo de Cohen-Sutherland
    private ArrayList<Ponto> recortarLinha(Ponto p1, Ponto p2) {
        ArrayList<Ponto> pontosLinha = new ArrayList<>();
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        int code1 = computeCode(x1, y1);
        int code2 = computeCode(x2, y2);

        while (true) {
            if ((code1 | code2) == 0) { // Ambos os pontos estão dentro
                pontosLinha.add(new Ponto(x1, y1));
                pontosLinha.add(new Ponto(x2, y2));
                break;
            } else if ((code1 & code2) != 0) { // Ambos os pontos estão fora
                break;
            } else {
                int codeOut;
                int x = 0, y = 0;

                if (code1 != 0) codeOut = code1;
                else codeOut = code2;

                if ((codeOut & TOP) != 0) {
                    x = x1 + (x2 - x1) * (yMax - y1) / (y2 - y1);
                    y = yMax;
                } else if ((codeOut & BOTTOM) != 0) {
                    x = x1 + (x2 - x1) * (yMin - y1) / (y2 - y1);
                    y = yMin;
                } else if ((codeOut & RIGHT) != 0) {
                    y = y1 + (y2 - y1) * (xMax - x1) / (x2 - x1);
                    x = xMax;
                } else if ((codeOut & LEFT) != 0) {
                    y = y1 + (y2 - y1) * (xMin - x1) / (x2 - x1);
                    x = xMin;
                }

                if (codeOut == code1) {
                    x1 = x;
                    y1 = y;
                    code1 = computeCode(x1, y1);
                } else {
                    x2 = x;
                    y2 = y;
                    code2 = computeCode(x2, y2);
                }
            }
        }

        // Converte a linha recortada para uma lista de pontos discretos
        Bresenham bresenham = new Bresenham(new Ponto(x1, y1), new Ponto(x2, y2));
        pontosLinha.addAll(bresenham.getPontos());
        return pontosLinha;
    }

    // Método para recortar uma polilinha inteira, segmentando linha a linha
    public ArrayList<Ponto> recortarPoligono(ArrayList<Ponto> pontosPolilinha) {
        ArrayList<Ponto> pontosDentroDoRecorte = new ArrayList<>();

        // Percorre a polilinha segmentando linha a linha e aplicando o recorte
        for (int i = 0; i < pontosPolilinha.size() - 1; i++) {
            Ponto p1 = pontosPolilinha.get(i);
            Ponto p2 = pontosPolilinha.get(i + 1);
            ArrayList<Ponto> linhaRecortada = recortarLinha(p1, p2);
            pontosDentroDoRecorte.addAll(linhaRecortada); // Adiciona todos os pontos dentro do recorte
        }

        return pontosDentroDoRecorte;
    }
}
