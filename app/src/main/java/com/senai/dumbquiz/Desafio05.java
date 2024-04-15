package com.senai.dumbquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Desafio05 extends AppCompatActivity {
    private Cronometro cronometro;
    private ImageView certo, gol, gol2,gol3;
    private int xDelta = 0;
    private int yDelta = 0;


    Singleton pontos = Singleton.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_desafio05);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cronometro = new Cronometro();
        cronometro.iniciar();
        certo = findViewById(R.id.bola);
        gol = findViewById(R.id.gol);
        gol2 = findViewById(R.id.gol2);
        gol3 = findViewById(R.id.gol3);

        certo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        ConstraintLayout.LayoutParams layoutParamsDown = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                        xDelta = x - layoutParamsDown.leftMargin;
                        yDelta = y - layoutParamsDown.topMargin;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        ConstraintLayout.LayoutParams layoutParamsMove = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                        layoutParamsMove.leftMargin = x - xDelta;
                        layoutParamsMove.topMargin = y - yDelta;
                        v.setLayoutParams(layoutParamsMove);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (checarColisao(v, gol)) {
                            // Faça aqui o que deseja ao atingir o gol
                            Intent intent = new Intent(Desafio05.this, Desafio06.class);
                            startActivity(intent);
                            finish();
                        } else if (checarColisao(v, gol2)) {
                            // Faça aqui o que deseja ao atingir o segundo gol
                            Intent intent = new Intent(Desafio05.this, Desafio06.class);
                            startActivity(intent);
                            finish();
                        }else if(checarColisao(v,gol3)){
                            Intent intent = new Intent(Desafio05.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                }
                return true;
            }
        });


    }

    private boolean checarColisao(@NonNull View view1, @NonNull View view2) {
        Rect rect1 = new Rect(view1.getLeft(), view1.getTop(), view1.getRight(), view1.getBottom());
        Rect rect2 = new Rect(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
        return rect1.intersect(rect2);
    }


}




