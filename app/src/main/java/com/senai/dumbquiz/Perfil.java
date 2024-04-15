package com.senai.dumbquiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    private ImageView fotoPerfil;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Uri uri_imagem;
    private TextView nome, ponto;
    private String usuarioID;

    // Constante para identificar a solicitação de seleção de imagem
    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        // Inicializa a referência para o usuário atual
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Inicializa a referência para o Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();
        // Inicializa a referência para o Firestore
        firestore = FirebaseFirestore.getInstance();

        fotoPerfil = findViewById(R.id.foto_perfil);
        nome = findViewById(R.id.nome);
        ponto = findViewById(R.id.pontos);
    }

    public void obterImagem(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent,"Escolha sua foto de perfil"), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                if(data != null){

                    uri_imagem = data.getData();

                    Glide.with(getBaseContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Toast.makeText(getBaseContext(), "Falha ao selecionar imagem",Toast.LENGTH_LONG).show();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            uploadImagem();
                            return false;
                        }
                    }).into(fotoPerfil);

                    }else{
                    Toast.makeText(getBaseContext(), "Falha ao selecionar imagem2",Toast.LENGTH_LONG).show();
                }
                }
            }
        }



    private void uploadImagem() {
        if (uri_imagem != null) {
            // Obtém uma referência para o local onde a imagem será armazenada no Firebase Storage
            StorageReference imagemRef = storageReference.child("imagens_perfil/" + currentUser.getUid() + "/perfil.jpg");

            // Faz o upload da imagem para o Firebase Storage
            imagemRef.putFile(uri_imagem)
                    .addOnSuccessListener(this, taskSnapshot -> {
                        // Se o upload for bem-sucedido, obtenha a URL da imagem armazenada
                        imagemRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Obtem a URL da imagem
                            String urlImagem = uri.toString();

                            // Armazena a URL da imagem no Firestore
                            atualizarURLImagemUsuario(urlImagem);
                        });
                    })
                    .addOnFailureListener(this, e -> {
                        // Se houver uma falha no upload, exibe uma mensagem de erro
                        Toast.makeText(getApplicationContext(), "Falha ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Nenhuma imagem selecionada para fazer upload", Toast.LENGTH_SHORT).show();
        }
    }
    private void atualizarURLImagemUsuario(String urlImagem) {
        // Obtém uma referência para o documento do usuário no Firestore
        DocumentReference userRef = firestore.collection("usuarios").document(currentUser.getUid());

        // Atualiza o campo 'urlImagemPerfil' do documento do usuário com a URL da imagem
        userRef.update("urlImagemPerfil", urlImagem)
                .addOnSuccessListener(aVoid -> {
                    // Se a atualização for bem-sucedida, exibe uma mensagem de sucesso
                    Toast.makeText(getApplicationContext(), "Imagem de perfil atualizada com sucesso", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Se houver uma falha na atualização, exibe uma mensagem de erro
                    Toast.makeText(getApplicationContext(), "Erro ao atualizar imagem de perfil", Toast.LENGTH_SHORT).show();
                });
    }
    private void exibirPerfilUsuario() {
        // Verifica se o usuário está autenticado
        if (currentUser != null) {
            // Obtém o email do usuário
            String userName = currentUser.getEmail();

            // Exibe o email do usuário
            // Supondo que você tenha um TextView chamado 'nome' no seu layout
            nome.setText(userName);

            // Obtém a URL da imagem de perfil do usuário (se existir)

            Log.d( "Imagem", "Quantidade de pontos: " + currentUser.getPhotoUrl().toString());
            // Se a URL da imagem de perfil estiver disponível, carrega a imagem usando Glide

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        int posicaoArroba = username.indexOf('@');

        String nomeUsuario = username.substring(0, posicaoArroba);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    nome.setText(nomeUsuario);
                    String vPontos = value.getString("pontos");
                    String pontosFormat = String.format("%s Pontos",vPontos );
                    ponto.setText(pontosFormat);
                    carregarImagemPerfil();
                }
            }
        });
    }
    public void deslogar(View v){

        FirebaseAuth.getInstance().signOut();


        Intent intent = new Intent(Perfil.this, Login.class);
        startActivity(intent);
    }

    private void carregarImagemPerfil() {
        // Obtém uma referência para o documento do usuário no Firestore
        DocumentReference userRef = firestore.collection("usuarios").document(currentUser.getUid());

        // Recupera os dados do documento do usuário
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            // Verifica se o documento existe
            if (documentSnapshot.exists()) {
                // Obtém a URL da imagem de perfil do usuário do Firestore
                String urlImagemPerfil = documentSnapshot.getString("urlImagemPerfil");

                // Verifica se a URL da imagem de perfil está disponível
                if (urlImagemPerfil != null && !urlImagemPerfil.isEmpty()) {
                    // Use Glide ou outra biblioteca de carregamento de imagem para carregar e exibir a imagem no ImageView
                    Glide.with(getApplicationContext())
                            .load(urlImagemPerfil)
                            .placeholder(R.drawable.placeholder_image) // Placeholder, caso a imagem demore a carregar
                            .error(R.drawable.error_image) // Imagem de erro, caso ocorra algum problema ao carregar a imagem
                            .into(fotoPerfil);
                } else {
                    // Se a URL da imagem de perfil não estiver disponível, você pode exibir uma imagem padrão ou outro indicador
                    fotoPerfil.setImageResource(R.drawable.bitcoin);
                }
            }
        }).addOnFailureListener(e -> {
            // Se houver uma falha ao recuperar os dados do Firestore, exiba uma mensagem de erro ou trate a falha de acordo com sua lógica de negócios
            Toast.makeText(getApplicationContext(), "Falha ao carregar imagem de perfil", Toast.LENGTH_SHORT).show();
        });
    }
    public void voltarMenu(View v){
        Intent intent = new Intent(Perfil.this, MainActivity.class);
        startActivity(intent);
    }
}

