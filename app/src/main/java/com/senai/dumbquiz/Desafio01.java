package com.senai.dumbquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class Desafio01 extends AppCompatActivity {
    private ImageView certo;
    private Cronometro cronometro; // Adicione a declaração do cronômetro
    Singleton pontos = Singleton.getInstance();
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desafio01);

        // Inicialize o cronômetro
        cronometro = new Cronometro();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        texto = findViewById(R.id.cronometroText);
        cronometro.iniciar();
        pontos.setTempo_Total(0);
        certo = findViewById(R.id.bitcoin);
        certo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tempoDecorrido = cronometro.parar();
                int segundos = (int) (tempoDecorrido / 1000);
                pontos.calcula_Tempo(segundos);

                Intent intent = new Intent(Desafio01.this,Desafio02.class);
                startActivity(intent);
                finish();
            }
        });

        exibirTempo();

    }

    public void erro(View v){
        long tempoDecorrido = cronometro.parar();
        int segundos = (int) (tempoDecorrido / 1000);
        pontos.calcula_Tempo(segundos);
        Intent intent = new Intent(Desafio01.this,GameOver.class);
        intent.putExtra("origem", "Desafio01");
        startActivity(intent);
        finish();
    }

    // Corrija a assinatura do método exibirTempo
    public void exibirTempo(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Obtenha o tempo decorrido do cronômetro e exiba-o no TextView
                long tempoDecorrido = cronometro.parar();
                texto.setText(cronometro.tempoString(tempoDecorrido));

                // Execute novamente o método postDelayed após 1000 ms
                handler.postDelayed(this, 1000);
            }
        });
    }
}
