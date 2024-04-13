package com.senai.dumbquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private EditText user,senha;
    private Button logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        inicializar();

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u_Name = user.getText().toString();
                String u_Senha = senha.getText().toString();
                if(u_Name.isEmpty() || u_Senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,"Preencha todos os campos", Snackbar.LENGTH_SHORT);

                    snackbar.show();
                }else{
                    autenticar(v);
                }
            }
        });
    }
    private void inicializar(){
        user = findViewById(R.id.username);
        senha = findViewById(R.id.senha);
        logar = findViewById(R.id.buttonLogin);
        TextView cadastrar = findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a atividade de cadastro quando o texto "Cadastrar" for clicado
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
            }
        });
    }

    private void autenticar(View v) {
        String u_Name = user.getText().toString();
        String u_Senha = senha.getText().toString();
        String email = u_Name + "@dumb.com";
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, u_Senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException weakPasswordException) {
                        erro = "Senha Incorreta";

                    } catch (FirebaseAuthInvalidCredentialsException invalidCredentialsException) {
                        erro = "Username inválido";
                    } catch (Exception e) {
                        erro = "Erro ao Logar usuário";
                    }
                    Snackbar s = Snackbar.make(v, erro, Snackbar.LENGTH_LONG);
                    s.show();
                }
            }
        });
    }
}