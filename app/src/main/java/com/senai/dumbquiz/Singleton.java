package com.senai.dumbquiz;

public class Singleton {

        private static Singleton instance;
        private int tempo_Total;

        private Singleton() {}

        public static Singleton getInstance() {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }

        public int getTempo_Total() {
            return tempo_Total ;
        }

        public void setTempo_Total(int valor) {
            this.tempo_Total = valor;
        }
        public void calcula_Tempo (int soma){
            this.tempo_Total+=soma;
        }


}
