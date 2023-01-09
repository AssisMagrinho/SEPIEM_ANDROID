package com.example.app_sepiem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class perfilEscola extends AppCompatActivity {

    TextView designacaoEscola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_escola);

        designacaoEscola = findViewById(R.id.txtPerfilNomeEscola);

        designacaoEscola.setText(getIntent().getStringExtra("designacao"));

    }
}