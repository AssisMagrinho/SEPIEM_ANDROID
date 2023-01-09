package com.example.app_sepiem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

public class activityEscola extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    AdapterEscolas adapterEscolas;
    ArrayList<Escolas> list;

    private TextView designacaoEscola;





    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escola);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Escolas");
        checkConnection();





        recyclerView = findViewById(R.id.tb_Escolas);
        designacaoEscola = findViewById(R.id.txtDesignacaoEscola);


        databaseReference = FirebaseDatabase.getInstance().getReference("Escolas");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapterEscolas = new AdapterEscolas(this, list);
        recyclerView.setAdapter(adapterEscolas);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Escolas escolas = dataSnapshot.getValue(Escolas.class);
                    list.add(escolas);
                }

                adapterEscolas.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    private void showDialogInterConnection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.internetconnection);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork){

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                // Toast.makeText(this, "Wifi Habilitado !!!", Toast.LENGTH_LONG).show();
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                // Toast.makeText(this, "Dados Móveis Habiltados !!!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            showDialogInterConnection();
            //Toast.makeText(this, "Sem Ligação à Internet !!!", Toast.LENGTH_LONG).show();
        }
    }
}