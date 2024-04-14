package com.senai.dumbquiz;

public class Cronometro {
    private long inicio;

    public void iniciar() {
        inicio = System.currentTimeMillis();
    }

    public long parar() {
        return System.currentTimeMillis() - inicio;
    }

    public String tempoString(long tempoDecorrido){

        int segundos = (int) (tempoDecorrido / 1000);
        int minutos = segundos / 60;
        segundos = segundos % 60;
        minutos = minutos % 60;

        String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
        return tempoFormatado;
    }
}

