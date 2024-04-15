package com.senai.dumbquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameOver extends AppCompatActivity {
    Singleton pontuacao = Singleton.getInstance();
    private String usuarioId;
    private TextView pontosView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pontosView = findViewById(R.id.ponto);
        calculaPontuacao();
    }

    public void recomecar(View view){
        Intent intent = new Intent(GameOver.this, Desafio01.class);
        startActivity(intent);
        finish();
    }
    public void menu(View view){
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private  void calculaPontuacao(){
        String origem = getIntent().getStringExtra("origem");
        int tempo = pontuacao.getTempo_Total();
        double peso = 0;
        if("Desafio01".equals(origem)){
            peso = 1;
        } else if ("Desafio02".equals(origem)) {
            peso = 2;
        }else if ("Desafio03".equals(origem)){
            peso = 3;
        } else if ("Desafio04".equals(origem)) {
            peso = 4;
        } else if ("Desafio05".equals(origem)) {
            peso = 5;
        }


        double pontuacao = (1.0 / tempo) * (1000*peso);
        String pontos = Double.toString(pontuacao);
        String valorArredondado = String.format("%.2f", pontuacao);
        pontosView.setText(valorArredondado);
        salvarPontos(valorArredondado);
    }

    private void salvarPontos(String pontuacao){
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            FirebaseFirestore database = FirebaseFirestore.getInstance();

            Map<String, Object> usuarios = new HashMap<>();
            usuarios.put("pontos", pontuacao);

            usuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = database.collection("usuarios").document(usuarioId);
            documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("db", "Sucesso ao salvar");
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db_error", "Erro ao Slavar Dados" + e.toString());
                        }
                    });
        }
    }
}