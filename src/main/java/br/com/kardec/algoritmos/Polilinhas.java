package br.com.kardec.algoritmos;

import java.util.ArrayList;

public class Polilinhas {
    public ArrayList<Ponto> listapontos = new ArrayList<>();

    public ArrayList<Ponto> getPontos() {
        return listapontos;
    }

    public Polilinhas(ArrayList<Ponto> pontos) {
        linhas(pontos);
    }

    private void linhas(ArrayList<Ponto> pontos) {
        if (pontos.size() < 2) {
            return; // Se houver menos de 2 pontos, não é possível desenhar a polilinha
        }

        Ponto anterior = pontos.get(0);

        for (int i = 1; i < pontos.size(); i++) {
            Ponto atual = pontos.get(i);
            Bresenham b = new Bresenham(anterior, atual);
            listapontos.addAll(b.getPontos());
            anterior = atual;
        }

        // Conectar o último ponto ao primeiro ponto para fechar a polilinha
        Ponto primeiro = pontos.get(0);
        Bresenham b = new Bresenham(anterior, primeiro);
        listapontos.addAll(b.getPontos());
    }
}

