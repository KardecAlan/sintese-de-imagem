package br.com.kardec.algoritmos;

public class Ponto {
    public int x;
    public int y;
    public Ponto(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    //métodos para inverter sinal
    public void iSinalX() {
        this.x = -this.x;
    }

    public void iSinalY() {
        this.y = -this.y;
    }


    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ponto other = (Ponto) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
