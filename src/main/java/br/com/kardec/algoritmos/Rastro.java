package br.com.kardec.algoritmos;

public class Rastro {
    private Ponto3D pontoOriginal;
    private Ponto pontoProjetado;

    public Rastro(Ponto3D pontoOriginal, Ponto pontoProjetado) {
        this.pontoOriginal = pontoOriginal;
        this.pontoProjetado = pontoProjetado;
    }

    public Ponto3D getPontoOriginal() {
        return pontoOriginal;
    }

    public Ponto getPontoProjetado() {
        return pontoProjetado;
    }
}
