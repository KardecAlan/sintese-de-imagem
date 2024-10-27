package br.com.kardec.algoritmos;

import java.awt.Color;
import java.util.ArrayList;

public class PreenchimentoRecursivo {
    public ArrayList<Ponto> listapontos = new ArrayList<>();
    public Color[][] grid;

    public PreenchimentoRecursivo(Ponto ponto, ArrayList<Ponto> pontos, int numPontos, Color cor, Color fillColor) {
        grid = new Color[numPontos][numPontos];
        for (Ponto p : pontos) {
            grid[p.x][p.y] = cor;
        }
        preencher(ponto.getX(), ponto.getY(), cor, Color.YELLOW, grid);
    }

    private void preencher(int x, int y, Color cor, Color coralvo, Color[][] grid) {
        Color pontoCor = grid[x][y];
        if(pontoCor == cor || pontoCor == coralvo){
            return;
        }
        listapontos.add(new Ponto(x, y));
        grid[x][y] = coralvo;
        preencher(x + 1, y, cor, coralvo,grid);
        preencher(x, y + 1, cor, coralvo,grid);
        preencher(x - 1, y, cor, coralvo,grid);
        preencher(x, y - 1, cor, coralvo,grid);
    }
}
