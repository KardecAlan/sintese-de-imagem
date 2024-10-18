package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Bresenham {
    private boolean mudaX;
    private boolean mudaY;
    private boolean mudaXY;
    public ArrayList <Ponto> listapontos = new ArrayList<>();


    public ArrayList<Ponto> getPontos() {
        return listapontos;
    }
    Bresenham(){
        this.mudaX = mudaX;
        this.mudaY = mudaY;
        this.mudaXY = mudaXY;
    }
    public Bresenham(Ponto p1, Ponto p2) {
        this();
        pontos(p1, p2);
    }

    public void pontos(Ponto p1, Ponto p2) {
        octante(p1, p2);
        reflexao(this.listapontos);
        bresenham(this.listapontos, p1, p2);

    }
    private void octante(Ponto p1, Ponto p2) {
        int deltaX = p2.getX() - p1.getX();
        int deltaY = p2.getY() - p1.getY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0 && deltaY > 0) {
                this.mudaX = true;
            } else if (deltaX < 0 && deltaY >= 0) {
                this.mudaX = true;
                this.mudaY = true;
            } else if (deltaX < 0 && deltaY < 0) {
                this.mudaY = true;
            } else {
                this.mudaY = true;
                this.mudaX = true;
            }
        } else {
            if (deltaX > 0 && deltaY > 0) {
                this.mudaXY = true;
            } else if (deltaX <= 0 && deltaY >= 0) {
                this.mudaX = true;
                this.mudaXY = true;
            } else if (deltaX < 0 && deltaY < 0) {
                this.mudaX = true;
                this.mudaY = true;
                this.mudaXY = true;
            } else {
                this.mudaY = true;
                this.mudaXY = true;
            }
        }
    }

    private void reflexao(ArrayList<Ponto> listapontos){
        if (this.mudaX) {
            for (Ponto p : listapontos) {
                p.setX(-p.getX());
            }
        }
        if (this.mudaY) {
            for (Ponto p : listapontos) {
                p.setY(-p.getY());
            }
        }
        if (this.mudaXY) {
            for (Ponto p : listapontos) {
                int aux = p.getX();
                p.setX(p.getY());
                p.setY(aux);
            }
        }
    }

    private void bresenham(ArrayList<Ponto> listapontos,Ponto p1, Ponto p2){
        int x = p1.getX();
        int y = p1.getY();
        int dX = Math.abs(p2.getX() - p1.getX());
        int dY = Math.abs(p2.getY() - p1.getY());
        int sX = (p1.getX() < p2.getX()) ? 1 : -1;
        int sY = (p1.getY() < p2.getY()) ? 1 : -1;
        float err = dX - dY;

        this.listapontos.add(p1);

        while (x != p2.getX() || y != p2.getY()) {
            float erro = 2 * err;
            if (erro > -dY) {
                err -= dY;
                x += sX;
            }
            if (erro < dX) {
                err += dX;
                y += sY;
            }
            this.listapontos.add(new Ponto(x, y));
        }
    }
}
