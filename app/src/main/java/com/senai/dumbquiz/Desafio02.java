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

public class Desafio02 extends AppCompatActivity {
    private ImageView certo;
    private Cronometro cronometro;
    Singleton pontos = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desafio02);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cronometro = new Cronometro();
        cronometro.iniciar();
        certo = findViewById(R.id.op1999);
        certo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tempoDecorrido = cronometro.parar();
                int segundos = (int) (tempoDecorrido / 1000);
                pontos.calcula_Tempo(segundos);

                Intent intent = new Intent(Desafio02.this,Desafio03.class);
                startActivity(intent);
                finish();
            }
        });
        Log.d( "Valor", "Quantidade de pontos: " + pontos.getTempo_Total());
    }
    public void erro(View v){
        Intent intent = new Intent(Desafio02.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
