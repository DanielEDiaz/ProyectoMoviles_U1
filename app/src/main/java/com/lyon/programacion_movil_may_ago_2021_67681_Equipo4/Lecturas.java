package com.lyon.programacion_movil_may_ago_2021_67681_Equipo4;

public class Lecturas {

    private int idLec;
    private int numeroLectura;
    private String lectura;
    private String tiempo;

    public Lecturas(){}

    public Lecturas(String lectura, String tiempo, int fklec){
        this.lectura = lectura;
        this.tiempo = tiempo;
        this.idLec = fklec;
    }

    public Lecturas(int idLec, String lectura){
        this.idLec = idLec;
        this.lectura = lectura;
    }

    public Lecturas(int numeroLectura, String lectura, String tiempo){
        this.numeroLectura = numeroLectura;
        this.lectura = lectura;
        this.tiempo = tiempo;
    }

    public int getIdLec() {
        return idLec;
    }

    public void setIdLec(int idLec) {
        this.idLec = idLec;
    }

    public int getNumeroLectura() {
        return numeroLectura;
    }

    public void setNumeroLectura(int numeroLectura) {
        this.numeroLectura = numeroLectura;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString(){
        return String.valueOf("Lectura "+this.numeroLectura);
    }
}
