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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {
    private TextView cadastrar;
    private EditText user, senha;
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializar();

    }
    private void inicializar(){

        senha = findViewById(R.id.senha);
        cadastrar = findViewById(R.id.buttoncadastrar);
        user = findViewById(R.id.username);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPassword = senha.getText().toString();
                String username = user.getText().toString();
                if(username.isEmpty() ||  userPassword.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    cadastrarUsuario(v);
                }

            }
        });
    }

    private void cadastrarUsuario(View v) {
        String username = user.getText().toString();
        String userPassword = senha.getText().toString();
        String email = username + "@dumb.com";

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Snackbar snackbar = Snackbar.make(v,"Usuario Cadastrado com sucesso", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    Intent intent = new Intent(Cadastro.this, Login.class);

                    startActivity(intent);
                    finish();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException weakPasswordException) {
                        erro = "Digite uma senha com no mínimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException userCollisionException) {
                        erro = "Conta já cadastrada";

                    } catch (Exception e) {
                        erro = "Erro ao cadastrar usuário";
                    }
                    Snackbar s = Snackbar.make(v, erro, Snackbar.LENGTH_LONG);
                    s.show();
                }
            }
        });
    }

}