package com.senai.dumbquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Desafio03 extends AppCompatActivity {
    private ImageView certo;
    private Cronometro cronometro;
    Singleton pontos = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desafio03);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cronometro = new Cronometro();
        cronometro.iniciar();
        certo = findViewById(R.id.tarta);
        certo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tempoDecorrido = cronometro.parar();
                int segundos = (int) (tempoDecorrido / 1000);
                pontos.calcula_Tempo(segundos);

                Intent intent = new Intent(Desafio03.this,Desafio04.class);
                startActivity(intent);
                finish();
            }
        });
        Log.d( "Valor03", "Quantidade de pontos: " + pontos.getTempo_Total());
    }
    public void erro(View v){
        long tempoDecorrido = cronometro.parar();
        int segundos = (int) (tempoDecorrido / 1000);
        pontos.calcula_Tempo(segundos);
        Intent intent = new Intent(Desafio03.this,GameOver.class);
        intent.putExtra("origem", "Desafio03");
        startActivity(intent);
        finish();
    }

}