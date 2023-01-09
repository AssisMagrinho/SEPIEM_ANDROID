package com.example.app_sepiem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MinhasCoordendas extends AppCompatActivity {

    Button btnLocation;
    TextView campo1,campo2, campo3, campo4, campo5;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_coordendas);

        btnLocation = findViewById(R.id.btnLocation);
        campo1 = findViewById(R.id.txt1);
        campo2 = findViewById(R.id.txt2);
        campo3 = findViewById(R.id.txt3);
        campo4 = findViewById(R.id.txt4);
        campo5 = findViewById(R.id.txt5);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(MinhasCoordendas.this
                    , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        getLocation();
                    }else{
                        ActivityCompat.requestPermissions(MinhasCoordendas.this
                        , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null){
                    try {


                        Geocoder geocoder = new Geocoder(MinhasCoordendas.this,
                                Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);

                        campo1.setText(Html.fromHtml(
                                "<font color='#06200EE'><b>Latitude : </b><br></font>"
                                +addresses.get(0).getLatitude()
                        ));

                        campo2.setText(Html.fromHtml(
                                "<font color='#06200EE'><b>Longitude : </b><br></font>"
                                        +addresses.get(0).getLongitude()
                        ));

                        campo3.setText(Html.fromHtml(
                                "<font color='#06200EE'><b>Pais : </b><br></font>"
                                        +addresses.get(0).getCountryName()
                        ));

                        campo4.setText(Html.fromHtml(
                                "<font color='#06200EE'><b>Municipio : </b><br></font>"
                                        +addresses.get(0).getLocality()
                        ));

                        campo5.setText(Html.fromHtml(
                                "<font color='#06200EE'><b>Endereco : </b><br></font>"
                                        +addresses.get(0).getAddressLine(0)
                        ));
                    }

                    catch (IOException e) {
                        Toast.makeText(MinhasCoordendas.this, "ERRO :"+e.toString(), Toast.LENGTH_LONG).show();

                       // e.printStackTrace();
                    }
                }
            }
        });

    }
}