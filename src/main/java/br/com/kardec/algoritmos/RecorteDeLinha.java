package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class RecorteDeLinha {
    private static final int INSIDE = 0; // 0000
    private static final int LEFT = 1;   // 0001
    private static final int RIGHT = 2;  // 0010
    private static final int BOTTOM = 4; // 0100
    private static final int TOP = 8;    // 1000

    private int xMin, yMin, xMax, yMax;

//    public RecorteDeLinha(Ponto ponto1, Ponto ponto2) {
//        // Define os limites de recorte com os pontos que o usuário clicar
//        this.xMin = Math.min(ponto1.getX(), ponto2.getX());
//        this.yMin = Math.min(ponto1.getY(), ponto2.getY());
//        this.xMax = Math.max(ponto1.getX(), ponto2.getX());
//        this.yMax = Math.max(ponto1.getY(), ponto2.getY());
//    }

    public RecorteDeLinha(int xMin, int yMin, int xMax, int yMax) {
        // Define os limites de recorte com os pontos que o usuário clicar
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    private int computeCode(int x, int y) {
        int code = INSIDE;
        if (x < xMin) code |= LEFT;
        else if (x > xMax) code |= RIGHT;
        if (y < yMin) code |= BOTTOM;
        else if (y > yMax) code |= TOP;
        return code;
    }

    public ArrayList<Ponto> recortarLinha(Ponto p1, Ponto p2) {
        int x1 = p1.getX(), y1 = p1.getY();
        int x2 = p2.getX(), y2 = p2.getY();
        int code1 = computeCode(x1, y1);
        int code2 = computeCode(x2, y2);
        boolean accept = false;

        while (true) {
            if ((code1 | code2) == 0) { // Ambos os pontos estão dentro
                accept = true;
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

        ArrayList<Ponto> pontosRecortados = new ArrayList<>();
        if (accept) {
            pontosRecortados.add(new Ponto(x1, y1));
            pontosRecortados.add(new Ponto(x2, y2));
        }
        return pontosRecortados;
    }

    public boolean estaDentro(Ponto ponto) {
        return ponto.getX() >= xMin && ponto.getX() <= xMax && ponto.getY() >= yMin && ponto.getY() <= yMax;
    }
}
